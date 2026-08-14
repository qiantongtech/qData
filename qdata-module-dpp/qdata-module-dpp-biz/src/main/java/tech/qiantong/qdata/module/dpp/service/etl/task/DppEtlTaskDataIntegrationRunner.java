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

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.api.ds.api.etl.ds.TaskInstance;
import tech.qiantong.qdata.common.enums.CommandType;
import tech.qiantong.qdata.common.enums.Priority;
import tech.qiantong.qdata.common.enums.TaskComponentTypeEnum;
import tech.qiantong.qdata.common.enums.TaskExecutionStatus;
import tech.qiantong.qdata.common.enums.WorkflowExecutionStatus;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.JSONUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.uuid.IdUtils;
import tech.qiantong.qdata.datax.DataXExecutor;
import tech.qiantong.qdata.datax.DataXJsonBuilder;
import tech.qiantong.qdata.datax.DataXResult;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskNodeRelPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskNodeRelRespVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlNodeInstanceDO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskDO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskInstanceDO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskInstanceLogDO;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlNodeInstanceService;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlNodeService;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlTaskInstanceLogService;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlTaskInstanceService;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlTaskNodeRelService;
import tech.qiantong.qdata.module.dpp.utils.datax.FlinkxJson;
import tech.qiantong.qdata.module.dpp.utils.log.LogUtils;
import tech.qiantong.qdata.module.dpp.utils.TaskConverter;
import tech.qiantong.qdata.redis.service.IRedisService;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 数据集成 DataX 本地任务执行器。
 * <p>
 * 负责将数据集成任务中的输入、处理、输出节点参数组装为 DataX JSON，
 * 并统一处理任务实例创建、延迟执行、失败重试、状态回写和执行日志保存。
 *
 * @author qdata
 */
@Service
@Slf4j
public class DppEtlTaskDataIntegrationRunner {
    private static final int REALTIME_LOG_FLUSH_LINES = 50;
    private static final long REALTIME_LOG_FLUSH_INTERVAL_MILLIS = 1000L;
    private static final String MASKED_SECRET = "******";
    private static final Pattern INCREMENT_ID_LOG_PATTERN = Pattern.compile("(?m)^QDATA_INCREMENT_ID=([+-]?\\d+)\\s*$");
    private static final Pattern INCREMENT_TIME_LOG_PATTERN = Pattern.compile("(?m)^QDATA_INCREMENT_TIME=([A-Za-z_][A-Za-z0-9_$#.]*)\\|([^\\r\\n]+?)\\s*$");
    @Resource
    private IDppEtlNodeService iDppEtlNodeService;
    @Resource
    private IDppEtlTaskInstanceService dppEtlTaskInstanceService;
    @Resource
    private IDppEtlTaskInstanceLogService iDppEtlTaskInstanceLogService;
    @Resource
    private DataXExecutor dataXExecutor;
    @Resource
    private IDppEtlTaskNodeRelService iDppEtlTaskNodeRelService;
    @Resource
    private IDppEtlNodeInstanceService dppEtlNodeInstanceService;
    @Resource
    private IRedisService redisService;

