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

import static tech.qiantong.qdata.common.core.domain.AjaxResult.error;
import static tech.qiantong.qdata.common.core.domain.AjaxResult.success;

/**
 * 数据开发 JDBC 任务执行器。
 * <p>
 * 负责将数据开发任务中的 SQL 开发、存储过程开发节点转成本地 JDBC 执行流程，
 * 并统一处理任务实例、执行日志、运行锁、事务提交和异常回滚。
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
     * 执行数据开发任务中的 JDBC 节点。
     *
     * @param task    数据开发任务详情，必须包含节点定义列表
     * @param instance 当前任务实例
     * @param taskLog 任务运行日志缓冲区
     */
    @Transactional
    public void run(DppEtlTaskDO task, DppEtlTaskInstanceDO instance, StringBuilder taskLog) {
        // 同一个任务同一时间只允许一个执行实例，避免重复点击或调度重入。
        Date startTime = new Date();
        String redisKey = buildDataDevelopmentRunLockKey(task.getId());
        redisService.delete(redisKey);
        // 获取锁失败表示同一任务已有执行实例，禁止重复执行。
        if (!acquireDataDevelopmentRunLock(redisKey)) {
            throw new RuntimeException("历史任务未执行完毕，请稍后重试");
        }

        int totalUpdateCount = 0;
        int totalResultCount = 0;
        LogUtils.appendLocalLogLine(taskLog, "***********************************************************************************************");
        LogUtils.appendLocalLogLine(taskLog, "********************************* Load task instance plugin *********************************");
        LogUtils.appendLocalLogLine(taskLog, "***********************************************************************************************");
        LogUtils.appendLocalLogLine(taskLog, "Send task status RUNNING_EXECUTION");
        LogUtils.appendLocalLogLine(taskLog, "Create TaskChannel: qData DataDevelopmentJdbcTaskRunner successfully");

        // 只执行 SQL 开发和存储过程开发节点，其它清洗、输入、输出节点不属于本地 JDBC 执行范围。
        List<DppEtlNodeRespVO> sqlNodes = getDataDevelopmentSqlNodes(task);
        // 没有可执行 JDBC 节点时直接作为任务配置错误处理。
        if (CollectionUtils.isEmpty(sqlNodes)) {
            throw new ServiceException("数据开发任务未找到SQL或存储过程节点");
        }

        LogUtils.appendLocalLogLine(taskLog, "********************************* Execute task instance *************************************");
        // 汇总所有节点的影响行数和结果行数，方便实例日志里快速判断执行规模。
        for (DppEtlNodeRespVO node : sqlNodes) {
            // 创建节点实例
            Long nodeInstanceId = createNodeInstance(task, node, instance);
            try {
                LogUtils.appendLocalLogLine(taskLog, "Start executing node: " + node.getName());

                JdbcExecuteResult result = executeDataDevelopmentNodeSql(node, taskLog);
                totalUpdateCount += result.getUpdateCount();
                totalResultCount += result.getResultCount();
                LogUtils.appendLocalLogLine(taskLog, String.format("Node execution completed: %s, affected rows=%d, result rows=%d",
                        node.getName(), result.getUpdateCount(), result.getResultCount()));
                // 更新节点实例状态为成功
                updateNodeInstance(nodeInstanceId, TaskExecutionStatus.SUCCESS);
                // 更新任务实例状态为成功
                markDataDevelopmentSuccess(instance);
                LogUtils.appendLocalLogLine(taskLog, String.format("Data development task executed successfully: node count=%d, affected rows=%d, result rows=%d",
                        sqlNodes.size(), totalUpdateCount, totalResultCount));
            } catch (Exception e) {
                // 业务异常和 JDBC 异常统一记录到实例状态与实例日志中，方便页面查看失败原因。
                markDataDevelopmentFail(instance, e);
                LogUtils.appendLocalLogLine(taskLog, "Data development task execution failed: " + JSONUtils.formatJson(JSONUtils.toJson(e.getMessage())));
            } finally {
                // 无论成功失败都释放锁，并在方法结尾一次性写入本次执行的完整日志。
                redisService.delete(redisKey);
                // 异常路径尚未设置结束时间时，在最终清理阶段补齐。
                if (instance.getEndTime() == null) {
                    instance.setEndTime(new Date());
                }
                LogUtils.appendLocalLogLine(taskLog, "Data development task execution finished, duration: " + calcDurationSeconds(startTime) + " seconds");
                safeDataDevelopmentLog(nodeInstanceId, instance.getId(), task, taskLog.toString());
            }
        }
    }

    /**
     * 创建数据开发节点实例。
     *
     * @param task     数据开发任务详情
     * @param node     当前节点配置
     * @param instance 所属任务实例
     * @return 生成的节点实例 ID
     */
    private Long createNodeInstance(DppEtlTaskDO task, DppEtlNodeRespVO node, DppEtlTaskInstanceDO instance) {
        log.info("任务实例创建消息开始>>>>>>>>>>>>>>>>>>>>>>>>>>>");
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
            // 节点实例落库失败只记录错误，实际 SQL 执行结果仍由主流程处理。
            log.error("创建任务实例异常:{}", e.getMessage());
        }
        log.info("任务实例创建消息结束>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        return id;
    }

    /**
     * 更新数据开发任务实例。
     *
     * @param id     节点实例 ID
     * @param status 目标执行状态
     */
    private void updateNodeInstance(Long id, TaskExecutionStatus status) {
        log.info("任务实例更新消息开始>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        Date date = new Date();
        try {
            dppEtlNodeInstanceService.updateNodeInstance(TaskInstance.builder()
                    .id(id)
                    .startTime(date)
                    .endTime(date)
                    .state(status)
                    .build());
        } catch (Exception e) {
            // 状态更新异常不能替代 SQL 的真实执行结果，只记录服务端日志。
            log.error("更新任务实例异常:{}", e.getMessage());
        }
        log.info("任务实例更新消息结束>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    /**
     * 构造数据开发任务执行锁 key。
     *
     * @param taskId 任务 ID
     * @return Redis 锁 key
     */
    private String buildDataDevelopmentRunLockKey(Long taskId) {
        return "dpp:data-development:run:" + taskId;
    }

    /**
     * 获取运行锁。
     *
     * @param redisKey Redis 锁 key
     * @return true 表示获取成功，false 表示已有任务正在运行
     */
    private boolean acquireDataDevelopmentRunLock(String redisKey) {
        String status = redisService.get(redisKey);
        // Redis 中状态为 1 表示任务仍在运行，直接拒绝本次启动。
        if (StringUtils.isNotEmpty(status) && "1".equals(status)) {
            return false;
        }
        // 锁有效期设置为 12 小时，避免异常退出后永久锁死。
        redisService.set(redisKey, "1", 60 * 60 * 12);
        return true;
    }

    /**
     * 创建数据开发任务实例。
     *
     * @param task 数据开发任务详情
     * @return 已持久化的任务实例
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public DppEtlTaskInstanceDO createDataDevelopmentTaskInstance(DppEtlTaskDO task) {
        // 状态使用 DolphinScheduler 兼容码：1 表示运行中。
        DppEtlTaskInstanceDO instance = DppEtlTaskInstanceDO.builder()
                // 本地 JDBC 执行没有 DolphinScheduler 的 processInstanceId，且 DM 表 ID 为非空字段，所以这里由应用侧生成实例主键。
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
                .validFlag(Boolean.TRUE)
                .delFlag(Boolean.FALSE)
                .dsId(task.getDsId())
                .build();
        dppEtlTaskInstanceService.save(instance);
        return instance;
    }

    /**
     * 从任务节点列表中过滤出 JDBC 可执行节点。
     *
     * @param task 数据开发任务详情
     * @return SQL 开发和存储过程开发节点列表
     */
    private List<DppEtlNodeRespVO> getDataDevelopmentSqlNodes(DppEtlTaskDO task) {
        // 查询这个任务保存下来的节点配置，里面包含数据源、表名、字段等 DataX 参数。
        List<DppEtlNodeRespVO> nodeList = iDppEtlNodeService.listNodeByTaskId(task.getId());
        // 没有节点时返回空列表，由上层统一转成业务异常。
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
     * 执行单个数据开发 JDBC 节点。
     *
     * @param node    节点配置
     * @param taskLog 任务日志
     * @return JDBC 执行结果
     * @throws Exception 执行失败时抛出，外层统一更新任务实例状态
     */
    private JdbcExecuteResult executeDataDevelopmentNodeSql(DppEtlNodeRespVO node, StringBuilder taskLog) throws Exception {
        // 节点参数是前端组件保存的 JSON，包含 SQL、SQL 类型和数据源配置。
        JSONObject params = JSONObject.parseObject(node.getParameters());
        // JSON 解析为空表示节点没有有效执行参数，不能继续创建数据库连接。
        if (params == null) {
            throw new ServiceException("节点参数为空：" + node.getName());
        }

        // SQL 开发节点使用 sql 字段，存储过程节点使用 method 字段。
        String sql = getNodeSql(node, params);
        // SQL 类型决定查询使用 executeQuery，非查询和存储过程使用通用 execute。
        String sqlType = getNodeSqlType(node, params);
        // segm 是前端配置的分段执行符号，未配置时继续使用默认 SQL 脚本拆分逻辑。
        String sqlSegmentDelimiter = getNodeSqlSegmentDelimiter(params);
        // 将节点内的数据源 JSON 转成项目通用 DbQueryProperty，复用已有 JDBC URL 生成逻辑。
        DbQueryProperty dbQueryProperty = buildNodeDbQueryProperty(params);

        LogUtils.appendLocalLogLine(taskLog, "Initialize sql task parameter " + JSONUtils.formatJson(params.toJSONString()));
        LogUtils.appendLocalLogLine(taskLog, "Database type: " + dbQueryProperty.getDbType());
        LogUtils.appendLocalLogLine(taskLog, "SQL type: " + sqlType);
        // 只有用户显式配置分隔符时才输出，便于判断复杂过程脚本是否会按预期拆分。
        if (StringUtils.isNotEmpty(sqlSegmentDelimiter)) {
            LogUtils.appendLocalLogLine(taskLog, "SQL segment delimiter: " + sqlSegmentDelimiter);
        }
        LogUtils.appendLocalLogLine(taskLog, "Full sql parameters: " + sql);
        LogUtils.appendLocalLogLine(taskLog, "Prepare to create JDBC connection");

        Class.forName(getDriverClassName(dbQueryProperty.getDbType()));

        Long failRetryInterval = params.getLong("failRetryInterval"); // '失败重试间隔'，单位：分钟
        Long failRetryTimes = params.getLong("failRetryTimes"); // '失败重试次数:'，单位：次
        Long delayTime = params.getLong("delayTime");//'延迟执行时间:'，单位：分钟
        SqlExecutionTiming timing = buildSqlExecutionTiming(failRetryTimes, failRetryInterval, delayTime);

        LogUtils.appendLocalLogLine(taskLog, String.format("SQL execution timing: delay=%d minutes, retryTimes=%d, retryInterval=%d minutes",
                normalizeNonNegative(delayTime), normalizeNonNegative(failRetryTimes), normalizeNonNegative(failRetryInterval)));
        sleepBeforeSqlExecution(timing.getDelayMillis(), taskLog, "Delay before SQL execution");

        Exception lastException = null;
        // maxAttempts 已包含首次执行，因此 failRetryTimes=0 时循环只执行一次。
        for (int attempt = 1; attempt <= timing.getMaxAttempts(); attempt++) {
            LogUtils.appendLocalLogLine(taskLog, String.format("Start SQL execution attempt %d/%d", attempt, timing.getMaxAttempts()));
            try {

                try (Connection connection = DriverManager.getConnection(
                        dbQueryProperty.trainToJdbcUrl(),
                        dbQueryProperty.getUsername(),
                        dbQueryProperty.getPassword())) {
                    connection.setAutoCommit(false);
                    try {
                        // pre、main、post SQL 共用同一连接和事务，全部成功后统一提交。
                        JdbcExecuteResult result = executeJdbcSql(connection, sql.trim(), dbQueryProperty.getDbType(), sqlType,
                                sqlSegmentDelimiter, params, taskLog);
                        connection.commit();

                        // 仅重试成功时额外记录恢复信息，首次成功无需重复输出。
                        if (attempt > 1) {
                            LogUtils.appendLocalLogLine(taskLog, String.format("SQL execution retry succeeded on attempt %d/%d", attempt, timing.getMaxAttempts()));
                        }
                        return result;
                    } catch (Exception e) {
                        // 任意阶段失败都回滚本次连接中尚未提交的 SQL。
                        connection.rollback();
                        LogUtils.appendLocalLogLine(taskLog, "SQL execution failed, transaction rolled back: "
                                + JSONUtils.formatJson(JSONUtils.toJson(e.getMessage())));
                        throw e;
                    }
                }
            } catch (Exception e) {
                log.error("Execute SQL failed", e);
                lastException = e;
                // 已达到最大尝试次数时直接向上抛出，不再进入等待流程。
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
        // 理论上循环失败会在最后一次尝试中抛出，这里保留最后异常作为防御性兜底。
        if (lastException != null) {
            throw lastException;
        }
        throw new ServiceException("dpp.error.scheduler.execute", "执行SQL失败！");
    }

    /**
     * 根据节点配置构造 SQL 延迟和重试时间参数。
     *
     * @param failRetryTimes    失败后重试次数
     * @param failRetryInterval 重试间隔，单位分钟
     * @param delayTime         首次执行前延迟，单位分钟
     * @return 标准化后的执行时间配置
     */
    static SqlExecutionTiming buildSqlExecutionTiming(Long failRetryTimes, Long failRetryInterval, Long delayTime) {
        long retryTimes = normalizeNonNegative(failRetryTimes);
        long maxAttempts = retryTimes + 1;
        // 防止极端配置在转换为 int 时溢出。
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
     * 将空值或负数统一转换为零。
     */
    private static long normalizeNonNegative(Long value) {
        // 延迟、间隔和重试次数都不允许为负数。
        if (value == null || value < 0) {
            return 0L;
        }
        return value;
    }

    /**
     * 在首次执行或重试前等待指定时间。
     *
     * @param millis  等待毫秒数
     * @param taskLog 任务日志
     * @param action  当前等待动作描述
     */
    private void sleepBeforeSqlExecution(long millis, StringBuilder taskLog, String action) throws InterruptedException {
        // 未配置等待时间时直接继续，避免无意义的线程休眠。
        if (millis <= 0) {
            return;
        }
        LogUtils.appendLocalLogLine(taskLog, String.format("%s, wait %d minutes", action, TimeUnit.MILLISECONDS.toMinutes(millis)));
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // 恢复中断标记，使上层调度器能够感知任务已被取消或中断。
            Thread.currentThread().interrupt();
            LogUtils.appendLocalLogLine(taskLog, action + " interrupted");
            throw e;
        }
    }

    /**
     * SQL 执行的延迟及重试配置值对象。
     */
    static class SqlExecutionTiming {
        private final int maxAttempts;
        private final long retryIntervalMillis;
        private final long delayMillis;

        /**
         * 创建执行时间配置。
         */
        SqlExecutionTiming(int maxAttempts, long retryIntervalMillis, long delayMillis) {
            this.maxAttempts = maxAttempts;
            this.retryIntervalMillis = retryIntervalMillis;
            this.delayMillis = delayMillis;
        }

        /** 返回包含首次执行在内的最大尝试次数。 */
        int getMaxAttempts() {
            return maxAttempts;
        }

        /** 返回失败后的重试间隔毫秒数。 */
        long getRetryIntervalMillis() {
            return retryIntervalMillis;
        }

        /** 返回首次执行前的延迟毫秒数。 */
        long getDelayMillis() {
            return delayMillis;
        }
    }

    /**
     * 解析节点 SQL 文本。
     *
     * @param node   节点配置
     * @param params 节点参数 JSON
     * @return 待执行 SQL 文本
     */
    private String getNodeSql(DppEtlNodeRespVO node, JSONObject params) {
        String sql;
        // 存储过程组件按 DolphinScheduler 结构保存到 method 字段。
        if (StringUtils.equals(TaskComponentTypeEnum.PROCEDURE_DEV.getCode(), node.getComponentType())) {
            sql = params.getString("method");
        } else {
            // SQL 开发组件保存到 sql 字段。
            sql = params.getString("sql");
        }
        // SQL 为空没有执行意义，直接作为配置错误返回。
        if (StringUtils.isEmpty(sql)) {
            throw new ServiceException("节点SQL为空：" + node.getName());
        }
        return sql;
    }

    /**
     * 解析 SQL 类型。
     *
     * @param node   节点配置
     * @param params 节点参数 JSON
     * @return SQL 类型：0 查询、1 非查询、2 存储过程
     */
    private String getNodeSqlType(DppEtlNodeRespVO node, JSONObject params) {
        String sqlType = params.getString("sqlType");
        // 前端已经传 sqlType 时优先使用前端配置，便于日志还原用户选择。
        if (StringUtils.isNotEmpty(sqlType)) {
            return sqlType;
        }
        // 存储过程组件没有 sqlType 时默认按存储过程记录。
        if (StringUtils.equals(TaskComponentTypeEnum.PROCEDURE_DEV.getCode(), node.getComponentType())) {
            return "2";
        }
        // SQL 开发组件没有 sqlType 时按非查询脚本兜底。
        return "1";
    }

    /**
     * 解析 SQL 分段执行符号。
     *
     * @param params 节点参数 JSON
     * @return 分段执行符号，未配置时返回 null
     */
    private String getNodeSqlSegmentDelimiter(JSONObject params) {
        return normalizeSqlSegmentDelimiter(params.getString("segm"));
    }

    /**
     * 解析节点数据源配置。
     *
     * @param params 节点参数 JSON
     * @return JDBC 查询属性
     */
    private DbQueryProperty buildNodeDbQueryProperty(JSONObject params) {
        // datasource 是 SQL/存储过程组件保存的数据源连接信息。
        JSONObject datasource = params.getJSONObject("datasources");
        if (datasource == null) {
            throw new ServiceException("节点数据源配置为空");
        }
        return new DbQueryProperty(
                datasource.getString("datasourceType"),
                datasource.getString("ip"),
                datasource.getLong("port"),
                datasource.get("datasourceConfig").toString()
        );
    }

    /**
     * 标准化数据库类型。
     *
     * @param dbType 节点中的数据库类型
     * @return 项目 DbType 枚举使用的数据库类型编码
     */
    private String normalizeDbType(String dbType) {
        if (StringUtils.isEmpty(dbType)) {
            throw new ServiceException("数据库类型不能为空");
        }
        // MySQL 组件通常传 mysql，项目 DbType 使用 MySql。
        if (StringUtils.equalsIgnoreCase(dbType, "mysql")) {
            return DbType.MYSQL.getDb();
        }
        // Oracle 统一走 Oracle12c 方言，兼容分页和常见连接生成逻辑。
        if (StringUtils.equalsIgnoreCase(dbType, "oracle") || StringUtils.equalsIgnoreCase(dbType, "oracle12c")) {
            return DbType.ORACLE_12C.getDb();
        }
        // 达梦兼容 dm 和 dm8 两种前端传值。
        if (StringUtils.equalsIgnoreCase(dbType, "dm8") || StringUtils.equalsIgnoreCase(dbType, "dm")) {
            return DbType.DM8.getDb();
        }
        // 人大金仓兼容 king base 和 king base8 两种前端传值。
        if (StringUtils.equalsIgnoreCase(dbType, "kingbase") || StringUtils.equalsIgnoreCase(dbType, "kingbase8")) {
            return DbType.KINGBASE8.getDb();
        }
        // 未知类型原样返回，由后续驱动匹配抛出不支持异常。
        return dbType;
    }

    /**
     * 解密数据源密码。
     *
     * @param password 原始密码
     * @return 可用于 JDBC 连接的密码
     */
    private String decryptPasswordIfNeeded(String password) {
        if (StringUtils.isEmpty(password)) {
            return password;
        }
        try {
            // 兼容平台保存的 AES 密文密码。
            return AesEncryptUtil.desEncrypt(password).trim();
        } catch (Exception e) {
            // 解密失败说明可能已经是明文，直接返回原值。
            return password;
        }
    }

    /**
     * 根据数据库类型获取 JDBC 驱动类名。
     *
     * @param dbType 标准化后的数据库类型
     * @return JDBC 驱动类名
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
        throw new ServiceException("不支持的数据库类型：" + dbType);
    }

    /**
     * 执行 SQL 脚本。
     *
     * @param connection JDBC 连接
     * @param sql        SQL 脚本文本
     * @return 执行结果
     * @throws SQLException SQL 执行异常
     */
    private JdbcExecuteResult executeJdbcSql(Connection connection, String sql, String dbType, String sqlType,
                                              String sqlSegmentDelimiter, JSONObject params,
                                              StringBuilder taskLog) throws SQLException {
        int updateCount = 0;
        int resultCount = 0;

        // localParams 与 DolphinScheduler SQL 参数结构兼容，用于替换 ${name} 和 !{name} 占位符。
        List<JSONObject> localParams = Optional.ofNullable(params.getJSONArray("localParams"))
                .map(array -> array.toJavaList(JSONObject.class)).orElse(Collections.emptyList());
        // limit 控制 JDBC 最多返回的查询行数；displayRows 只控制写入任务日志的展示行数。
        int queryLimit = params.getIntValue("limit", 10000);
        int displayRows = params.getIntValue("displayRows", 10);

        // 预置语句和后置语句都按非查询脚本处理，并与主 SQL 共用当前连接和事务。
        List<String> preStatements = getStringList(params, "preStatements");
        List<String> postStatements = getStringList(params, "postStatements");

        // 先执行预置 SQL，任意一条失败都会抛出异常并由外层回滚整个节点事务。
        updateCount += executeNonQueryStatements(connection, preStatements, dbType, sqlSegmentDelimiter,
                localParams, queryLimit, "pre", taskLog);

        // 主 SQL 先按自定义分隔符或数据库方言拆分，再逐条绑定参数并执行。
        List<String> mainStatements = splitJdbcSqlScript(sql, dbType, sqlSegmentDelimiter);
        for (String item : mainStatements) {
            SqlBind sqlBind = buildSqlBind(item, localParams);
            // sqlType=0 是纯查询，明确调用 executeQuery 并统计、展示结果集。
            if (StringUtils.equals("0", sqlType)) {
                resultCount += executeQueryStatement(connection, sqlBind, queryLimit, displayRows, taskLog);
            // sqlType=1/2 可能包含 DDL、DML、过程定义或过程调用，使用 execute 兼容不同结果类型。
            } else if (StringUtils.equals("1", sqlType) || StringUtils.equals("2", sqlType)) {
                JdbcExecuteResult result = executeStatement(connection, sqlBind, queryLimit, taskLog);
                updateCount += result.getUpdateCount();
                resultCount += result.getResultCount();
            } else {
                throw new SQLException("不支持的sqlType：" + sqlType + "，有效值为0（查询）、1（非查询）、2（存储过程）");
            }
        }

        // 主 SQL 全部成功后执行后置 SQL，保持 DolphinScheduler 的 pre-main-post 执行顺序。
        updateCount += executeNonQueryStatements(connection, postStatements, dbType, sqlSegmentDelimiter,
                localParams, queryLimit, "post", taskLog);
        return new JdbcExecuteResult(updateCount, resultCount);
    }

    /**
     * 执行预置或后置非查询脚本。
     *
     * @param connection          当前节点 JDBC 连接
     * @param scripts             待执行脚本集合
     * @param dbType              数据库类型
     * @param sqlSegmentDelimiter 自定义分隔符
     * @param localParams         本地 SQL 参数
     * @param queryLimit          JDBC 最大返回行数
     * @param handlerType         日志阶段名称，pre 或 post
     * @param taskLog             任务日志
     * @return 累计影响行数
     * @throws SQLException SQL 执行异常
     */
    private int executeNonQueryStatements(Connection connection, List<String> scripts, String dbType,
                                            String sqlSegmentDelimiter, List<JSONObject> localParams,
                                            int queryLimit, String handlerType,
                                            StringBuilder taskLog) throws SQLException {
        int updateCount = 0;
        for (String script : scripts) {
            // 单个 pre/post 配置项本身也可能包含多条 SQL，因此仍需经过统一拆分器。
            for (String statement : splitJdbcSqlScript(script, dbType, sqlSegmentDelimiter)) {
                LogUtils.appendLocalLogLine(taskLog, "Execute " + handlerType + " sql: " + statement);
                JdbcExecuteResult result = executeStatement(connection, buildSqlBind(statement, localParams), queryLimit, taskLog);
                updateCount += result.getUpdateCount();
            }
        }
        return updateCount;
    }

    /**
     * 使用 JDBC 通用 execute 方法执行一条 SQL，并消费其全部结果集和更新计数。
     *
     * @return 当前 SQL 的影响行数及结果行数
     */
    private JdbcExecuteResult executeStatement(Connection connection, SqlBind sqlBind, int queryLimit,
                                                StringBuilder taskLog) throws SQLException {
        int updateCount = 0;
        int resultCount = 0;
        LogUtils.appendLocalLogLine(taskLog, "Execute sql: " + sqlBind.sql);
        try (PreparedStatement statement = prepareStatement(connection, sqlBind, queryLimit)) {
            boolean hasResultSet = statement.execute();
            while (true) {
                // execute 返回 true 表示当前结果是 ResultSet，并不代表执行成功与否。
                if (hasResultSet) {
                    try (ResultSet resultSet = statement.getResultSet()) {
                        resultCount += countResultSetRows(resultSet, 0, taskLog);
                    }
                } else {
                    int count = statement.getUpdateCount();
                    // 更新计数为 -1 表示当前 SQL 已经没有更多执行结果。
                    if (count == -1) {
                        break;
                    }
                    if (count > 0) {
                        updateCount += count;
                    }
                }
                // 存储过程可能连续返回多个结果集和更新计数，必须循环读取到结束。
                hasResultSet = statement.getMoreResults();
            }
        }
        return new JdbcExecuteResult(updateCount, resultCount);
    }

    /**
     * 执行纯查询 SQL，并统计结果总行数及输出指定数量的结果日志。
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
     * 创建 PreparedStatement、设置最大返回行数并按顺序绑定参数。
     */
    private PreparedStatement prepareStatement(Connection connection, SqlBind sqlBind, int queryLimit) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sqlBind.sql);
        // 未配置或配置为非正数时使用与 DolphinScheduler 一致的默认上限 10000。
        statement.setMaxRows(queryLimit <= 0 ? 10000 : queryLimit);
        for (int i = 0; i < sqlBind.values.size(); i++) {
            statement.setObject(i + 1, sqlBind.values.get(i));
        }
        return statement;
    }

    /**
     * 统计结果集行数。
     *
     * @param resultSet JDBC 结果集
     * @param displayRows 写入日志的最大行数，0 表示只统计不展示
     * @param taskLog     任务日志
     * @return 结果行数
     * @throws SQLException 读取结果集异常
     */
    private int countResultSetRows(ResultSet resultSet, int displayRows, StringBuilder taskLog) throws SQLException {
        if (resultSet == null) {
            return 0;
        }
        int count = 0;
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        while (resultSet.next()) {
            // 超过 displayRows 后不再构造日志对象，但继续消费结果集并统计总行数。
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
     * 从节点 JSON 参数中读取字符串数组，缺失时返回空集合。
     */
    private List<String> getStringList(JSONObject params, String key) {
        // 老任务可能没有 preStatements/postStatements 字段，需要保持向后兼容。
        if (params.getJSONArray(key) == null) {
            return Collections.emptyList();
        }
        return params.getJSONArray(key).toJavaList(String.class).stream()
                .filter(StringUtils::isNotEmpty).collect(Collectors.toList());
    }

    /**
     * 将节点 SQL 转换为可预编译 SQL 及对应参数值。
     * <p>
     * !{name} 直接替换原值，适用于表名、字段名等不能使用 JDBC 占位符的位置；
     * ${name} 替换为问号并通过 PreparedStatement 绑定，适用于普通条件值。
     */
    private SqlBind buildSqlBind(String sql, List<JSONObject> localParams) throws SQLException {
        Map<String, Object> values = new HashMap<>();
        for (JSONObject param : localParams) {
            values.put(param.getString("prop"), param.get("value"));
        }

        // 原值参数不参与预编译，调用者应只将其用于可信的结构性 SQL 片段。
        Matcher rawMatcher = Pattern.compile("['\"]*!\\{(.*?)}['\"]*").matcher(sql);
        StringBuffer rawSql = new StringBuffer();
        while (rawMatcher.find()) {
            String name = rawMatcher.group(1);
            if (!values.containsKey(name)) {
                throw new SQLException("SQL参数不存在：" + name);
            }
            rawMatcher.appendReplacement(rawSql, Matcher.quoteReplacement(String.valueOf(values.get(name))));
        }
        rawMatcher.appendTail(rawSql);

        // 普通参数统一转换为 JDBC 问号占位符，避免直接拼接参数值。
        Matcher bindMatcher = Pattern.compile("\\$\\{(.*?)}").matcher(rawSql.toString());
        StringBuffer bindSql = new StringBuffer();
        List<Object> bindValues = new ArrayList<>();
        while (bindMatcher.find()) {
            String name = bindMatcher.group(1);
            if (!values.containsKey(name)) {
                throw new SQLException("SQL参数不存在：" + name);
            }
            bindValues.add(values.get(name));
            bindMatcher.appendReplacement(bindSql, "?");
        }
        bindMatcher.appendTail(bindSql);
        return new SqlBind(bindSql.toString(), bindValues);
    }

    private static class SqlBind {
        /** 替换完成、可交给 PreparedStatement 的 SQL。 */
        private final String sql;
        /** 按问号出现顺序排列的绑定值。 */
        private final List<Object> values;

        private SqlBind(String sql, List<Object> values) {
            this.sql = sql;
            this.values = values;
        }
    }

    /**
     * 根据数据库类型和可选自定义分段执行符号拆分 SQL 脚本。
     * <p>
     * 自定义分段执行符号由节点参数 segm 提供；未提供时保持原数据库类型拆分逻辑。
     *
     * @param sql                 SQL 脚本文本
     * @param dbType              项目内部数据库类型编码
     * @param sqlSegmentDelimiter 自定义分段执行符号
     * @return JDBC 可逐条执行的 SQL 列表
     */
    private List<String> splitJdbcSqlScript(String sql, String dbType, String sqlSegmentDelimiter) {
        String segmentDelimiter = normalizeSqlSegmentDelimiter(sqlSegmentDelimiter);
        // 显式配置 segm 时必须优先按原始脚本拆分，避免过程体内部的分号被 Druid 误拆或移除。
        if (StringUtils.isNotEmpty(segmentDelimiter)) {
            return Arrays.stream(sql.split(Pattern.quote(segmentDelimiter)))
                    .map(String::trim)
                    .filter(StringUtils::isNotEmpty)
                    .collect(Collectors.toList());
        }
        // 未配置自定义分隔符时，根据实际数据库方言选择 Druid 拆分器。
        if (StringUtils.equals(DbType.MYSQL.getDb(), dbType)) {
            String cleanSQL = SQLParserUtils.removeComment(sql, com.alibaba.druid.DbType.mysql);
            return SQLParserUtils.split(cleanSQL, com.alibaba.druid.DbType.mysql);
        }
        if (StringUtils.equals(DbType.ORACLE.getDb(), dbType)
                || StringUtils.equals(DbType.ORACLE_12C.getDb(), dbType)) {
            // Oracle PL/SQL 块包含内部分号，交给 Oracle 专用语法解析器保持块完整。
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
        // 未识别数据库使用 Druid 通用方言兜底。
        String cleanSQL = SQLParserUtils.removeComment(sql, com.alibaba.druid.DbType.other);
        return SQLParserUtils.split(cleanSQL, com.alibaba.druid.DbType.other);
    }

    /**
     * 标准化 SQL 分段执行符号。
     *
     * @param delimiter 原始分段执行符号
     * @return 去掉首尾空白后的分段执行符号
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
     * 标记任务实例执行成功。
     *
     * @param instance 任务实例
     */
    private void markDataDevelopmentSuccess(DppEtlTaskInstanceDO instance) {
        // SQL 节点全部成功后，将任务实例更新为成功并记录结束时间。
        instance.setStatus(String.valueOf(WorkflowExecutionStatus.SUCCESS.getCode()));
        instance.setEndTime(new Date());
        dppEtlTaskInstanceService.updateById(instance);
    }

    /**
     * 标记任务实例执行失败。
     *
     * @param instance 任务实例
     * @param e        失败异常
     */
    private void markDataDevelopmentFail(DppEtlTaskInstanceDO instance, Exception e) {
        // 保存原始异常消息，方便任务实例页面直接展示失败原因。
        instance.setStatus(String.valueOf(WorkflowExecutionStatus.FAILURE.getCode()));
        instance.setEndTime(new Date());
        instance.setRemark(e.getMessage());
        dppEtlTaskInstanceService.updateById(instance);
    }

    /**
     * 计算任务耗时。
     *
     * @param startTime 任务实例
     * @return 耗时秒数
     */
    private long calcDurationSeconds(Date startTime) {
        // 日志只需要秒级耗时，毫秒差值直接向下取整。
        Date endTime = new Date();
        return (endTime.getTime() - startTime.getTime()) / 1000;
    }

    /**
     * 安全写入任务实例日志。
     *
     * @param nodeInstanceId 节点实例 ID
     * @param instanceId 任务实例 ID
     * @param task       任务详情
     * @param msg        日志内容
     */
    private void safeDataDevelopmentLog(Long nodeInstanceId, Long instanceId, DppEtlTaskDO task, String msg) {
        try {
            // run 方法会在结尾一次性传入完整日志，这里直接保存原文，避免重复追加或重复加日志前缀。
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
            // 日志失败不能覆盖真正的 SQL 执行异常，只记录本地错误日志。
            log.error("数据开发任务实例日志写入失败 instanceId={}, msg={}", instanceId, msg, e);
        }
    }
}
