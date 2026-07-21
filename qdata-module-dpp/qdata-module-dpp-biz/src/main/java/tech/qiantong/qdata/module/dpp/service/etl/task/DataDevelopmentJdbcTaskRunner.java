/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.module.dpp.service.etl.task;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.oracle.parser.OracleStatementParser;
import com.alibaba.druid.sql.parser.SQLParserUtils;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.api.ds.api.etl.ds.TaskInstance;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.constants.DbType;
import tech.qiantong.qdata.common.database.utils.AesEncryptUtil;
import tech.qiantong.qdata.common.enums.*;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.JSONUtils;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.uuid.IdUtils;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeRespVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlNodeInstanceLogDO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskDO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskInstanceDO;
import tech.qiantong.qdata.module.dpp.service.etl.*;
import tech.qiantong.qdata.module.dpp.utils.log.LogUtils;
import tech.qiantong.qdata.redis.service.IRedisService;

import javax.annotation.Resource;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static tech.qiantong.qdata.common.core.domain.AjaxResult.error;
import static tech.qiantong.qdata.common.core.domain.AjaxResult.success;

/**
 * Data development JDBC task runner.
 * <p>
 * Converts SQL-development and stored-procedure-development nodes in data development tasks into a local JDBC execution flow,
 * with centralized handling for task instances, execution logs, run locks, transaction commits, and exception rollbacks.
 *
 * @author qdata
 */
@Slf4j
@Service
public class DataDevelopmentJdbcTaskRunner {

    @Resource
    private IDppEtlTaskInstanceService dppEtlTaskInstanceService;
    @Resource
    private IDppEtlNodeService iDppEtlNodeService;
    @Resource
    private IRedisService redisService;
    @Resource
    private IDppEtlNodeInstanceService dppEtlNodeInstanceService;
    @Resource
    private IDppEtlNodeInstanceLogService dppEtlNodeInstanceLogService;

    /**
     * Executes the JDBC nodes in a data development task.
     *
     * @param task    Data development task details; must include the node definition list
     * @param instance Current task instance
     * @param taskLog Task execution log buffer
     */
    @Transactional
    public void run(DppEtlTaskDO task, DppEtlTaskInstanceDO instance, StringBuilder taskLog) {
        // Allow only one execution instance of a task at a time to prevent duplicate clicks or scheduler re-entry.
        Date startTime = new Date();
        String redisKey = buildDataDevelopmentRunLockKey(task.getId());
        redisService.delete(redisKey);
        // Failure to acquire the lock means another instance of the same task is already running.
        if (!acquireDataDevelopmentRunLock(redisKey)) {
            throw new RuntimeException(MessageUtils.messageWithFallback(
                    "dpp.error.data.development.task.running",
                    "A previous execution of this data development task is still running. Please try again later."));
        }

        int totalUpdateCount = 0;
        int totalResultCount = 0;
        LogUtils.appendLocalLogLine(taskLog, "***********************************************************************************************");
        LogUtils.appendLocalLogLine(taskLog, "********************************* Load task instance plugin *********************************");
        LogUtils.appendLocalLogLine(taskLog, "***********************************************************************************************");
        LogUtils.appendLocalLogLine(taskLog, "Send task status RUNNING_EXECUTION");
        LogUtils.appendLocalLogLine(taskLog, "Create TaskChannel: qData DataDevelopmentJdbcTaskRunner successfully");

        // Execute only SQL-development and stored-procedure-development nodes; cleaning, input, and output nodes are outside the local JDBC execution scope.
        List<DppEtlNodeRespVO> sqlNodes = getDataDevelopmentSqlNodes(task);
        // Treat a task with no executable JDBC nodes as a configuration error.
        if (CollectionUtils.isEmpty(sqlNodes)) {
            throw new ServiceException("dpp.error.data.development.jdbc.node.missing",
                    "No SQL-development or stored-procedure node was found for the data development task.");
        }

        LogUtils.appendLocalLogLine(taskLog, "********************************* Execute task instance *************************************");
        // Aggregate affected-row and result-row counts so the execution scale is visible in the instance log.
        for (DppEtlNodeRespVO node : sqlNodes) {
            // Create the node instance
            Long nodeInstanceId = createNodeInstance(task, node, instance);
            try {
                LogUtils.appendLocalLogLine(taskLog, "Start executing node: " + node.getName());

                JdbcExecuteResult result = executeDataDevelopmentNodeSql(node, taskLog);
                totalUpdateCount += result.getUpdateCount();
                totalResultCount += result.getResultCount();
                LogUtils.appendLocalLogLine(taskLog, String.format("Node execution completed: %s, affected rows=%d, result rows=%d",
                        node.getName(), result.getUpdateCount(), result.getResultCount()));
                // Mark the node instance as successful
                updateNodeInstance(nodeInstanceId, TaskExecutionStatus.SUCCESS);
                // Mark the task instance as successful
                markDataDevelopmentSuccess(instance);
                LogUtils.appendLocalLogLine(taskLog, String.format("Data development task executed successfully: node count=%d, affected rows=%d, result rows=%d",
                        sqlNodes.size(), totalUpdateCount, totalResultCount));
            } catch (Exception e) {
                // Record business and JDBC exceptions in the instance status and log so the failure reason is visible in the UI.
                markDataDevelopmentFail(instance, e);
                LogUtils.appendLocalLogLine(taskLog, "Data development task execution failed: " + JSONUtils.formatJson(JSONUtils.toJson(e.getMessage())));
            } finally {
                // Always release the lock and write the complete execution log once at the end of the method.
                redisService.delete(redisKey);
                // Set the end time during final cleanup if the failure path has not already set it.
                if (instance.getEndTime() == null) {
                    instance.setEndTime(new Date());
                }
                LogUtils.appendLocalLogLine(taskLog, "Data development task execution finished, duration: " + calcDurationSeconds(startTime) + " seconds");
                safeDataDevelopmentLog(nodeInstanceId, instance.getId(), task, taskLog.toString());
            }
        }
    }