    /**
     * 启动数据集成本地 DataX 任务。
     *
     * @param dppEtlTaskDO 数据集成任务详情
     */
    @Transactional
    public void startDppEtlTaskDataIntegration(DppEtlTaskDO dppEtlTaskDO, DppEtlTaskInstanceDO instance, StringBuilder taskLog) {
        Date startTime = new Date();
        List<Long> nodeInstanceIds = new ArrayList<>();

        // DataX 启动前先创建日志记录，页面可立即查询到初始化日志。
        saveLocalDataXTaskInstanceLogSafely(instance, dppEtlTaskDO, taskLog.toString());
        try {
            // 查询这个任务保存下来的节点配置，里面包含数据源、表名、字段等 DataX 参数。
            List<DppEtlNodeRespVO> nodeList = iDppEtlNodeService.listNodeByTaskId(dppEtlTaskDO.getId());
            // 节点为空时无法生成 DataX 作业，直接按业务异常结束本次执行。
            if (CollectionUtils.isEmpty(nodeList)) {
                throw new ServiceException("The local DataX task has not been configured with nodes. Please save the task first！");
            }
            // 为任务中的每个节点创建运行实例，后续随任务执行结果统一更新状态。
            for (DppEtlNodeRespVO node : nodeList) {
                nodeInstanceIds.add(createNodeInstance(dppEtlTaskDO, node, instance));
            }

            // 输入节点负责 reader 参数，输出节点负责 writer 参数。
            DppEtlNodeRespVO readerNode = FlinkxJson.findLocalDataXNode(nodeList, new ArrayList<String>() {{
                add(TaskComponentTypeEnum.DB_READER.getCode());
                add(TaskComponentTypeEnum.EXCEL_READER.getCode());
                add(TaskComponentTypeEnum.CSV_READER.getCode());
            }}).stream().findFirst().orElse(null);
            ;
            List<DppEtlNodeRespVO> processorNodes = FlinkxJson.findLocalDataXNode(nodeList, new ArrayList<String>() {{
                add(TaskComponentTypeEnum.SELECT_FIELDS.getCode());
                add(TaskComponentTypeEnum.SPARK_CLEAN.getCode());
                add(TaskComponentTypeEnum.SORT_RECORD.getCode());
                add(TaskComponentTypeEnum.FIELD_DERIVATION.getCode());
                add(TaskComponentTypeEnum.DATA_DEDUPLICATION.getCode());
                add(TaskComponentTypeEnum.VALUE_MAP.getCode());
                add(TaskComponentTypeEnum.ADD_CONSTANT.getCode());
            }});
            if (processorNodes.size() > 1) {
                DppEtlTaskNodeRelPageReqVO reqVO = new DppEtlTaskNodeRelPageReqVO();
                reqVO.setTaskId(dppEtlTaskDO.getId());
                reqVO.setTaskCode(dppEtlTaskDO.getCode());
                reqVO.setTaskVersion(dppEtlTaskDO.getVersion());

                List<DppEtlTaskNodeRelRespVO> relations = iDppEtlTaskNodeRelService.getDppEtlTaskNodeRelRespVOList(reqVO);

                // 需要将转换组件进行排序执行
                processorNodes = topologicalSortProcessorNodes(processorNodes, relations);
            }
            DppEtlNodeRespVO writerNode = FlinkxJson.findLocalDataXNode(nodeList, TaskComponentTypeEnum.DB_WRITER.getCode());
            // DataX 本地执行至少需要 reader 和 writer，处理节点为可选配置。
            if (readerNode == null || writerNode == null) {
                throw new ServiceException("The local DataX task has no input or output nodes configured. Please save the task first！");
            }

            Map<String, Object> readerNodeJsonMap = JSONUtils.convertTaskDefinitionJsonMap(readerNode.getParameters());
            // 处理参数本身不包含组件编码，补充 componentType 供 DataX 区分不同处理组件。
            readerNodeJsonMap.put("componentType", readerNode.getComponentType());
            applyCachedIncrementIdCursor(readerNode, readerNodeJsonMap, taskLog);
            IncrementIdCursorContext incrementIdCursor = buildIncrementIdCursorContext(readerNode, readerNodeJsonMap);
            DateIncrementCursorContext dateIncrementCursor = buildDateIncrementCursorContext(readerNode, readerNodeJsonMap);

            Map<String, Object> writerNodeJsonMap = JSONUtils.convertTaskDefinitionJsonMap(writerNode.getParameters());

            List<Map<String, Object>> definitionJsonMaps = new ArrayList<>();
            // 处理节点为可选节点，存在多个时按节点顺序全部参与 DataX JSON 构建。
            for (DppEtlNodeRespVO processorNode : processorNodes) {
                Map<String, Object> definitionJsonMap = JSONUtils.convertTaskDefinitionJsonMap(processorNode.getParameters());
                // 处理参数本身不包含组件编码，补充 componentType 供 DataX 区分不同处理组件。
                definitionJsonMap.put("componentType", processorNode.getComponentType());
                definitionJsonMaps.add(definitionJsonMap);
            }

            // 生成 DataX JSON
            String json = DataXJsonBuilder.buildJson(readerNodeJsonMap, writerNodeJsonMap, definitionJsonMaps);
            // Never persist connection passwords in task logs.
            LogUtils.appendLocalLogLine(taskLog, "DataX JSON: " + maskSensitiveJson(json));

            LogUtils.appendLocalLogLine(taskLog, "********************************* Execute DataX task instance ********************************");
            LogUtils.appendLocalLogLine(taskLog, "Start executing DataX job");

            DataXExecutionTiming timing = buildDataXExecutionTiming(dppEtlTaskDO.getDraftJson());
            LogUtils.appendLocalLogLine(taskLog, String.format("DataX execution timing: delay=%d minutes, retryTimes=%d, retryInterval=%d minutes", timing.getDelayMinutes(), timing.getRetryTimes(), timing.getRetryIntervalMinutes()));

            DataXResult run = executeDataXJobWithRetry(json, timing, taskLog, instance, dppEtlTaskDO);
            LogUtils.appendLocalLogLine(taskLog, "DataX exitCode: " + run.getExitCode());
            LogUtils.appendLocalLogLine(taskLog, "*********************************** Execute DataX task end *************************************");
            // DataX 返回非成功状态时，统一抛出异常交给失败分支回写实例状态。
            if (!run.isSuccess()) {
                throw new ServiceException("DataX task execution failed，exitCode=" + run.getExitCode());
            }
            persistIncrementIdCursor(run, incrementIdCursor, taskLog);
            persistDateIncrementCursors(run, dateIncrementCursor, taskLog);
            markLocalDataXTaskSuccess(instance);
            updateNodeInstances(nodeInstanceIds, TaskExecutionStatus.SUCCESS);
            LogUtils.appendLocalLogLine(taskLog, "DataX task executed successfully");
        } catch (Exception e) {
            // 任意异常都标记任务失败，并将失败原因写入本地执行日志。
            updateNodeInstances(nodeInstanceIds, TaskExecutionStatus.FAILURE);
            markLocalDataXTaskFail(instance, e);
            LogUtils.appendLocalLogLine(taskLog, "DataX task execution failed: " + JSONUtils.formatJson(JSONUtils.toJson(e.getMessage())));
        } finally {
            // 无论成功失败都保存完整执行日志，方便任务实例详情页排查。
            long duration = (new Date().getTime() - startTime.getTime()) / 1000L;
            LogUtils.appendLocalLogLine(taskLog, "DataX task execution finished, duration: " + duration + " seconds");
            saveLocalDataXTaskInstanceLog(instance, dppEtlTaskDO, taskLog.toString());
        }
    }

