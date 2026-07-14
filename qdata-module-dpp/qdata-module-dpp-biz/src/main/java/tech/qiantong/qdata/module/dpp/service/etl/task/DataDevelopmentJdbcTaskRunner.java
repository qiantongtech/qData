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


/**
 * Handle JDBC SQL execution.
 * <p>
 * Handle JDBC SQL execution.
 * Handle task-related data and operations.
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
     * Handle JDBC SQL execution.
     *
     * @param task parameter value
     * @param instance parameter value
     * @param taskLog parameter value
     */
    @Transactional
    public void run(DppEtlTaskDO task, DppEtlTaskInstanceDO instance, StringBuilder taskLog) {
        // Handle task-related data and operations.
        Date startTime = new Date();
        String redisKey = buildDataDevelopmentRunLockKey(task.getId());
        redisService.delete(redisKey);
        // Handle task-related data and operations.
        if (!acquireDataDevelopmentRunLock(redisKey)) {
            throw new RuntimeException("The historical task has not been fully executed. Please try again later");
        }

        int totalUpdateCount = 0;
        int totalResultCount = 0;
        LogUtils.appendLocalLogLine(taskLog, "***********************************************************************************************");
        LogUtils.appendLocalLogLine(taskLog, "********************************* Load task instance plugin *********************************");
        LogUtils.appendLocalLogLine(taskLog, "***********************************************************************************************");
        LogUtils.appendLocalLogLine(taskLog, "Send task status RUNNING_EXECUTION");
        LogUtils.appendLocalLogLine(taskLog, "Create TaskChannel: qData DataDevelopmentJdbcTaskRunner successfully");

        // Handle JDBC SQL execution.
        List<DppEtlNodeRespVO> sqlNodes = getDataDevelopmentSqlNodes(task);
        // Handle JDBC SQL execution.
        if (CollectionUtils.isEmpty(sqlNodes)) {
            throw new ServiceException("The SQL or stored procedure node was not found in the data development task");
        }

        LogUtils.appendLocalLogLine(taskLog, "********************************* Execute task instance *************************************");
        // Handle node-related data and operations.
        for (DppEtlNodeRespVO node : sqlNodes) {
            // Handle node-related data and operations.
            Long nodeInstanceId = createNodeInstance(task, node, instance);
            try {
                LogUtils.appendLocalLogLine(taskLog, "Start executing node: " + node.getName());

                JdbcExecuteResult result = executeDataDevelopmentNodeSql(node, taskLog);
                totalUpdateCount += result.getUpdateCount();
                totalResultCount += result.getResultCount();
                LogUtils.appendLocalLogLine(taskLog, String.format("Node execution completed: %s, affected rows=%d, result rows=%d",
                        node.getName(), result.getUpdateCount(), result.getResultCount()));
                // Handle node-related data and operations.
                updateNodeInstance(nodeInstanceId, TaskExecutionStatus.SUCCESS);
                // Handle task-related data and operations.
                markDataDevelopmentSuccess(instance);
                LogUtils.appendLocalLogLine(taskLog, String.format("Data development task executed successfully: node count=%d, affected rows=%d, result rows=%d",
                        sqlNodes.size(), totalUpdateCount, totalResultCount));
            } catch (Exception e) {
                // Handle JDBC SQL execution.
                markDataDevelopmentFail(instance, e);
                LogUtils.appendLocalLogLine(taskLog, "Data development task execution failed: " + JSONUtils.formatJson(JSONUtils.toJson(e.getMessage())));
            } finally {
                // Handle execution logging.
                redisService.delete(redisKey);
                // Implementation details.
                if (instance.getEndTime() == null) {
                    instance.setEndTime(new Date());
                }
                LogUtils.appendLocalLogLine(taskLog, "Data development task execution finished, duration: " + calcDurationSeconds(startTime) + " seconds");
                safeDataDevelopmentLog(nodeInstanceId, instance.getId(), task, taskLog.toString());
            }
        }
    }

    /**
     * Handle node-related data and operations.
     *
     * @param task parameter value
     * @param node parameter value
     * @param instance parameter value
     * @return the operation result
     */
    private Long createNodeInstance(DppEtlTaskDO task, DppEtlNodeRespVO node, DppEtlTaskInstanceDO instance) {
        log.info("Starting task instance creation message processing >>>>>>>>>>>>>>>>>>>>>>>>>>>");
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
            // Handle JDBC SQL execution.
            log.error("Failed to create the task instance: {}", e.getMessage());
        }
        log.info("Finished task instance creation message processing >>>>>>>>>>>>>>>>>>>>>>>>>>>");
        return id;
    }

    /**
     * Handle task-related data and operations.
     *
     * @param id parameter value
     * @param status parameter value
     */
    private void updateNodeInstance(Long id, TaskExecutionStatus status) {
        log.info("Starting task instance update message processing >>>>>>>>>>>>>>>>>>>>>>>>>>>");
        Date date = new Date();
        try {
            dppEtlNodeInstanceService.updateNodeInstance(TaskInstance.builder()
                    .id(id)
                    .startTime(date)
                    .endTime(date)
                    .state(status)
                    .build());
        } catch (Exception e) {
            // Handle JDBC SQL execution.
            log.error("Exception in updating task instance:{}", e.getMessage());
        }
        log.info("Finished task instance update message processing >>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    /**
     * Handle task-related data and operations.
     *
     * @param taskId parameter value
     * @return the operation result
     */
    private String buildDataDevelopmentRunLockKey(Long taskId) {
        return "dpp:data-development:run:" + taskId;
    }

    /**
     * Retrieve the required data.
     *
     * @param redisKey parameter value
     * @return the operation result
     */
    private boolean acquireDataDevelopmentRunLock(String redisKey) {
        String status = redisService.get(redisKey);
        // Handle Redis state for this operation.
        if (StringUtils.isNotEmpty(status) && "1".equals(status)) {
            return false;
        }
        // Implementation details.
        redisService.set(redisKey, "1", 60 * 60 * 12);
        return true;
    }

    /**
     * Handle task-related data and operations.
     *
     * @param task parameter value
     * @return the operation result
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public DppEtlTaskInstanceDO createDataDevelopmentTaskInstance(DppEtlTaskDO task) {
        // Handle DolphinScheduler operations.
        DppEtlTaskInstanceDO instance = DppEtlTaskInstanceDO.builder()
                // Handle JDBC SQL execution.
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
     * Handle JDBC SQL execution.
     *
     * @param task parameter value
     * @return the operation result
     */
    private List<DppEtlNodeRespVO> getDataDevelopmentSqlNodes(DppEtlTaskDO task) {
        // Handle DataX task configuration and execution.
        List<DppEtlNodeRespVO> nodeList = iDppEtlNodeService.listNodeByTaskId(task.getId());
        // Handle node-related data and operations.
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
     * Handle JDBC SQL execution.
     *
     * @param node parameter value
     * @param taskLog parameter value
     * @return the operation result
     * @throws Exception when the operation fails
     */
    private JdbcExecuteResult executeDataDevelopmentNodeSql(DppEtlNodeRespVO node, StringBuilder taskLog) throws Exception {
        // Handle JDBC SQL execution.
        JSONObject params = JSONObject.parseObject(node.getParameters());
        // Handle JSON data for this operation.
        if (params == null) {
            throw new ServiceException("The node parameter is empty：" + node.getName());
        }

        // Handle JDBC SQL execution.
        String sql = getNodeSql(node, params);
        // Handle JDBC SQL execution.
        String sqlType = getNodeSqlType(node, params);
        // Handle JDBC SQL execution.
        String sqlSegmentDelimiter = getNodeSqlSegmentDelimiter(params);
        // Handle JDBC SQL execution.
        DbQueryProperty dbQueryProperty = buildNodeDbQueryProperty(params);

        LogUtils.appendLocalLogLine(taskLog, "Initialize sql task parameter " + JSONUtils.formatJson(params.toJSONString()));
        LogUtils.appendLocalLogLine(taskLog, "Database type: " + dbQueryProperty.getDbType());
        LogUtils.appendLocalLogLine(taskLog, "SQL type: " + sqlType);
        // Implementation details.
        if (StringUtils.isNotEmpty(sqlSegmentDelimiter)) {
            LogUtils.appendLocalLogLine(taskLog, "SQL segment delimiter: " + sqlSegmentDelimiter);
        }
        LogUtils.appendLocalLogLine(taskLog, "Full sql parameters: " + sql);
        LogUtils.appendLocalLogLine(taskLog, "Prepare to create JDBC connection");

        Class.forName(getDriverClassName(dbQueryProperty.getDbType()));

        Long failRetryInterval = params.getLong("failRetryInterval"); // Implementation details.
        Long failRetryTimes = params.getLong("failRetryTimes"); // Implementation details.
        Long delayTime = params.getLong("delayTime");// Implementation details.
        SqlExecutionTiming timing = buildSqlExecutionTiming(failRetryTimes, failRetryInterval, delayTime);

        LogUtils.appendLocalLogLine(taskLog, String.format("SQL execution timing: delay=%d minutes, retryTimes=%d, retryInterval=%d minutes",
                normalizeNonNegative(delayTime), normalizeNonNegative(failRetryTimes), normalizeNonNegative(failRetryInterval)));
        sleepBeforeSqlExecution(timing.getDelayMillis(), taskLog, "Delay before SQL execution");

        Exception lastException = null;
        // Implementation details.
        for (int attempt = 1; attempt <= timing.getMaxAttempts(); attempt++) {
            LogUtils.appendLocalLogLine(taskLog, String.format("Start SQL execution attempt %d/%d", attempt, timing.getMaxAttempts()));
            try {

                try (Connection connection = DriverManager.getConnection(
                        dbQueryProperty.trainToJdbcUrl(),
                        dbQueryProperty.getUsername(),
                        dbQueryProperty.getPassword())) {
                    connection.setAutoCommit(false);
                    try {
                        // Handle JDBC SQL execution.
                        JdbcExecuteResult result = executeJdbcSql(connection, sql.trim(), dbQueryProperty.getDbType(), sqlType,
                                sqlSegmentDelimiter, params, taskLog);
                        connection.commit();

                        // Implementation details.
                        if (attempt > 1) {
                            LogUtils.appendLocalLogLine(taskLog, String.format("SQL execution retry succeeded on attempt %d/%d", attempt, timing.getMaxAttempts()));
                        }
                        return result;
                    } catch (Exception e) {
                        // Handle JDBC SQL execution.
                        connection.rollback();
                        LogUtils.appendLocalLogLine(taskLog, "SQL execution failed, transaction rolled back: "
                                + JSONUtils.formatJson(JSONUtils.toJson(e.getMessage())));
                        throw e;
                    }
                }
            } catch (Exception e) {
                log.error("Execute SQL failed", e);
                lastException = e;
                // Implementation details.
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
        // Implementation details.
        if (lastException != null) {
            throw lastException;
        }
        throw new ServiceException("dpp.error.scheduler.execute", "执行SQL失败！");
    }

    /**
     * Handle JDBC SQL execution.
     *
     * @param failRetryTimes parameter value
     * @param failRetryInterval parameter value
     * @param delayTime parameter value
     * @return the operation result
     */
    static SqlExecutionTiming buildSqlExecutionTiming(Long failRetryTimes, Long failRetryInterval, Long delayTime) {
        long retryTimes = normalizeNonNegative(failRetryTimes);
        long maxAttempts = retryTimes + 1;
        // Implementation details.
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
     * Implementation details.
     */
    private static long normalizeNonNegative(Long value) {
        // Implementation details.
        if (value == null || value < 0) {
            return 0L;
        }
        return value;
    }

    /**
     * Implementation details.
     *
     * @param millis parameter value
     * @param taskLog parameter value
     * @param action parameter value
     */
    private void sleepBeforeSqlExecution(long millis, StringBuilder taskLog, String action) throws InterruptedException {
        // Implementation details.
        if (millis <= 0) {
            return;
        }
        LogUtils.appendLocalLogLine(taskLog, String.format("%s, wait %d minutes", action, TimeUnit.MILLISECONDS.toMinutes(millis)));
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // Handle task-related data and operations.
            Thread.currentThread().interrupt();
            LogUtils.appendLocalLogLine(taskLog, action + " interrupted");
            throw e;
        }
    }

    /**
     * Handle JDBC SQL execution.
     */
    static class SqlExecutionTiming {
        private final int maxAttempts;
        private final long retryIntervalMillis;
        private final long delayMillis;

        /**
         * Create the required record.
         */
        SqlExecutionTiming(int maxAttempts, long retryIntervalMillis, long delayMillis) {
            this.maxAttempts = maxAttempts;
            this.retryIntervalMillis = retryIntervalMillis;
            this.delayMillis = delayMillis;
        }

        /** Return the operation result. */
        int getMaxAttempts() {
            return maxAttempts;
        }

        /** Return the operation result. */
        long getRetryIntervalMillis() {
            return retryIntervalMillis;
        }

        /** Return the operation result. */
        long getDelayMillis() {
            return delayMillis;
        }
    }

    /**
     * Handle JDBC SQL execution.
     *
     * @param node parameter value
     * @param params parameter value
     * @return the operation result
     */
    private String getNodeSql(DppEtlNodeRespVO node, JSONObject params) {
        String sql;
        // Handle DolphinScheduler operations.
        if (StringUtils.equals(TaskComponentTypeEnum.PROCEDURE_DEV.getCode(), node.getComponentType())) {
            sql = params.getString("method");
        } else {
            // Handle JDBC SQL execution.
            sql = params.getString("sql");
        }
        // Handle JDBC SQL execution.
        if (StringUtils.isEmpty(sql)) {
            throw new ServiceException("节点SQL为空：" + node.getName());
        }
        return sql;
    }

    /**
     * Handle JDBC SQL execution.
     *
     * @param node parameter value
     * @param params parameter value
     * @return the operation result
     */
    private String getNodeSqlType(DppEtlNodeRespVO node, JSONObject params) {
        String sqlType = params.getString("sqlType");
        // Handle execution logging.
        if (StringUtils.isNotEmpty(sqlType)) {
            return sqlType;
        }
        // Implementation details.
        if (StringUtils.equals(TaskComponentTypeEnum.PROCEDURE_DEV.getCode(), node.getComponentType())) {
            return "2";
        }
        // Handle JDBC SQL execution.
        return "1";
    }

    /**
     * Handle JDBC SQL execution.
     *
     * @param params parameter value
     * @return the operation result
     */
    private String getNodeSqlSegmentDelimiter(JSONObject params) {
        return normalizeSqlSegmentDelimiter(params.getString("segm"));
    }

    /**
     * Handle node-related data and operations.
     *
     * @param params parameter value
     * @return the operation result
     */
    private DbQueryProperty buildNodeDbQueryProperty(JSONObject params) {
        // Handle JDBC SQL execution.
        JSONObject datasource = params.getJSONObject("datasources");
        if (datasource == null) {
            throw new ServiceException("The node data source configuration is empty");
        }
        return new DbQueryProperty(
                datasource.getString("datasourceType"),
                datasource.getString("ip"),
                datasource.getLong("port"),
                datasource.get("datasourceConfig").toString()
        );
    }

    /**
     * Handle database and data source configuration.
     *
     * @param dbType parameter value
     * @return the operation result
     */
    private String normalizeDbType(String dbType) {
        if (StringUtils.isEmpty(dbType)) {
            throw new ServiceException("Database type cannot be empty");
        }
        // Handle JDBC SQL execution.
        if (StringUtils.equalsIgnoreCase(dbType, "mysql")) {
            return DbType.MYSQL.getDb();
        }
        // Maintain compatibility with existing data and configurations.
        if (StringUtils.equalsIgnoreCase(dbType, "oracle") || StringUtils.equalsIgnoreCase(dbType, "oracle12c")) {
            return DbType.ORACLE_12C.getDb();
        }
        // Maintain compatibility with existing data and configurations.
        if (StringUtils.equalsIgnoreCase(dbType, "dm8") || StringUtils.equalsIgnoreCase(dbType, "dm")) {
            return DbType.DM8.getDb();
        }
        // Maintain compatibility with existing data and configurations.
        if (StringUtils.equalsIgnoreCase(dbType, "kingbase") || StringUtils.equalsIgnoreCase(dbType, "kingbase8")) {
            return DbType.KINGBASE8.getDb();
        }
        // Return the operation result.
        return dbType;
    }

    /**
     * Handle database and data source configuration.
     *
     * @param password parameter value
     * @return the operation result
     */
    private String decryptPasswordIfNeeded(String password) {
        if (StringUtils.isEmpty(password)) {
            return password;
        }
        try {
            // Persist the related data.
            return AesEncryptUtil.desEncrypt(password).trim();
        } catch (Exception e) {
            // Return the operation result.
            return password;
        }
    }

    /**
     * Handle JDBC SQL execution.
     *
     * @param dbType parameter value
     * @return the operation result
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
        throw new ServiceException("Unsupported database type：" + dbType);
    }

    /**
     * Handle JDBC SQL execution.
     *
     * @param connection parameter value
     * @param sql parameter value
     * @return the operation result
     * @throws SQLException when the operation fails
     */
    private JdbcExecuteResult executeJdbcSql(Connection connection, String sql, String dbType, String sqlType,
                                              String sqlSegmentDelimiter, JSONObject params,
                                              StringBuilder taskLog) throws SQLException {
        int updateCount = 0;
        int resultCount = 0;

        // Handle JDBC SQL execution.
        List<JSONObject> localParams = Optional.ofNullable(params.getJSONArray("localParams"))
                .map(array -> array.toJavaList(JSONObject.class)).orElse(Collections.emptyList());
        // Handle JDBC SQL execution.
        int queryLimit = params.getIntValue("limit", 10000);
        int displayRows = params.getIntValue("displayRows", 10);

        // Handle JDBC SQL execution.
        List<String> preStatements = getStringList(params, "preStatements");
        List<String> postStatements = getStringList(params, "postStatements");

        // Handle JDBC SQL execution.
        updateCount += executeNonQueryStatements(connection, preStatements, dbType, sqlSegmentDelimiter,
                localParams, queryLimit, "pre", taskLog);

        // Handle JDBC SQL execution.
        List<String> mainStatements = splitJdbcSqlScript(sql, dbType, sqlSegmentDelimiter);
        for (String item : mainStatements) {
            SqlBind sqlBind = buildSqlBind(item, localParams);
            // Retrieve the required data.
            if (StringUtils.equals("0", sqlType)) {
                resultCount += executeQueryStatement(connection, sqlBind, queryLimit, displayRows, taskLog);
            // Maintain compatibility with existing data and configurations.
            } else if (StringUtils.equals("1", sqlType) || StringUtils.equals("2", sqlType)) {
                JdbcExecuteResult result = executeStatement(connection, sqlBind, queryLimit, taskLog);
                updateCount += result.getUpdateCount();
                resultCount += result.getResultCount();
            } else {
                throw new SQLException("Unsupported sqlType：" + sqlType + "，The valid values are 0 (query), 1 (non-query), and 2 (stored procedure)");
            }
        }

        // Handle JDBC SQL execution.
        updateCount += executeNonQueryStatements(connection, postStatements, dbType, sqlSegmentDelimiter,
                localParams, queryLimit, "post", taskLog);
        return new JdbcExecuteResult(updateCount, resultCount);
    }

    /**
     * Retrieve the required data.
     *
     * @param connection parameter value
     * @param scripts parameter value
     * @param dbType parameter value
     * @param sqlSegmentDelimiter parameter value
     * @param localParams parameter value
     * @param queryLimit parameter value
     * @param handlerType parameter value
     * @param taskLog parameter value
     * @return the operation result
     * @throws SQLException when the operation fails
     */
    private int executeNonQueryStatements(Connection connection, List<String> scripts, String dbType,
                                            String sqlSegmentDelimiter, List<JSONObject> localParams,
                                            int queryLimit, String handlerType,
                                            StringBuilder taskLog) throws SQLException {
        int updateCount = 0;
        for (String script : scripts) {
            // Handle JDBC SQL execution.
            for (String statement : splitJdbcSqlScript(script, dbType, sqlSegmentDelimiter)) {
                LogUtils.appendLocalLogLine(taskLog, "Execute " + handlerType + " sql: " + statement);
                JdbcExecuteResult result = executeStatement(connection, buildSqlBind(statement, localParams), queryLimit, taskLog);
                updateCount += result.getUpdateCount();
            }
        }
        return updateCount;
    }

    /**
     * Handle JDBC SQL execution.
     *
     * @return the operation result
     */
    private JdbcExecuteResult executeStatement(Connection connection, SqlBind sqlBind, int queryLimit,
                                                StringBuilder taskLog) throws SQLException {
        int updateCount = 0;
        int resultCount = 0;
        LogUtils.appendLocalLogLine(taskLog, "Execute sql: " + sqlBind.sql);
        try (PreparedStatement statement = prepareStatement(connection, sqlBind, queryLimit)) {
            boolean hasResultSet = statement.execute();
            while (true) {
                // Return the operation result.
                if (hasResultSet) {
                    try (ResultSet resultSet = statement.getResultSet()) {
                        resultCount += countResultSetRows(resultSet, 0, taskLog);
                    }
                } else {
                    int count = statement.getUpdateCount();
                    // Handle JDBC SQL execution.
                    if (count == -1) {
                        break;
                    }
                    if (count > 0) {
                        updateCount += count;
                    }
                }
                // Update the related record.
                hasResultSet = statement.getMoreResults();
            }
        }
        return new JdbcExecuteResult(updateCount, resultCount);
    }

    /**
     * Handle JDBC SQL execution.
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
     * Create the required record.
     */
    private PreparedStatement prepareStatement(Connection connection, SqlBind sqlBind, int queryLimit) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sqlBind.sql);
        // Handle DolphinScheduler operations.
        statement.setMaxRows(queryLimit <= 0 ? 10000 : queryLimit);
        for (int i = 0; i < sqlBind.values.size(); i++) {
            statement.setObject(i + 1, sqlBind.values.get(i));
        }
        return statement;
    }

    /**
     * Implementation details.
     *
     * @param resultSet parameter value
     * @param displayRows parameter value
     * @param taskLog parameter value
     * @return the operation result
     * @throws SQLException when the operation fails
     */
    private int countResultSetRows(ResultSet resultSet, int displayRows, StringBuilder taskLog) throws SQLException {
        if (resultSet == null) {
            return 0;
        }
        int count = 0;
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        while (resultSet.next()) {
            // Handle execution logging.
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
     * Handle JSON data for this operation.
     */
    private List<String> getStringList(JSONObject params, String key) {
        // Handle task-related data and operations.
        if (params.getJSONArray(key) == null) {
            return Collections.emptyList();
        }
        return params.getJSONArray(key).toJavaList(String.class).stream()
                .filter(StringUtils::isNotEmpty).collect(Collectors.toList());
    }

    /**
     * Handle JDBC SQL execution.
     * <p>
     * Handle JDBC SQL execution.
     * Implementation details.
     */
    private SqlBind buildSqlBind(String sql, List<JSONObject> localParams) throws SQLException {
        Map<String, Object> values = new HashMap<>();
        for (JSONObject param : localParams) {
            values.put(param.getString("prop"), param.get("value"));
        }

        // Handle JDBC SQL execution.
        Matcher rawMatcher = Pattern.compile("['\"]*!\\{(.*?)}['\"]*").matcher(sql);
        StringBuffer rawSql = new StringBuffer();
        while (rawMatcher.find()) {
            String name = rawMatcher.group(1);
            if (!values.containsKey(name)) {
                throw new SQLException("SQL parameter does not exist：" + name);
            }
            rawMatcher.appendReplacement(rawSql, Matcher.quoteReplacement(String.valueOf(values.get(name))));
        }
        rawMatcher.appendTail(rawSql);

        // Handle JDBC SQL execution.
        Matcher bindMatcher = Pattern.compile("\\$\\{(.*?)}").matcher(rawSql.toString());
        StringBuffer bindSql = new StringBuffer();
        List<Object> bindValues = new ArrayList<>();
        while (bindMatcher.find()) {
            String name = bindMatcher.group(1);
            if (!values.containsKey(name)) {
                throw new SQLException("SQL parameter does not exist：" + name);
            }
            bindValues.add(values.get(name));
            bindMatcher.appendReplacement(bindSql, "?");
        }
        bindMatcher.appendTail(bindSql);
        return new SqlBind(bindSql.toString(), bindValues);
    }

    private static class SqlBind {
        /** Handle JDBC SQL execution. */
        private final String sql;
        /** Implementation details. */
        private final List<Object> values;

        private SqlBind(String sql, List<Object> values) {
            this.sql = sql;
            this.values = values;
        }
    }

    /**
     * Handle JDBC SQL execution.
     * <p>
     * Handle node-related data and operations.
     *
     * @param sql parameter value
     * @param dbType parameter value
     * @param sqlSegmentDelimiter parameter value
     * @return the operation result
     */
    private List<String> splitJdbcSqlScript(String sql, String dbType, String sqlSegmentDelimiter) {
        String segmentDelimiter = normalizeSqlSegmentDelimiter(sqlSegmentDelimiter);
        // Implementation details.
        if (StringUtils.isNotEmpty(segmentDelimiter)) {
            return Arrays.stream(sql.split(Pattern.quote(segmentDelimiter)))
                    .map(String::trim)
                    .filter(StringUtils::isNotEmpty)
                    .collect(Collectors.toList());
        }
        // Handle database and data source configuration.
        if (StringUtils.equals(DbType.MYSQL.getDb(), dbType)) {
            String cleanSQL = SQLParserUtils.removeComment(sql, com.alibaba.druid.DbType.mysql);
            return SQLParserUtils.split(cleanSQL, com.alibaba.druid.DbType.mysql);
        }
        if (StringUtils.equals(DbType.ORACLE.getDb(), dbType)
                || StringUtils.equals(DbType.ORACLE_12C.getDb(), dbType)) {
            // Handle JDBC SQL execution.
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
        // Handle database and data source configuration.
        String cleanSQL = SQLParserUtils.removeComment(sql, com.alibaba.druid.DbType.other);
        return SQLParserUtils.split(cleanSQL, com.alibaba.druid.DbType.other);
    }

    /**
     * Handle JDBC SQL execution.
     *
     * @param delimiter parameter value
     * @return the operation result
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
     * Handle task-related data and operations.
     *
     * @param instance parameter value
     */
    private void markDataDevelopmentSuccess(DppEtlTaskInstanceDO instance) {
        // Handle JDBC SQL execution.
        instance.setStatus(String.valueOf(WorkflowExecutionStatus.SUCCESS.getCode()));
        instance.setEndTime(new Date());
        dppEtlTaskInstanceService.updateById(instance);
    }

    /**
     * Handle task-related data and operations.
     *
     * @param instance parameter value
     * @param e parameter value
     */
    private void markDataDevelopmentFail(DppEtlTaskInstanceDO instance, Exception e) {
        // Handle task-related data and operations.
        instance.setStatus(String.valueOf(WorkflowExecutionStatus.FAILURE.getCode()));
        instance.setEndTime(new Date());
        instance.setRemark(e.getMessage());
        dppEtlTaskInstanceService.updateById(instance);
    }

    /**
     * Handle task-related data and operations.
     *
     * @param startTime parameter value
     * @return the operation result
     */
    private long calcDurationSeconds(Date startTime) {
        // Handle execution logging.
        Date endTime = new Date();
        return (endTime.getTime() - startTime.getTime()) / 1000;
    }

    /**
     * Handle task-related data and operations.
     *
     * @param nodeInstanceId parameter value
     * @param instanceId parameter value
     * @param task parameter value
     * @param msg parameter value
     */
    private void safeDataDevelopmentLog(Long nodeInstanceId, Long instanceId, DppEtlTaskDO task, String msg) {
        try {
            // Handle execution logging.
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
            // Handle JDBC SQL execution.
            log.error("The log writing for the data development task instance failed instanceId={}, msg={}", instanceId, msg, e);
        }
    }
}