    /**
     * Creates a data development node instance.
     *
     * @param task     Data development task details
     * @param node     Current node configuration
     * @param instance Owning task instance
     * @return Generated node instance ID
     */
    private Long createNodeInstance(DppEtlTaskDO task, DppEtlNodeRespVO node, DppEtlTaskInstanceDO instance) {
        log.info("Starting task instance creation");
        long id = IdUtils.generateArtificialId();
        try {
            JSONObject params = JSONObject.parseObject(node.getParameters());
            dppEtlNodeInstanceService.createNodeInstance(TaskInstance.builder()
                    .id(id)
                    .name(task.getName())
                    .taskCode(task.getCode())
                    .taskDefinitionVersion(instance.getTaskVersion())
                    .taskType(String.valueOf(params.get("taskType")))
                    .processInstanceId(instance.getId())
                    .processInstanceName(instance.getName())
                    .projectCode(task.getProjectCode())
                    .taskInstancePriority(Priority.MEDIUM)
                    .startTime(new Date())
                    .state(TaskExecutionStatus.RUNNING_EXECUTION)
                    .build());
        } catch (Exception e) {
            // Log node-instance persistence failures only; the main flow still handles the actual SQL result.
            log.error("Failed to create task instance: {}", e.getMessage());
        }
        log.info("Task instance creation completed");
        return id;
    }

    /**
     * Updates a data development task instance.
     *
     * @param id     Node instance ID
     * @param status Target execution status
     */
    private void updateNodeInstance(Long id, TaskExecutionStatus status) {
        log.info("Starting task instance update");
        Date date = new Date();
        try {
            dppEtlNodeInstanceService.updateNodeInstance(TaskInstance.builder()
                    .id(id)
                    .startTime(date)
                    .endTime(date)
                    .state(status)
                    .build());
        } catch (Exception e) {
            // A status-update error must not replace the actual SQL result, so log it only on the server.
            log.error("Failed to update task instance: {}", e.getMessage());
        }
        log.info("Task instance update completed");
    }

    /**
     * Builds the data development task run-lock key.
     *
     * @param taskId Task ID
     * @return Redis lock key
     */
    private String buildDataDevelopmentRunLockKey(Long taskId) {
        return "dpp:data-development:run:" + taskId;
    }

    /**
     * Acquires the run lock.
     *
     * @param redisKey Redis lock key
     * @return true when acquired; false when another task instance is already running
     */
    private boolean acquireDataDevelopmentRunLock(String redisKey) {
        String status = redisService.get(redisKey);
        // A Redis status of 1 means the task is still running, so reject this start attempt.
        if (StringUtils.isNotEmpty(status) && "1".equals(status)) {
            return false;
        }
        // Expire the lock after 12 hours to avoid a permanent lock after an abnormal exit.
        redisService.set(redisKey, "1", 60 * 60 * 12);
        return true;
    }

    /**
     * Creates a data development task instance.
     *
     * @param task Data development task details
     * @return Persisted task instance
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public DppEtlTaskInstanceDO createDataDevelopmentTaskInstance(DppEtlTaskDO task) {
        // Use the DolphinScheduler-compatible status code 1 for a running task.
        DppEtlTaskInstanceDO instance = DppEtlTaskInstanceDO.builder()
                // Local JDBC execution has no DolphinScheduler processInstanceId, and the DM table requires a non-null ID, so the application generates the instance primary key.
                .id(IdUtils.generateArtificialId())
                .catId(task.getCatId())
                .catCode(task.getCatCode())
                .taskType(task.getType())
                .name(task.getName())
                .taskId(task.getId())
                .taskCode(task.getCode())
                .taskVersion(task.getVersion() == null ? null : task.getVersion().intValue())
                .personCharge(task.getPersonCharge())
                .contactNumber(task.getContactNumber())
                .projectId(task.getProjectId())
                .projectCode(task.getProjectCode())
                .scheduleTime(new Date())
                .startTime(new Date())
                .runTimes(1)
                .commandType(String.valueOf(CommandType.START_PROCESS.getCode()))
                .subTaskFlag("0")
                .status(String.valueOf(WorkflowExecutionStatus.RUNNING_EXECUTION.getCode()))
                .taskScheduler(task.getScheduler())
                .taskActuator(task.getActuator())
                .quartzId(task.getQuartzId())
                .validFlag(Boolean.TRUE)
                .delFlag(Boolean.FALSE)
                .dsId(task.getDsId())
                .build();
        dppEtlTaskInstanceService.save(instance);
        return instance;
    }

    /**
     * Filters JDBC-executable nodes from the task node list.
     *
     * @param task Data development task details
     * @return List of SQL-development and stored-procedure-development nodes
     */
    private List<DppEtlNodeRespVO> getDataDevelopmentSqlNodes(DppEtlTaskDO task) {
        // Load the saved node configuration, including DataX parameters such as the data source, table name, and fields.
        List<DppEtlNodeRespVO> nodeList = iDppEtlNodeService.listNodeByTaskId(task.getId());
        // Return an empty list when no nodes exist; the caller converts it to a business exception.
        if (CollectionUtils.isEmpty(nodeList)) {
            return Collections.emptyList();
        }
        return nodeList.stream()
                .filter(node -> {
                    if (node == null) {
                        return false;
                    }
                    Map<String, Object> stringObjectMap = JSONUtils.convertTaskDefinitionJsonMap(node.getParameters());
                    String componentType = stringObjectMap.get("componentType").toString();
                    return StringUtils.equals(TaskComponentTypeEnum.SQL_DEV.getCode(), componentType) || StringUtils.equals(TaskComponentTypeEnum.PROCEDURE_DEV.getCode(), componentType);
                })
                .collect(Collectors.toList());
    }