    /**
     * Replaces the saved initial ID with the latest Redis cursor before building DataX JSON.
     * This execution-time lookup is shared by manual runs and Quartz-triggered runs.
     */
    private void applyCachedIncrementIdCursor(DppEtlNodeRespVO readerNode,
                                              Map<String, Object> readerParameters,
                                              StringBuilder taskLog) {
        // Only ID incremental readers are allowed to consume the ID cursor cache.
        if (!"2".equals(String.valueOf(readerParameters.get("readModeType")))) {
            return;
        }
        JSONObject idConfig = JSONObject.parseObject(
                JSONObject.toJSONString(readerParameters.get("idIncrementConfig")));
        String incrementColumn = idConfig == null ? null : idConfig.getString("incrementColumn");
        if (StringUtils.isEmpty(incrementColumn)) {
            throw new ServiceException("ID incremental reading is not configured with incrementColumn");
        }
        String redisKey = TaskConverter.ETL_READER_ID_KEY + readerNode.getCode() + ":" + incrementColumn;
        String cachedCursor = redisService.get(redisKey);
        // A missing cache means this is the first run, so retain the configured initial value.
        if (StringUtils.isEmpty(cachedCursor)) {
            return;
        }
        BigInteger cursor;
        try {
            cursor = new BigInteger(cachedCursor);
        } catch (NumberFormatException e) {
            throw new ServiceException("The cached ID incremental cursor is not an integer: " + cachedCursor);
        }
        if (cursor.signum() < 0) {
            throw new ServiceException("The cached ID incremental cursor cannot be negative: " + cachedCursor);
        }
        BigInteger nextStart = applyNextIncrementStart(readerParameters, idConfig, cursor);
        LogUtils.appendLocalLogLine(taskLog, String.format(
                "DataX ID increment cursor loaded: column=%s, incrementStart=%s",
                incrementColumn, nextStart));
    }

    /**
     * Converts the greatest successfully processed ID into the next inclusive start ID.
     * The initial configured incrementStart remains inclusive, while cached completed IDs advance by one.
     */
    static BigInteger applyNextIncrementStart(Map<String, Object> readerParameters,
                                              JSONObject idConfig, BigInteger completedCursor) {
        BigInteger nextStart = completedCursor.add(BigInteger.ONE);
        idConfig.put("incrementStart", nextStart.toString());
        readerParameters.put("idIncrementConfig", idConfig);
        return nextStart;
    }

    /**
     * Builds the Redis cursor context only for ID incremental readers.
     * Full reads and date incremental reads must not participate in the ID cursor workflow.
     */
    private IncrementIdCursorContext buildIncrementIdCursorContext(DppEtlNodeRespVO readerNode, Map<String, Object> readerParameters) {
        // Restrict all ID log parsing and Redis updates to readModeType=2.
        if (!"2".equals(String.valueOf(readerParameters.get("readModeType")))) {
            return null;
        }
        Object configValue = readerParameters.get("idIncrementConfig");
        JSONObject idConfig;
        if (configValue instanceof Map) {
            idConfig = new JSONObject((Map<String, Object>) configValue);
        } else {
            idConfig = JSONObject.parseObject(String.valueOf(configValue));
        }
        String incrementColumn = idConfig == null ? null : idConfig.getString("incrementColumn");
        if (StringUtils.isEmpty(incrementColumn)) {
            throw new ServiceException("ID incremental reading is not configured with incrementColumn");
        }
        String redisKey = TaskConverter.ETL_READER_ID_KEY + readerNode.getCode() + ":" + incrementColumn;
        return new IncrementIdCursorContext(redisKey, incrementColumn);
    }

    /**
     * Persists the maximum ID emitted by the final successful DataX attempt.
     * The Redis operation is atomic and never moves an existing cursor backwards.
     */
    private void persistIncrementIdCursor(DataXResult result, IncrementIdCursorContext context, StringBuilder taskLog) {
        // A null context means this reader is not running in ID incremental mode.
        if (context == null) {
            return;
        }
        Optional<BigInteger> parsedCursor = parseMaxIncrementId(result.getOutput());
        // DataX emits no marker when the result set is empty, so keep the previous cursor.
        if (!parsedCursor.isPresent()) {
            LogUtils.appendLocalLogLine(taskLog, "DataX ID increment produced no new cursor; Redis cursor remains unchanged");
            return;
        }

        BigInteger newCursor = parsedCursor.get();
        // Redis cursor keys are defined as non-negative integer IDs.
        if (newCursor.signum() < 0) {
            throw new ServiceException("The ID incremental cursor returned by DataX cannot be negative：" + newCursor);
        }
        boolean updated = redisService.setIfGreater(context.redisKey, newCursor.toString());
        LogUtils.appendLocalLogLine(taskLog, updated ? String.format("DataX ID increment cursor saved: column=%s, value=%s", context.incrementColumn, newCursor) : String.format("DataX ID increment cursor not advanced: column=%s, candidate=%s", context.incrementColumn, newCursor));
    }

    /**
     * Parses standalone cursor marker lines and returns the greatest value when multiple
     * reader task markers are present. BigInteger preserves arbitrarily large database IDs.
     */
    static Optional<BigInteger> parseMaxIncrementId(String output) {
        if (StringUtils.isEmpty(output)) {
            return Optional.empty();
        }
        Matcher matcher = INCREMENT_ID_LOG_PATTERN.matcher(output);
        BigInteger maxId = null;
        while (matcher.find()) {
            BigInteger current = new BigInteger(matcher.group(1));
            if (maxId == null || current.compareTo(maxId) > 0) {
                maxId = current;
            }
        }
        return Optional.ofNullable(maxId);
    }