    /**
     * Executes a single data development JDBC node.
     *
     * @param node    Node configuration
     * @param taskLog Task log
     * @return JDBC execution result
     * @throws Exception Thrown on failure so the caller can update the task instance status
     */
    private JdbcExecuteResult executeDataDevelopmentNodeSql(DppEtlNodeRespVO node, StringBuilder taskLog) throws Exception {
        // Node parameters are JSON saved by the frontend component and include the SQL, SQL type, and data source configuration.
        JSONObject params = JSONObject.parseObject(node.getParameters());
        // A null JSON result means the node has no valid execution parameters, so a database connection cannot be created.
        if (params == null) {
            throw new ServiceException("dpp.error.data.development.node.parameters.missing",
                    "Node parameters are missing: {0}", node.getName());
        }

        // SQL-development nodes use the sql field; stored-procedure nodes use the method field.
        String sql = getNodeSql(node, params);
        // The SQL type selects executeQuery for queries and the generic execute method for non-queries and stored procedures.
        String sqlType = getNodeSqlType(node, params);
        // segm is the statement delimiter configured by the frontend; when absent, use the default SQL script splitting logic.
        String sqlSegmentDelimiter = getNodeSqlSegmentDelimiter(params);
        // Convert the node data source JSON to the shared DbQueryProperty and reuse the existing JDBC URL generation logic.
        DbQueryProperty dbQueryProperty = buildNodeDbQueryProperty(params);

        LogUtils.appendLocalLogLine(taskLog, "Initialize sql task parameter " + JSONUtils.formatJson(params.toJSONString()));
        LogUtils.appendLocalLogLine(taskLog, "Database type: " + dbQueryProperty.getDbType());
        LogUtils.appendLocalLogLine(taskLog, "SQL type: " + sqlType);
        // Log the delimiter only when explicitly configured so complex procedure-script splitting can be verified.
        if (StringUtils.isNotEmpty(sqlSegmentDelimiter)) {
            LogUtils.appendLocalLogLine(taskLog, "SQL segment delimiter: " + sqlSegmentDelimiter);
        }
        LogUtils.appendLocalLogLine(taskLog, "Full sql parameters: " + sql);
        LogUtils.appendLocalLogLine(taskLog, "Prepare to create JDBC connection");

        Class.forName(getDriverClassName(dbQueryProperty.getDbType()));

        Long failRetryInterval = params.getLong("failRetryInterval"); // Failure retry interval in minutes
        Long failRetryTimes = params.getLong("failRetryTimes"); // Number of retries after failure
        Long delayTime = params.getLong("delayTime"); // Execution delay in minutes
        SqlExecutionTiming timing = buildSqlExecutionTiming(failRetryTimes, failRetryInterval, delayTime);

        LogUtils.appendLocalLogLine(taskLog, String.format("SQL execution timing: delay=%d minutes, retryTimes=%d, retryInterval=%d minutes",
                normalizeNonNegative(delayTime), normalizeNonNegative(failRetryTimes), normalizeNonNegative(failRetryInterval)));
        sleepBeforeSqlExecution(timing.getDelayMillis(), taskLog, "Delay before SQL execution");

        Exception lastException = null;
        // maxAttempts includes the initial attempt, so failRetryTimes=0 executes the loop only once.
        for (int attempt = 1; attempt <= timing.getMaxAttempts(); attempt++) {
            LogUtils.appendLocalLogLine(taskLog, String.format("Start SQL execution attempt %d/%d", attempt, timing.getMaxAttempts()));
            try {

                try (Connection connection = DriverManager.getConnection(
                        dbQueryProperty.trainToJdbcUrl(),
                        dbQueryProperty.getUsername(),
                        dbQueryProperty.getPassword())) {
                    connection.setAutoCommit(false);
                    try {
                        // pre, main, and post SQL share one connection and transaction and are committed only after all stages succeed.
                        JdbcExecuteResult result = executeJdbcSql(connection, sql.trim(), dbQueryProperty.getDbType(), sqlType,
                                sqlSegmentDelimiter, params, taskLog);
                        connection.commit();

                        // Log recovery information only after a successful retry; do not duplicate it for an initial success.
                        if (attempt > 1) {
                            LogUtils.appendLocalLogLine(taskLog, String.format("SQL execution retry succeeded on attempt %d/%d", attempt, timing.getMaxAttempts()));
                        }
                        return result;
                    } catch (Exception e) {
                        // Roll back all uncommitted SQL on the current connection when any stage fails.
                        connection.rollback();
                        LogUtils.appendLocalLogLine(taskLog, "SQL execution failed, transaction rolled back: "
                                + JSONUtils.formatJson(JSONUtils.toJson(e.getMessage())));
                        throw e;
                    }
                }
            } catch (Exception e) {
                log.error("Execute SQL failed", e);
                lastException = e;
                // Throw immediately after the maximum number of attempts instead of entering another wait cycle.
                if (attempt >= timing.getMaxAttempts()) {
                    LogUtils.appendLocalLogLine(taskLog, String.format("SQL execution failed after %d attempt(s)", timing.getMaxAttempts()));
                    throw e;
                }
                LogUtils.appendLocalLogLine(taskLog, String.format("SQL execution attempt %d/%d failed, will retry after %d minutes: %s",
                        attempt, timing.getMaxAttempts(), TimeUnit.MILLISECONDS.toMinutes(timing.getRetryIntervalMillis()),
                        JSONUtils.formatJson(JSONUtils.toJson(e.getMessage()))));
                sleepBeforeSqlExecution(timing.getRetryIntervalMillis(), taskLog, "Wait before SQL retry");
            }
        }
        // The final attempt should throw on failure; retain the last exception here as a defensive fallback.
        if (lastException != null) {
            throw lastException;
        }
        throw new ServiceException("dpp.error.scheduler.execute", "SQL execution failed.");
    }

    /**
     * Builds SQL delay and retry timing parameters from the node configuration.
     *
     * @param failRetryTimes    Number of retries after failure
     * @param failRetryInterval Retry interval in minutes
     * @param delayTime         Delay before the initial execution in minutes
     * @return Normalized execution timing configuration
     */
    static SqlExecutionTiming buildSqlExecutionTiming(Long failRetryTimes, Long failRetryInterval, Long delayTime) {
        long retryTimes = normalizeNonNegative(failRetryTimes);
        long maxAttempts = retryTimes + 1;
        // Prevent extreme configuration values from overflowing during int conversion.
        if (maxAttempts > Integer.MAX_VALUE) {
            maxAttempts = Integer.MAX_VALUE;
        }
        return new SqlExecutionTiming(
                (int) maxAttempts,
                TimeUnit.MINUTES.toMillis(normalizeNonNegative(failRetryInterval)),
                TimeUnit.MINUTES.toMillis(normalizeNonNegative(delayTime))
        );
    }

    /**
     * Converts null or negative values to zero.
     */
    private static long normalizeNonNegative(Long value) {
        // Delay, interval, and retry count cannot be negative.
        if (value == null || value < 0) {
            return 0L;
        }
        return value;
    }

    /**
     * Waits for the specified duration before the initial execution or a retry.
     *
     * @param millis  Wait duration in milliseconds
     * @param taskLog Task log
     * @param action  Description of the current wait action
     */
    private void sleepBeforeSqlExecution(long millis, StringBuilder taskLog, String action) throws InterruptedException {
        // Continue immediately when no wait duration is configured to avoid unnecessary thread sleep.
        if (millis <= 0) {
            return;
        }
        LogUtils.appendLocalLogLine(taskLog, String.format("%s, wait %d minutes", action, TimeUnit.MILLISECONDS.toMinutes(millis)));
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // Restore the interrupt flag so the parent scheduler can detect cancellation or interruption.
            Thread.currentThread().interrupt();
            LogUtils.appendLocalLogLine(taskLog, action + " interrupted");
            throw e;
        }
    }

    /**
     * Value object for SQL execution delay and retry settings.
     */
    static class SqlExecutionTiming {
        private final int maxAttempts;
        private final long retryIntervalMillis;
        private final long delayMillis;

        /**
         * Creates an execution timing configuration.
         */
        SqlExecutionTiming(int maxAttempts, long retryIntervalMillis, long delayMillis) {
            this.maxAttempts = maxAttempts;
            this.retryIntervalMillis = retryIntervalMillis;
            this.delayMillis = delayMillis;
        }

        /** Returns the maximum number of attempts, including the initial execution. */
        int getMaxAttempts() {
            return maxAttempts;
        }

        /** Returns the retry interval in milliseconds. */
        long getRetryIntervalMillis() {
            return retryIntervalMillis;
        }

        /** Returns the delay before the initial execution in milliseconds. */
        long getDelayMillis() {
            return delayMillis;
        }
    }

    /**
     * Parses the node SQL text.
     *
     * @param node   Node configuration
     * @param params Node parameter JSON
     * @return SQL text to execute
     */
    private String getNodeSql(DppEtlNodeRespVO node, JSONObject params) {
        String sql;
        // The stored-procedure component saves its content in the method field using the DolphinScheduler structure.
        if (StringUtils.equals(TaskComponentTypeEnum.PROCEDURE_DEV.getCode(), node.getComponentType())) {
            sql = params.getString("method");
        } else {
            // The SQL-development component saves its content in the sql field.
            sql = params.getString("sql");
        }
        // Empty SQL has no execution meaning, so return it as a configuration error.
        if (StringUtils.isEmpty(sql)) {
            throw new ServiceException("dpp.error.data.development.node.sql.missing",
                    "Node SQL is missing: {0}", node.getName());
        }
        return sql;
    }

    /**
     * Parses the SQL type.
     *
     * @param node   Node configuration
     * @param params Node parameter JSON
     * @return SQL type: 0 query, 1 non-query, 2 stored procedure
     */
    private String getNodeSqlType(DppEtlNodeRespVO node, JSONObject params) {
        String sqlType = params.getString("sqlType");
        // Prefer the frontend sqlType when present so logs reflect the user selection.
        if (StringUtils.isNotEmpty(sqlType)) {
            return sqlType;
        }
        // Default to the stored-procedure type when a stored-procedure component has no sqlType.
        if (StringUtils.equals(TaskComponentTypeEnum.PROCEDURE_DEV.getCode(), node.getComponentType())) {
            return "2";
        }
        // Default to a non-query script when an SQL-development component has no sqlType.
        return "1";
    }

    /**
     * Parses the SQL statement delimiter.
     *
     * @param params Node parameter JSON
     * @return Statement delimiter, or null when not configured
     */
    private String getNodeSqlSegmentDelimiter(JSONObject params) {
        return normalizeSqlSegmentDelimiter(params.getString("segm"));
    }

    /**
     * Parses the node data source configuration.
     *
     * @param params Node parameter JSON
     * @return JDBC query properties
     */
    private DbQueryProperty buildNodeDbQueryProperty(JSONObject params) {
        // datasource contains the connection information saved by the SQL or stored-procedure component.
        JSONObject datasource = params.getJSONObject("datasources");
        if (datasource == null) {
            throw new ServiceException("dpp.error.data.development.node.datasource.missing",
                    "Node data source configuration is missing.");
        }
        return new DbQueryProperty(
                datasource.getString("datasourceType"),
                datasource.getString("ip"),
                datasource.getLong("port"),
                datasource.get("datasourceConfig").toString()
        );
    }

    /**
     * Normalizes the database type.
     *
     * @param dbType Database type from the node
     * @return Database type code used by the project DbType enum
     */
    private String normalizeDbType(String dbType) {
        if (StringUtils.isEmpty(dbType)) {
            throw new ServiceException("dpp.error.data.development.database.type.missing",
                    "Database type is required.");
        }
        // MySQL components usually send mysql, while the project DbType uses MySql.
        if (StringUtils.equalsIgnoreCase(dbType, "mysql")) {
            return DbType.MYSQL.getDb();
        }
        // Use the Oracle12c dialect for Oracle to support pagination and common connection-generation logic.
        if (StringUtils.equalsIgnoreCase(dbType, "oracle") || StringUtils.equalsIgnoreCase(dbType, "oracle12c")) {
            return DbType.ORACLE_12C.getDb();
        }
        // DM accepts both dm and dm8 values from the frontend.
        if (StringUtils.equalsIgnoreCase(dbType, "dm8") || StringUtils.equalsIgnoreCase(dbType, "dm")) {
            return DbType.DM8.getDb();
        }
        // Kingbase accepts both king base and king base8 values from the frontend.
        if (StringUtils.equalsIgnoreCase(dbType, "kingbase") || StringUtils.equalsIgnoreCase(dbType, "kingbase8")) {
            return DbType.KINGBASE8.getDb();
        }
        // Return unknown types unchanged and let later driver matching throw the unsupported-type exception.
        return dbType;
    }

    /**
     * Decrypts the data source password.
     *
     * @param password Original password
     * @return Password usable for the JDBC connection
     */
    private String decryptPasswordIfNeeded(String password) {
        if (StringUtils.isEmpty(password)) {
            return password;
        }
        try {
            // Support AES-encrypted passwords stored by the platform.
            return AesEncryptUtil.desEncrypt(password).trim();
        } catch (Exception e) {
            // If decryption fails, the value may already be plaintext, so return it unchanged.
            return password;
        }
    }

    /**
     * Returns the JDBC driver class name for the database type.
     *
     * @param dbType Normalized database type
     * @return JDBC driver class name
     */
    private String getDriverClassName(String dbType) {
        if (StringUtils.equals(DbType.MYSQL.getDb(), dbType)) {
            return "com.mysql.cj.jdbc.Driver";
        }
        if (StringUtils.equals(DbType.ORACLE.getDb(), dbType) || StringUtils.equals(DbType.ORACLE_12C.getDb(), dbType)) {
            return "oracle.jdbc.OracleDriver";
        }
        if (StringUtils.equals(DbType.DM8.getDb(), dbType)) {
            return "dm.jdbc.driver.DmDriver";
        }
        if (StringUtils.equals(DbType.KINGBASE8.getDb(), dbType)) {
            return "com.kingbase8.Driver";
        }
        throw new ServiceException("dpp.error.data.development.database.type.unsupported",
                "Unsupported database type: {0}", dbType);
    }

    /**
     * Executes an SQL script.
     *
     * @param connection JDBC connection
     * @param sql        SQL script text
     * @return Execution result
     * @throws SQLException SQL execution exception
     */
    private JdbcExecuteResult executeJdbcSql(Connection connection, String sql, String dbType, String sqlType,
                                              String sqlSegmentDelimiter, JSONObject params,
                                              StringBuilder taskLog) throws SQLException {
        int updateCount = 0;
        int resultCount = 0;

        // localParams is compatible with the DolphinScheduler SQL parameter structure and replaces ${name} and !{name} placeholders.
        List<JSONObject> localParams = Optional.ofNullable(params.getJSONArray("localParams"))
                .map(array -> array.toJavaList(JSONObject.class)).orElse(Collections.emptyList());
        // limit controls the maximum rows returned by JDBC; displayRows controls only the rows written to the task log.
        int queryLimit = params.getIntValue("limit", 10000);
        int displayRows = params.getIntValue("displayRows", 10);

        // Treat pre- and post-statements as non-query scripts that share the current connection and transaction with the main SQL.
        List<String> preStatements = getStringList(params, "preStatements");
        List<String> postStatements = getStringList(params, "postStatements");

        // Execute pre-SQL first; any failure throws and causes the caller to roll back the entire node transaction.
        updateCount += executeNonQueryStatements(connection, preStatements, dbType, sqlSegmentDelimiter,
                localParams, queryLimit, "pre", taskLog);

        // Split the main SQL by the custom delimiter or database dialect, then bind parameters and execute each statement.
        List<String> mainStatements = splitJdbcSqlScript(sql, dbType, sqlSegmentDelimiter);
        for (String item : mainStatements) {
            SqlBind sqlBind = buildSqlBind(item, localParams);
            // sqlType=0 is a query; call executeQuery explicitly and count and display the result set.
            if (StringUtils.equals("0", sqlType)) {
                resultCount += executeQueryStatement(connection, sqlBind, queryLimit, displayRows, taskLog);
            // sqlType=1 or 2 may contain DDL, DML, procedure definitions, or procedure calls; use execute to support different result types.
            } else if (StringUtils.equals("1", sqlType) || StringUtils.equals("2", sqlType)) {
                JdbcExecuteResult result = executeStatement(connection, sqlBind, queryLimit, taskLog);
                updateCount += result.getUpdateCount();
                resultCount += result.getResultCount();
            } else {
                throw new SQLException(MessageUtils.messageWithFallback(
                        "dpp.error.data.development.sql.type.unsupported",
                        "Unsupported sqlType: {0}. Valid values are 0 (query), 1 (non-query), and 2 (stored procedure).",
                        sqlType));
            }
        }

        // Execute post-SQL after all main SQL succeeds to preserve DolphinScheduler pre-main-post ordering.
        updateCount += executeNonQueryStatements(connection, postStatements, dbType, sqlSegmentDelimiter,
                localParams, queryLimit, "post", taskLog);
        return new JdbcExecuteResult(updateCount, resultCount);
    }

    /**
     * Executes pre- or post-non-query scripts.
     *
     * @param connection          Current node JDBC connection
     * @param scripts             Scripts to execute
     * @param dbType              Database type
     * @param sqlSegmentDelimiter Custom delimiter
     * @param localParams         Local SQL parameters
     * @param queryLimit          Maximum rows returned by JDBC
     * @param handlerType         Log stage name: pre or post
     * @param taskLog             Task log
     * @return Cumulative affected-row count
     * @throws SQLException SQL execution exception
     */
    private int executeNonQueryStatements(Connection connection, List<String> scripts, String dbType,
                                            String sqlSegmentDelimiter, List<JSONObject> localParams,
                                            int queryLimit, String handlerType,
                                            StringBuilder taskLog) throws SQLException {
        int updateCount = 0;
        for (String script : scripts) {
            // A single pre or post item may contain multiple SQL statements, so it must still pass through the common splitter.
            for (String statement : splitJdbcSqlScript(script, dbType, sqlSegmentDelimiter)) {
                LogUtils.appendLocalLogLine(taskLog, "Execute " + handlerType + " sql: " + statement);
                JdbcExecuteResult result = executeStatement(connection, buildSqlBind(statement, localParams), queryLimit, taskLog);
                updateCount += result.getUpdateCount();
            }
        }
        return updateCount;
    }

    /**
     * Executes one SQL statement with the generic JDBC execute method and consumes all result sets and update counts.
     *
     * @return Affected-row and result-row counts for the current SQL statement
     */
    private JdbcExecuteResult executeStatement(Connection connection, SqlBind sqlBind, int queryLimit,
                                                StringBuilder taskLog) throws SQLException {
        int updateCount = 0;
        int resultCount = 0;
        LogUtils.appendLocalLogLine(taskLog, "Execute sql: " + sqlBind.sql);
        try (PreparedStatement statement = prepareStatement(connection, sqlBind, queryLimit)) {
            boolean hasResultSet = statement.execute();
            while (true) {
                // execute returning true means the current result is a ResultSet; it does not indicate success or failure.
                if (hasResultSet) {
                    try (ResultSet resultSet = statement.getResultSet()) {
                        resultCount += countResultSetRows(resultSet, 0, taskLog);
                    }
                } else {
                    int count = statement.getUpdateCount();
                    // An update count of -1 means the current SQL statement has no more results.
                    if (count == -1) {
                        break;
                    }
                    if (count > 0) {
                        updateCount += count;
                    }
                }
                // A stored procedure may return multiple result sets and update counts, so consume them until completion.
                hasResultSet = statement.getMoreResults();
            }
        }
        return new JdbcExecuteResult(updateCount, resultCount);
    }

    /**
     * Executes query-only SQL, counts all result rows, and logs the configured number of rows.
     */
    private int executeQueryStatement(Connection connection, SqlBind sqlBind, int queryLimit, int displayRows,
                                      StringBuilder taskLog) throws SQLException {
        LogUtils.appendLocalLogLine(taskLog, "Execute query sql: " + sqlBind.sql);
        try (PreparedStatement statement = prepareStatement(connection, sqlBind, queryLimit);
             ResultSet resultSet = statement.executeQuery()) {
            return countResultSetRows(resultSet, displayRows, taskLog);
        }
    }

    /**
     * Creates a PreparedStatement, sets the maximum returned rows, and binds parameters in order.
     */
    private PreparedStatement prepareStatement(Connection connection, SqlBind sqlBind, int queryLimit) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sqlBind.sql);
        // Use the DolphinScheduler-compatible default limit of 10000 when the value is absent or non-positive.
        statement.setMaxRows(queryLimit <= 0 ? 10000 : queryLimit);
        for (int i = 0; i < sqlBind.values.size(); i++) {
            statement.setObject(i + 1, sqlBind.values.get(i));
        }
        return statement;
    }

    /**
     * Counts result-set rows.
     *
     * @param resultSet JDBC result set
     * @param displayRows Maximum rows written to the log; 0 counts without displaying
     * @param taskLog     Task log
     * @return Result-row count
     * @throws SQLException Result-set read exception
     */
    private int countResultSetRows(ResultSet resultSet, int displayRows, StringBuilder taskLog) throws SQLException {
        if (resultSet == null) {
            return 0;
        }
        int count = 0;
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        while (resultSet.next()) {
            // After displayRows is reached, stop building log objects but continue consuming and counting the result set.
            Map<String, Object> row = count < displayRows ? new LinkedHashMap<>() : null;
            for (int i = 1; i <= columnCount; i++) {
                Object value = resultSet.getObject(i);
                if (row != null) {
                    row.put(metaData.getColumnLabel(i), value);
                }
            }
            count++;
            if (row != null) {
                LogUtils.appendLocalLogLine(taskLog, "Query result row " + count + ": " + JSONUtils.toJson(row));
            }
        }
        return count;
    }

    /**
     * Reads a string array from node JSON parameters and returns an empty collection when absent.
     */
    private List<String> getStringList(JSONObject params, String key) {
        // Legacy tasks may lack preStatements or postStatements, so preserve backward compatibility.
        if (params.getJSONArray(key) == null) {
            return Collections.emptyList();
        }
        return params.getJSONArray(key).toJavaList(String.class).stream()
                .filter(StringUtils::isNotEmpty).collect(Collectors.toList());
    }

    /**
     * Converts node SQL to prepared SQL and its corresponding parameter values.
     * <p>
     * !{name} directly substitutes the raw value for structural positions such as table and column names where JDBC placeholders cannot be used;
     * ${name} becomes a question mark and is bound through PreparedStatement for ordinary condition values.
     */
    private SqlBind buildSqlBind(String sql, List<JSONObject> localParams) throws SQLException {
        Map<String, Object> values = new HashMap<>();
        for (JSONObject param : localParams) {
            values.put(param.getString("prop"), param.get("value"));
        }

        // Raw-value parameters are not prepared; callers must use them only for trusted structural SQL fragments.
        Matcher rawMatcher = Pattern.compile("['\"]*!\\{(.*?)}['\"]*").matcher(sql);
        StringBuffer rawSql = new StringBuffer();
        while (rawMatcher.find()) {
            String name = rawMatcher.group(1);
            if (!values.containsKey(name)) {
                throw new SQLException(MessageUtils.messageWithFallback(
                        "dpp.error.data.development.sql.parameter.missing",
                        "SQL parameter does not exist: {0}", name));
            }
            rawMatcher.appendReplacement(rawSql, Matcher.quoteReplacement(String.valueOf(values.get(name))));
        }
        rawMatcher.appendTail(rawSql);

        // Convert ordinary parameters to JDBC question-mark placeholders to avoid direct value concatenation.
        Matcher bindMatcher = Pattern.compile("\\$\\{(.*?)}").matcher(rawSql.toString());
        StringBuffer bindSql = new StringBuffer();
        List<Object> bindValues = new ArrayList<>();
        while (bindMatcher.find()) {
            String name = bindMatcher.group(1);
            if (!values.containsKey(name)) {
                throw new SQLException(MessageUtils.messageWithFallback(
                        "dpp.error.data.development.sql.parameter.missing",
                        "SQL parameter does not exist: {0}", name));
            }
            bindValues.add(values.get(name));
            bindMatcher.appendReplacement(bindSql, "?");
        }
        bindMatcher.appendTail(bindSql);
        return new SqlBind(bindSql.toString(), bindValues);
    }

    private static class SqlBind {
        /** SQL after substitution, ready for PreparedStatement. */
        private final String sql;
        /** Bound values ordered by question-mark position. */
        private final List<Object> values;

        private SqlBind(String sql, List<Object> values) {
            this.sql = sql;
            this.values = values;
        }
    }

    /**
     * Splits an SQL script by database type and an optional custom statement delimiter.
     * <p>
     * The custom delimiter comes from the segm node parameter; when absent, retain the database-specific splitting logic.
     *
     * @param sql                 SQL script text
     * @param dbType              Internal project database type code
     * @param sqlSegmentDelimiter Custom statement delimiter
     * @return List of SQL statements executable individually through JDBC
     */
    private List<String> splitJdbcSqlScript(String sql, String dbType, String sqlSegmentDelimiter) {
        String segmentDelimiter = normalizeSqlSegmentDelimiter(sqlSegmentDelimiter);
        // When segm is explicitly configured, split the original script first so Druid does not incorrectly split or remove semicolons inside procedure bodies.
        if (StringUtils.isNotEmpty(segmentDelimiter)) {
            return Arrays.stream(sql.split(Pattern.quote(segmentDelimiter)))
                    .map(String::trim)
                    .filter(StringUtils::isNotEmpty)
                    .collect(Collectors.toList());
        }
        // When no custom delimiter is configured, select the Druid splitter for the actual database dialect.
        if (StringUtils.equals(DbType.MYSQL.getDb(), dbType)) {
            String cleanSQL = SQLParserUtils.removeComment(sql, com.alibaba.druid.DbType.mysql);
            return SQLParserUtils.split(cleanSQL, com.alibaba.druid.DbType.mysql);
        }
        if (StringUtils.equals(DbType.ORACLE.getDb(), dbType)
                || StringUtils.equals(DbType.ORACLE_12C.getDb(), dbType)) {
            // Oracle PL/SQL blocks contain internal semicolons; use the Oracle parser to keep each block intact.
            if (sql.toUpperCase().contains("BEGIN") && sql.toUpperCase().contains("END")) {
                return new OracleStatementParser(sql).parseStatementList().stream().map(SQLStatement::toString)
                        .collect(Collectors.toList());
            }
            return SQLParserUtils.splitAndRemoveComment(sql, com.alibaba.druid.DbType.oracle);
        }
        if (StringUtils.equals(DbType.DM8.getDb(), dbType)) {
            String cleanSQL = SQLParserUtils.removeComment(sql, com.alibaba.druid.DbType.dm);
            return SQLParserUtils.split(cleanSQL, com.alibaba.druid.DbType.dm);
        }
        if (StringUtils.equals(DbType.KINGBASE8.getDb(), dbType)) {
            String cleanSQL = SQLParserUtils.removeComment(sql, com.alibaba.druid.DbType.kingbase);
            return SQLParserUtils.splitAndRemoveComment(cleanSQL, com.alibaba.druid.DbType.kingbase);
        }
        // Use the generic Druid dialect as a fallback for unrecognized databases.
        String cleanSQL = SQLParserUtils.removeComment(sql, com.alibaba.druid.DbType.other);
        return SQLParserUtils.split(cleanSQL, com.alibaba.druid.DbType.other);
    }

    /**
     * Normalizes the SQL statement delimiter.
     *
     * @param delimiter Original statement delimiter
     * @return Statement delimiter with surrounding whitespace removed
     */
    private String normalizeSqlSegmentDelimiter(String delimiter) {
        if (StringUtils.isEmpty(delimiter)) {
            return null;
        }
        String normalizedDelimiter = delimiter.trim();
        if (StringUtils.isEmpty(normalizedDelimiter)) {
            return null;
        }
        return normalizedDelimiter;
    }

    /**
     * Marks the task instance as successful.
     *
     * @param instance Task instance
     */
    private void markDataDevelopmentSuccess(DppEtlTaskInstanceDO instance) {
        // After all SQL nodes succeed, mark the task instance successful and record its end time.
        instance.setStatus(String.valueOf(WorkflowExecutionStatus.SUCCESS.getCode()));
        instance.setEndTime(new Date());
        dppEtlTaskInstanceService.updateById(instance);
    }

    /**
     * Marks the task instance as failed.
     *
     * @param instance Task instance
     * @param e        Failure exception
     */
    private void markDataDevelopmentFail(DppEtlTaskInstanceDO instance, Exception e) {
        // Preserve the original exception message so the task instance page can display the failure reason.
        instance.setStatus(String.valueOf(WorkflowExecutionStatus.FAILURE.getCode()));
        instance.setEndTime(new Date());
        instance.setRemark(e.getMessage());
        dppEtlTaskInstanceService.updateById(instance);
    }

    /**
     * Calculates task duration.
     *
     * @param startTime Task instance
     * @return Duration in seconds
     */
    private long calcDurationSeconds(Date startTime) {
        // The log needs second-level duration only, so truncate the millisecond difference.
        Date endTime = new Date();
        return (endTime.getTime() - startTime.getTime()) / 1000;
    }

    /**
     * Writes the task instance log safely.
     *
     * @param nodeInstanceId Node instance ID
     * @param instanceId Task instance ID
     * @param task       Task details
     * @param msg        Log content
     */
    private void safeDataDevelopmentLog(Long nodeInstanceId, Long instanceId, DppEtlTaskDO task, String msg) {
        try {
            // The run method passes the complete log once at the end; save it unchanged to avoid duplicate appends or prefixes.
            dppEtlNodeInstanceLogService.save(DppEtlNodeInstanceLogDO.builder()
                    .nodeId(task.getId())
                    .nodeCode(task.getCode())
                    .nodeInstanceId(nodeInstanceId)
                    .taskInstanceId(instanceId)
                    .tm(new Date())
                    .taskType(task.getType())
                    .logContent(msg)
                    .validFlag(Boolean.TRUE)
                    .delFlag(Boolean.FALSE)
                    .build());
        } catch (Exception e) {
            // A log-write failure must not mask the actual SQL exception, so record it only in the local error log.
            log.error("Failed to write the data development task instance log: instanceId={}, msg={}", instanceId, msg, e);
        }
    }
}