    /**
     * Builds field-to-Redis-key mappings only for type-2 time-range cursors.
     */
    private DateIncrementCursorContext buildDateIncrementCursorContext(DppEtlNodeRespVO readerNode, Map<String, Object> readerParameters) {
        // Full and ID incremental readers must not enter the time-cursor workflow.
        if (!"3".equals(String.valueOf(readerParameters.get("readModeType")))) {
            return null;
        }
        JSONObject dateConfig = JSONObject.parseObject(JSONObject.toJSONString(readerParameters.get("dateIncrementConfig")));
        String dateFormat = dateConfig == null ? null : dateConfig.getString("dateFormat");
        // The same format is required for log validation, legacy migration and Redis values.
        if (StringUtils.isEmpty(dateFormat)) {
            throw new ServiceException("Time incremental reading is not configured with dateFormat");
        }
        Map<String, String> redisKeys = new LinkedHashMap<>();
        if (dateConfig.getJSONArray("column") != null) {
            dateConfig.getJSONArray("column").forEach(value -> {
                JSONObject item = (JSONObject) value;
                // Only type-2 conditions own a reusable time cursor.
                if ("2".equals(item.getString("type"))) {
                    String column = item.getString("incrementColumn");
                    redisKeys.put(column.toUpperCase(Locale.ROOT), TaskConverter.ETL_READER_DATE_KEY + readerNode.getCode() + ":" + column);
                }
            });
        }
        return new DateIncrementCursorContext(dateFormat, redisKeys);
    }

    /**
     * Persists only markers emitted by the final successful DataX attempt.
     */
    private void persistDateIncrementCursors(DataXResult result, DateIncrementCursorContext context, StringBuilder taskLog) {
        // A missing or empty context means this job has no type-2 time cursor to persist.
        if (context == null || context.redisKeys.isEmpty()) {
            return;
        }
        Map<String, String> cursors = parseDateIncrementCursors(result.getOutput());
        Map<String, String> valuesByRedisKey = new LinkedHashMap<>();
        Map<String, Long> epochsByRedisKey = new LinkedHashMap<>();
        Map<String, Long> legacyEpochsByRedisKey = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : context.redisKeys.entrySet()) {
            String value = cursors.get(entry.getKey());
            // Successful DataX output must contain every configured cursor marker.
            if (value == null) {
                throw new ServiceException("DataX did not return the time cursor for column: " + entry.getKey());
            }
            long epochMillis = parseDateCursor(value, context.dateFormat).getTime();
            valuesByRedisKey.put(entry.getValue(), value);
            epochsByRedisKey.put(entry.getValue(), epochMillis);
            // Parse the pre-epoch Redis representation so upgrades never move a cursor backwards.
            String legacyValue = redisService.get(entry.getValue());
            if (StringUtils.isNotEmpty(legacyValue)) {
                legacyEpochsByRedisKey.put(entry.getValue(),
                        parseDateCursor(legacyValue, context.dateFormat).getTime());
            }
        }
        boolean updated = redisService.setDatesIfLater(valuesByRedisKey, epochsByRedisKey,
                legacyEpochsByRedisKey);
        LogUtils.appendLocalLogLine(taskLog, updated
                ? "DataX time increment cursors saved atomically"
                : "DataX time increment cursors were already current");
    }

    /** Parses standalone time-cursor marker lines; later duplicate markers take precedence. */
    static Map<String, String> parseDateIncrementCursors(String output) {
        Map<String, String> cursors = new LinkedHashMap<>();
        if (StringUtils.isEmpty(output)) {
            return cursors;
        }
        Matcher matcher = INCREMENT_TIME_LOG_PATTERN.matcher(output);
        while (matcher.find()) {
            cursors.put(matcher.group(1).toUpperCase(Locale.ROOT), matcher.group(2));
        }
        return cursors;
    }

    /** Strictly parses and round-trip validates a cursor using its configured dateFormat. */
    static Date parseDateCursor(String value, String dateFormat) {
        try {
            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(dateFormat);
            formatter.setLenient(false);
            Date parsed = formatter.parse(value);
            // Round-trip validation catches partially parsed values and lost precision.
            if (!value.equals(formatter.format(parsed))) {
                throw new java.text.ParseException(value, 0);
            }
            return parsed;
        } catch (IllegalArgumentException | java.text.ParseException e) {
            throw new ServiceException("DataX time cursor does not match dateFormat " + dateFormat + ": " + value);
        }
    }

    /**
     * Immutable execution context for readModeType=3 type-2 time cursors.
     * It remains private because only this runner consumes the DataX cursor markers.
     */
    private static final class DateIncrementCursorContext {
        /** Format shared by DataX output validation, Redis migration and cursor storage. */
        private final String dateFormat;
        /** Maps normalized database column names to their corresponding Redis cursor keys. */
        private final Map<String, String> redisKeys;

        private DateIncrementCursorContext(String dateFormat, Map<String, String> redisKeys) {
            this.dateFormat = dateFormat;
            this.redisKeys = redisKeys;
        }
    }

    /**
     * Immutable execution context for the readModeType=2 ID cursor workflow.
     * It carries only the values required after the final successful DataX attempt.
     */
    private static final class IncrementIdCursorContext {
        /** Redis key used to atomically persist the greatest ID returned by DataX. */
        private final String redisKey;
        /** Configured ID column name used in cursor-related task log messages. */
        private final String incrementColumn;

        private IncrementIdCursorContext(String redisKey, String incrementColumn) {
            this.redisKey = redisKey;
            this.incrementColumn = incrementColumn;
        }
    }

    /**
     * 创建本地 DataX 节点实例。
     *
     * @param task     数据集成任务详情
     * @param node     当前节点配置
     * @param instance 所属任务实例
     * @return 生成的节点实例 ID
     */
    private Long createNodeInstance(DppEtlTaskDO task, DppEtlNodeRespVO node, DppEtlTaskInstanceDO instance) {
        long id = IdUtils.generateArtificialId();
        try {
            JSONObject params = JSONObject.parseObject(node.getParameters());
            if (params == null) {
                throw new ServiceException("The node parameter is empty：" + node.getName());
            }
            // 直接使用当前节点配置创建运行中实例，不依赖节点历史版本表。
            boolean created = dppEtlNodeInstanceService.createLocalDataXNodeInstance(DppEtlNodeInstanceDO.builder().id(id).taskType(node.getTaskType() == null ? task.getType() : node.getTaskType()).name(node.getName()).nodeType(params.getString("taskType")).nodeId(node.getId()).nodeCode(node.getCode()).nodeVersion(node.getVersion() == null ? 0 : node.getVersion().intValue()).taskInstanceId(instance.getId()).taskInstanceName(instance.getName()).projectId(task.getProjectId()).projectCode(task.getProjectCode()).submitTime(new Date()).startTime(new Date()).parameters(node.getParameters()).priority(String.valueOf(Priority.MEDIUM.getCode())).retryTimes(node.getFailRetryTimes() == null ? 0 : node.getFailRetryTimes().intValue()).delayTime(node.getDelayTime() == null ? 0 : node.getDelayTime().intValue()).cpuQuota(node.getCpuQuota() == null ? null : node.getCpuQuota().intValue()).memoryMax(node.getMemoryMax() == null ? null : node.getMemoryMax().intValue()).status(String.valueOf(TaskExecutionStatus.RUNNING_EXECUTION.getCode())).componentType(node.getComponentType()).dsId(id).dsTaskInstanceId(instance.getId()).validFlag(Boolean.TRUE).delFlag(Boolean.FALSE).build());
            if (!created) {
                throw new ServiceException("Node instance save failed：" + node.getName());
            }
        } catch (Exception e) {
            log.error("Failed to create a local DataX node instance，nodeCode={}", node.getCode(), e);
            throw new ServiceException("Failed to create a local DataX node instance：" + node.getName() + "，" + e.getMessage());
        }
        return id;
    }

    /**
     * 批量更新本地 DataX 节点实例状态。
     *
     * @param nodeInstanceIds 节点实例 ID 列表
     * @param status          目标执行状态
     */
    private void updateNodeInstances(Collection<Long> nodeInstanceIds, TaskExecutionStatus status) {
        nodeInstanceIds.forEach(id -> updateNodeInstance(id, status));
    }

    /**
     * 更新本地 DataX 节点实例状态。
     *
     * @param id     节点实例 ID
     * @param status 目标执行状态
     */
    private void updateNodeInstance(Long id, TaskExecutionStatus status) {
        Date date = new Date();
        try {
            dppEtlNodeInstanceService.updateNodeInstance(TaskInstance.builder().id(id).startTime(date).endTime(date).state(status).build());
        } catch (Exception e) {
            // 状态更新失败只记录服务端日志，任务实例仍按 DataX 的实际结果回写。
            log.error("Update local DataX node instance exception，nodeInstanceId={}, status={}", id, status, e);
        }
    }

    /**
     * 根据 preNodeCode -> postNodeCode 对处理节点进行拓扑排序。
     * <p>
     * 关系集合已经包含完整任务图的节点编码，因此无需额外传入节点列表。
     * 先对完整关系图排序，再从排序结果中提取处理节点，以保留中间节点形成的传递依赖。
     */
    private List<DppEtlNodeRespVO> topologicalSortProcessorNodes(List<DppEtlNodeRespVO> processorNodes, Collection<DppEtlTaskNodeRelRespVO> relations) {
        Map<String, DppEtlNodeRespVO> processorNodeMap = new LinkedHashMap<>();
        processorNodes.forEach(node -> processorNodeMap.putIfAbsent(node.getCode(), node));

        // 邻接表使用 Set 对重复关系去重，LinkedHashMap 保持关系的原始顺序。
        Map<String, Set<String>> nextNodeMap = new LinkedHashMap<>();
        relations.stream().filter(relation -> !Boolean.FALSE.equals(relation.getValidFlag())).forEach(relation -> {
            String preNodeCode = relation.getPreNodeCode();
            String postNodeCode = relation.getPostNodeCode();
            nextNodeMap.computeIfAbsent(preNodeCode, code -> new LinkedHashSet<>()).add(postNodeCode);
            nextNodeMap.computeIfAbsent(postNodeCode, code -> new LinkedHashSet<>());
        });

        // 补充未配置关系的处理节点，保证它们仍出现在最终结果中。
        processorNodeMap.keySet().forEach(code -> nextNodeMap.computeIfAbsent(code, key -> new LinkedHashSet<>()));

        Map<String, Integer> indegreeMap = new LinkedHashMap<>();
        nextNodeMap.keySet().forEach(code -> indegreeMap.put(code, 0));
        nextNodeMap.values().forEach(postNodeCodes -> postNodeCodes.forEach(code -> indegreeMap.merge(code, 1, Integer::sum)));

        Queue<String> zeroIndegreeQueue = new ArrayDeque<>();
        indegreeMap.forEach((code, indegree) -> {
            if (indegree == 0) {
                zeroIndegreeQueue.offer(code);
            }
        });

        List<String> sortedNodeCodes = new ArrayList<>();

        while (!zeroIndegreeQueue.isEmpty()) {
            String currentNodeCode = zeroIndegreeQueue.poll();
            sortedNodeCodes.add(currentNodeCode);

            for (String nextNodeCode : nextNodeMap.get(currentNodeCode)) {
                int newIndegree = indegreeMap.get(nextNodeCode) - 1;
                indegreeMap.put(nextNodeCode, newIndegree);

                if (newIndegree == 0) {
                    zeroIndegreeQueue.offer(nextNodeCode);
                }
            }
        }

        // 关系存在循环时无法得到完整拓扑序，保留处理节点原始顺序。
        if (sortedNodeCodes.size() != nextNodeMap.size()) {
            return processorNodes;
        }

        // listNodeByTaskId 可能因关系查询返回重复节点，按 code 去重后提取处理节点。
        return sortedNodeCodes.stream().filter(processorNodeMap::containsKey).map(processorNodeMap::get).collect(Collectors.toList());
    }

    /**
     * 按任务配置执行 DataX，并在失败时按配置次数和间隔进行重试。
     *
     * @param json    DataX 作业 JSON
     * @param timing  执行延迟和失败重试配置
     * @param taskLog 本次任务实例日志
     * @return DataX 执行结果
     * @throws Exception DataX 执行失败或等待过程被中断
     */
    private DataXResult executeDataXJobWithRetry(String json, DataXExecutionTiming timing, StringBuilder taskLog, DppEtlTaskInstanceDO instance, DppEtlTaskDO task) throws Exception {
        sleepBeforeDataXExecution(timing.getDelayMillis(), taskLog, "Delay before DataX execution");

        Exception lastException = null;
        // 最大执行次数 = 首次执行 + 失败重试次数。
        for (int attempt = 1; attempt <= timing.getMaxAttempts(); attempt++) {
            LogUtils.appendLocalLogLine(taskLog, String.format("Start DataX execution attempt %d/%d", attempt, timing.getMaxAttempts()));
            LogUtils.appendLocalLogLine(taskLog, "DataX output:");
            saveLocalDataXTaskInstanceLogSafely(instance, task, taskLog.toString());
            try {
                RealtimeLogFlushPolicy flushPolicy = new RealtimeLogFlushPolicy(
                        REALTIME_LOG_FLUSH_LINES, REALTIME_LOG_FLUSH_INTERVAL_MILLIS,
                        System.currentTimeMillis());
                DataXResult result = dataXExecutor.run(json, line -> {
                    LogUtils.appendLocalLogLine(taskLog, line);
                    long now = System.currentTimeMillis();
                    // Persist periodically instead of rewriting the growing full log for every line.
                    if (flushPolicy.shouldFlush(now)) {
                        saveLocalDataXTaskInstanceLogSafely(instance, task, taskLog.toString());
                        flushPolicy.markFlushed(now);
                    }
                });
                // 执行成功时立即返回，避免继续进入后续重试流程。
                if (result.isSuccess()) {
                    // 非首次成功说明本次任务经历过重试，补充重试成功日志。
                    if (attempt > 1) {
                        LogUtils.appendLocalLogLine(taskLog, String.format("DataX execution retry succeeded on attempt %d/%d", attempt, timing.getMaxAttempts()));
                    }
                    return result;
                }
                lastException = new ServiceException("DataX任务执行失败，exitCode=" + result.getExitCode());
                LogUtils.appendLocalLogLine(taskLog, "DataX exitCode: " + result.getExitCode());
            } catch (Exception e) {
                lastException = e;
                LogUtils.appendLocalLogLine(taskLog, "DataX execution attempt exception: " + JSONUtils.formatJson(JSONUtils.toJson(e.getMessage())));
            }

            // 当前已经是最后一次执行时，抛出最后一次失败原因。
            if (attempt >= timing.getMaxAttempts()) {
                LogUtils.appendLocalLogLine(taskLog, String.format("DataX execution failed after %d attempt(s)", timing.getMaxAttempts()));
                throw lastException;
            }
            // 未达到最大次数时，按配置的重试间隔等待后继续下一次执行。
            LogUtils.appendLocalLogLine(taskLog, String.format("DataX execution attempt %d/%d failed, will retry after %d minutes", attempt, timing.getMaxAttempts(), timing.getRetryIntervalMinutes()));
            saveLocalDataXTaskInstanceLogSafely(instance, task, taskLog.toString());
            sleepBeforeDataXExecution(timing.getRetryIntervalMillis(), taskLog, "Wait before DataX retry");
        }

        throw new ServiceException("DataX任务执行失败");
    }

    /**
     * Produces a log-safe copy of DataX JSON by masking secret fields recursively.
     * Invalid JSON is not expected here because DataXJsonBuilder has just generated it.
     */
    static String maskSensitiveJson(String json) {
        Object value = JSONObject.parse(json);
        maskSensitiveValue(value);
        return JSONObject.toJSONString(value);
    }

    /** Recursively masks password-like values in JSON objects and arrays. */
    private static void maskSensitiveValue(Object value) {
        if (value instanceof Map) {
            Map<Object, Object> values = (Map<Object, Object>) value;
            for (Map.Entry<Object, Object> entry : values.entrySet()) {
                String key = String.valueOf(entry.getKey());
                if ("password".equalsIgnoreCase(key)) {
                    entry.setValue(MASKED_SECRET);
                } else {
                    maskSensitiveValue(entry.getValue());
                }
            }
        } else if (value instanceof Iterable) {
            for (Object item : (Iterable<?>) value) {
                maskSensitiveValue(item);
            }
        }
    }

    /** Controls line-count and elapsed-time thresholds for realtime task-log persistence. */
    static final class RealtimeLogFlushPolicy {
        private final int lineThreshold;
        private final long intervalMillis;
        private int pendingLines;
        private long lastFlushTime;

        RealtimeLogFlushPolicy(int lineThreshold, long intervalMillis, long initialTime) {
            if (lineThreshold <= 0 || intervalMillis <= 0) {
                throw new IllegalArgumentException("Realtime log flush thresholds must be positive");
            }
            this.lineThreshold = lineThreshold;
            this.intervalMillis = intervalMillis;
            this.lastFlushTime = initialTime;
        }

        /** Records one output line and reports whether either flush threshold was reached. */
        boolean shouldFlush(long now) {
            pendingLines++;
            return pendingLines >= lineThreshold || now - lastFlushTime >= intervalMillis;
        }

        /** Resets both thresholds after a successful or best-effort persistence attempt. */
        void markFlushed(long now) {
            pendingLines = 0;
            lastFlushTime = now;
        }
    }

    /**
     * 从任务草稿 JSON 中解析 DataX 延迟执行和失败重试参数。
     *
     * @param draftJsonText 任务草稿 JSON
     * @return DataX 执行时序配置
     */
    static DataXExecutionTiming buildDataXExecutionTiming(String draftJsonText) {
        Map<String, Object> draftJson = Collections.emptyMap();
        // 草稿内容不为空时才尝试解析，解析失败或为空时使用默认空配置。
        if (StringUtils.isNotEmpty(draftJsonText)) {
            Map<String, Object> parsedDraftJson = JSONUtils.convertTaskDefinitionJsonMap(draftJsonText);
            // JSON 工具可能返回 null，这里兜底为空配置避免后续取值异常。
            if (parsedDraftJson != null) {
                draftJson = parsedDraftJson;
            }
        }
        long failRetryTimes = normalizeNonNegative(toLong(draftJson.get("failRetryTimes")));
        long failRetryInterval = normalizeNonNegative(toLong(draftJson.get("failRetryInterval")));
        long delayTime = normalizeNonNegative(toLong(draftJson.get("delayTime")));
        long maxAttempts = failRetryTimes + 1;
        // 防止极端配置导致重试次数超过 int 上限。
        if (maxAttempts > Integer.MAX_VALUE) {
            maxAttempts = Integer.MAX_VALUE;
        }
        return new DataXExecutionTiming((int) maxAttempts, failRetryTimes, failRetryInterval, delayTime, TimeUnit.MINUTES.toMillis(failRetryInterval), TimeUnit.MINUTES.toMillis(delayTime));
    }

    /**
     * 将配置值转换为 Long。
     *
     * @param value 原始配置值
     * @return 转换后的 Long，无法转换时返回 null
     */
    private static Long toLong(Object value) {
        // 空值视为未配置。
        if (value == null) {
            return null;
        }
        // 数字类型直接取 long 值，兼容 Integer、Long 等类型。
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        try {
            return Long.parseLong(value.toString());
        } catch (NumberFormatException e) {
            // 非数字字符串按未配置处理，后续统一归零。
            return null;
        }
    }

    /**
     * 将空值或负数配置归一化为 0。
     *
     * @param value 原始数值
     * @return 非负数值
     */
    private static long normalizeNonNegative(Long value) {
        // 延迟和重试参数不允许小于 0，异常配置按 0 处理。
        if (value == null || value < 0) {
            return 0L;
        }
        return value;
    }

    /**
     * 按配置等待 DataX 执行或重试。
     *
     * @param millis  等待毫秒数
     * @param taskLog 本次任务实例日志
     * @param action  等待动作描述
     * @throws InterruptedException 等待过程被中断
     */
    private void sleepBeforeDataXExecution(long millis, StringBuilder taskLog, String action) throws InterruptedException {
        // 未配置等待时间时直接继续执行。
        if (millis <= 0) {
            return;
        }
        LogUtils.appendLocalLogLine(taskLog, String.format("%s, wait %d minutes", action, TimeUnit.MILLISECONDS.toMinutes(millis)));
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // 恢复线程中断标记，保证上层调度器能够感知中断状态。
            Thread.currentThread().interrupt();
            LogUtils.appendLocalLogLine(taskLog, action + " interrupted");
            throw e;
        }
    }

    /**
     * 创建本地 DataX 任务实例。
     *
     * @param task 数据集成任务详情
     * @return 已持久化的任务实例
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public DppEtlTaskInstanceDO createLocalDataXTaskInstance(DppEtlTaskDO task) {
        // 本地 DataX 执行没有外部调度实例 ID，使用应用侧生成的 ID 作为实例主键。
        DppEtlTaskInstanceDO instance = DppEtlTaskInstanceDO.builder().id(IdUtils.generateArtificialId()).catId(task.getCatId()).catCode(task.getCatCode()).taskType(task.getType()).name(task.getName()).taskId(task.getId()).taskCode(task.getCode()).taskVersion(task.getVersion() == null ? null : task.getVersion().intValue()).personCharge(task.getPersonCharge()).contactNumber(task.getContactNumber()).projectId(task.getProjectId()).projectCode(task.getProjectCode()).scheduleTime(new Date()).startTime(new Date()).runTimes(1).commandType(String.valueOf(CommandType.START_PROCESS.getCode())).subTaskFlag("0").status(String.valueOf(WorkflowExecutionStatus.RUNNING_EXECUTION.getCode())).validFlag(Boolean.TRUE).delFlag(Boolean.FALSE).dsId(task.getDsId()).build();
        dppEtlTaskInstanceService.save(instance);
        return instance;
    }

    /**
     * 将本地 DataX 任务实例标记为成功。
     *
     * @param instance 本次任务实例
     */
    private void markLocalDataXTaskSuccess(DppEtlTaskInstanceDO instance) {
        instance.setStatus(String.valueOf(WorkflowExecutionStatus.SUCCESS.getCode()));
        instance.setEndTime(new Date());
        dppEtlTaskInstanceService.updateById(instance);
    }

    /**
     * 将本地 DataX 任务实例标记为失败。
     *
     * @param instance 本次任务实例
     * @param e        失败异常
     */
    private void markLocalDataXTaskFail(DppEtlTaskInstanceDO instance, Exception e) {
        instance.setStatus(String.valueOf(WorkflowExecutionStatus.FAILURE.getCode()));
        instance.setEndTime(new Date());
        instance.setRemark(e.getMessage());
        dppEtlTaskInstanceService.updateById(instance);
    }

    /**
     * 保存本地 DataX 任务实例日志。
     *
     * @param instance   本次任务实例
     * @param task       任务详情
     * @param logContent 完整执行日志
     */
    private void saveLocalDataXTaskInstanceLog(DppEtlTaskInstanceDO instance, DppEtlTaskDO task, String logContent) {
        DppEtlTaskInstanceLogDO taskInstanceLog = DppEtlTaskInstanceLogDO.builder().taskInstanceId(instance.getId()).tm(new Date()).taskType(instance.getTaskType() == null ? task.getType() : instance.getTaskType()).taskId(instance.getTaskId() == null ? task.getId() : instance.getTaskId()).taskCode(instance.getTaskCode() == null ? task.getCode() : instance.getTaskCode()).logContent(logContent).validFlag(Boolean.TRUE).delFlag(Boolean.FALSE).build();
        iDppEtlTaskInstanceLogService.saveOrUpdateRealtime(taskInstanceLog);
    }

    /**
     * 实时日志保存失败不应中断正在运行的 DataX 进程。
     */
    private void saveLocalDataXTaskInstanceLogSafely(DppEtlTaskInstanceDO instance, DppEtlTaskDO task, String logContent) {
        try {
            saveLocalDataXTaskInstanceLog(instance, task, logContent);
        } catch (Exception e) {
            log.error("实时保存本地DataX任务日志失败，taskInstanceId={}", instance.getId(), e);
        }
    }

    /**
     * 向任务日志追加一段本地 DataX 文本。
     *
     * @param taskLog 本次任务实例日志
     * @param text    需要追加的文本
     */
    private void appendLocalDataXLogText(StringBuilder taskLog, String text) {
        // 空文本无需写入，避免日志出现多余空行。
        if (StringUtils.isEmpty(text)) {
            return;
        }
        taskLog.append(text);
        // 追加外部文本时补齐换行，保证日志按行展示。
        if (!text.endsWith("\n") && !text.endsWith("\r")) {
            taskLog.append(System.lineSeparator());
        }
    }

    /**
     * DataX 执行时序配置。
     * <p>
     * 同时保留分钟值用于日志展示，保留毫秒值用于实际等待。
     */
    static class DataXExecutionTiming {
        private final int maxAttempts;
        private final long retryTimes;
        private final long retryIntervalMinutes;
        private final long delayMinutes;
        private final long retryIntervalMillis;
        private final long delayMillis;

        /**
         * 创建 DataX 执行时序配置。
         *
         * @param maxAttempts          最大执行次数
         * @param retryTimes           失败重试次数
         * @param retryIntervalMinutes 失败重试间隔，单位分钟
         * @param delayMinutes         延迟执行时间，单位分钟
         * @param retryIntervalMillis  失败重试间隔，单位毫秒
         * @param delayMillis          延迟执行时间，单位毫秒
         */
        DataXExecutionTiming(int maxAttempts, long retryTimes, long retryIntervalMinutes, long delayMinutes, long retryIntervalMillis, long delayMillis) {
            this.maxAttempts = maxAttempts;
            this.retryTimes = retryTimes;
            this.retryIntervalMinutes = retryIntervalMinutes;
            this.delayMinutes = delayMinutes;
            this.retryIntervalMillis = retryIntervalMillis;
            this.delayMillis = delayMillis;
        }

        /**
         * 获取最大执行次数。
         *
         * @return 最大执行次数
         */
        int getMaxAttempts() {
            return maxAttempts;
        }

        /**
         * 获取失败重试次数。
         *
         * @return 失败重试次数
         */
        long getRetryTimes() {
            return retryTimes;
        }

        /**
         * 获取失败重试间隔分钟数。
         *
         * @return 失败重试间隔，单位分钟
         */
        long getRetryIntervalMinutes() {
            return retryIntervalMinutes;
        }

        /**
         * 获取延迟执行分钟数。
         *
         * @return 延迟执行时间，单位分钟
         */
        long getDelayMinutes() {
            return delayMinutes;
        }

        /**
         * 获取失败重试间隔毫秒数。
         *
         * @return 失败重试间隔，单位毫秒
         */
        long getRetryIntervalMillis() {
            return retryIntervalMillis;
        }

        /**
         * 获取延迟执行毫秒数。
         *
         * @return 延迟执行时间，单位毫秒
         */
        long getDelayMillis() {
            return delayMillis;
        }
    }
}
