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

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Local DataX task executor for data integration.
 * <p>
 * Builds DataX JSON from input, deduplication, and output node parameters,
 * and manages task instance creation, delayed execution, retries, status updates, and execution logs.
 *
 * @author qdata
 */
@Service
@Slf4j
public class DppEtlTaskDataIntegrationRunner {
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

    /**
     * Starts a local DataX data integration task.
     *
     * @param dppEtlTaskDO data integration task details
     */
    @Transactional
    public void startDppEtlTaskDataIntegration(DppEtlTaskDO dppEtlTaskDO, DppEtlTaskInstanceDO instance, StringBuilder taskLog) {
        Date startTime = new Date();
        List<Long> nodeInstanceIds = new ArrayList<>();

        try {
            // Load the saved node configuration containing DataX parameters such as data sources, tables, and columns.
            List<DppEtlNodeRespVO> nodeList = iDppEtlNodeService.listNodeByTaskId(dppEtlTaskDO.getId());
            // A DataX job cannot be generated without nodes, so terminate this execution with a business exception.
            if (CollectionUtils.isEmpty(nodeList)) {
                throw new ServiceException("dpp.error.datax.nodes.missing",
                        "The local DataX task has no configured nodes; save the task first");
            }
            // 为任务中的每个节点创建运行实例，后续随任务执行结果统一更新状态。
            for (DppEtlNodeRespVO node : nodeList) {
                nodeInstanceIds.add(createNodeInstance(dppEtlTaskDO, node, instance));
            }

            // The input node provides reader parameters, and the output node provides writer parameters.
            DppEtlNodeRespVO readerNode = FlinkxJson.findLocalDataXNode(nodeList, TaskComponentTypeEnum.DB_READER.getCode());
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
            // Local DataX execution requires a reader and writer; the deduplication node is optional.
            if (readerNode == null || writerNode == null) {
                throw new ServiceException("dpp.error.datax.input.output.missing",
                        "The local DataX task has no input or output node; save the task first");
            }

            Map<String, Object> readerNodeJsonMap = Collections.emptyMap();
            // Parse input parameters only when the reader node exists to avoid null values during JSON construction.
            if (ObjectUtils.isNotEmpty(readerNode)) {
                readerNodeJsonMap = JSONUtils.convertTaskDefinitionJsonMap(readerNode.getParameters());
            }
            Map<String, Object> writerNodeJsonMap = Collections.emptyMap();
            // Parse output parameters only when the writer node exists to avoid null values during JSON construction.
            if (ObjectUtils.isNotEmpty(writerNode)) {
                writerNodeJsonMap = JSONUtils.convertTaskDefinitionJsonMap(writerNode.getParameters());
            }
            List<Map<String, Object>> definitionJsonMaps = new ArrayList<>();
            for (DppEtlNodeRespVO processorNode : processorNodes) {
                Map<String, Object> definitionJsonMap = JSONUtils.convertTaskDefinitionJsonMap(processorNode.getParameters());
                definitionJsonMap.put("componentType", processorNode.getComponentType());
                definitionJsonMaps.add(definitionJsonMap);
            }

            // Generate DataX JSON.
            String json = DataXJsonBuilder.buildJson(readerNodeJsonMap, writerNodeJsonMap, definitionJsonMaps);
            LogUtils.appendLocalLogLine(taskLog, "DataX JSON: " + json);

            LogUtils.appendLocalLogLine(taskLog, "********************************* Execute DataX task instance ********* ***********************");
            LogUtils.appendLocalLogLine(taskLog, "Start executing DataX job");

            DataXExecutionTiming timing = buildDataXExecutionTiming(dppEtlTaskDO.getDraftJson());
            LogUtils.appendLocalLogLine(taskLog, String.format("DataX execution timing: delay=%d minutes, retryTimes=%d, retryInterval=%d minutes",
                    timing.getDelayMinutes(), timing.getRetryTimes(), timing.getRetryIntervalMinutes()));

            DataXResult run = executeDataXJobWithRetry(json, timing, taskLog);
            LogUtils.appendLocalLogLine(taskLog, "DataX exitCode: " + run.getExitCode());
            LogUtils.appendLocalLogLine(taskLog, "DataX output:" + run.getOutput());
            LogUtils.appendLocalLogLine(taskLog, "*********************************** Execute DataX task end *************************************");
            // Throw a common exception for unsuccessful DataX results so the failure branch updates the instance status.
            if (!run.isSuccess()) {
                throw new ServiceException("dpp.error.datax.execution.exit.code",
                        "DataX task execution failed with exit code {0}", run.getExitCode());
            }
            markLocalDataXTaskSuccess(instance);
            updateNodeInstances(nodeInstanceIds, TaskExecutionStatus.SUCCESS);
            LogUtils.appendLocalLogLine(taskLog, "DataX task executed successfully");
        } catch (Exception e) {
            // Mark the task as failed for any exception and write the reason to the local execution log.
            updateNodeInstances(nodeInstanceIds, TaskExecutionStatus.FAILURE);
            markLocalDataXTaskFail(instance, e);
            LogUtils.appendLocalLogLine(taskLog, "DataX task execution failed: " + JSONUtils.formatJson(JSONUtils.toJson(e.getMessage())));
        } finally {
            // Save the complete execution log on both success and failure for task instance troubleshooting.
            long duration = (new Date().getTime() - startTime.getTime()) / 1000L;
            LogUtils.appendLocalLogLine(taskLog, "DataX task execution finished, duration: " + duration + " seconds");
            saveLocalDataXTaskInstanceLog(instance, dppEtlTaskDO, taskLog.toString());
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
            dppEtlNodeInstanceService.createNodeInstance(TaskInstance.builder()
                    .id(id)
                    .name(node.getName())
                    .taskCode(node.getCode())
                    .taskDefinitionVersion(node.getVersion() == null ? 0 : node.getVersion().intValue())
                    .taskType(String.valueOf(params.get("taskType")))
                    .processInstanceId(instance.getId())
                    .processInstanceName(instance.getName())
                    .projectCode(task.getProjectCode())
                    .taskInstancePriority(Priority.MEDIUM)
                    .startTime(new Date())
                    .state(TaskExecutionStatus.RUNNING_EXECUTION)
                    .build());
        } catch (Exception e) {
            // 节点实例创建失败不阻断任务主流程，避免实例服务异常覆盖 DataX 的真实执行结果。
            log.error("创建本地DataX节点实例异常，nodeCode={}", node.getCode(), e);
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
            dppEtlNodeInstanceService.updateNodeInstance(TaskInstance.builder()
                    .id(id)
                    .startTime(date)
                    .endTime(date)
                    .state(status)
                    .build());
        } catch (Exception e) {
            // 状态更新失败只记录服务端日志，任务实例仍按 DataX 的实际结果回写。
            log.error("更新本地DataX节点实例异常，nodeInstanceId={}, status={}", id, status, e);
        }
    }

    /**
     * 根据 preNodeCode -> postNodeCode 对处理节点进行拓扑排序。
     *
     * 关系集合已经包含完整任务图的节点编码，因此无需额外传入节点列表。
     * 先对完整关系图排序，再从排序结果中提取处理节点，以保留中间节点形成的传递依赖。
     */
    private List<DppEtlNodeRespVO> topologicalSortProcessorNodes(
            List<DppEtlNodeRespVO> processorNodes,
            Collection<DppEtlTaskNodeRelRespVO> relations) {
        Map<String, DppEtlNodeRespVO> processorNodeMap = new LinkedHashMap<>();
        processorNodes.forEach(node -> processorNodeMap.putIfAbsent(node.getCode(), node));

        // 邻接表使用 Set 对重复关系去重，LinkedHashMap 保持关系的原始顺序。
        Map<String, Set<String>> nextNodeMap = new LinkedHashMap<>();
        relations.stream()
                .filter(relation -> !Boolean.FALSE.equals(relation.getValidFlag()))
                .forEach(relation -> {
                    String preNodeCode = relation.getPreNodeCode();
                    String postNodeCode = relation.getPostNodeCode();
                    nextNodeMap.computeIfAbsent(preNodeCode, code -> new LinkedHashSet<>()).add(postNodeCode);
                    nextNodeMap.computeIfAbsent(postNodeCode, code -> new LinkedHashSet<>());
                });

        // 补充未配置关系的处理节点，保证它们仍出现在最终结果中。
        processorNodeMap.keySet().forEach(
                code -> nextNodeMap.computeIfAbsent(code, key -> new LinkedHashSet<>())
        );

        Map<String, Integer> indegreeMap = new LinkedHashMap<>();
        nextNodeMap.keySet().forEach(code -> indegreeMap.put(code, 0));
        nextNodeMap.values().forEach(postNodeCodes ->
                postNodeCodes.forEach(code -> indegreeMap.merge(code, 1, Integer::sum))
        );

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
        return sortedNodeCodes.stream()
                .filter(processorNodeMap::containsKey)
                .map(processorNodeMap::get)
                .collect(Collectors.toList());
    }

    /**
     * Executes DataX with the task configuration and retries failures using the configured count and interval.
     *
     * @param json    DataX job JSON
     * @param timing  execution delay and failure retry configuration
     * @param taskLog current task instance log
     * @return the DataX execution result
     * @throws Exception if DataX execution fails or the wait is interrupted
     */
    private DataXResult executeDataXJobWithRetry(String json, DataXExecutionTiming timing, StringBuilder taskLog) throws Exception {
        sleepBeforeDataXExecution(timing.getDelayMillis(), taskLog, "Delay before DataX execution");

        Exception lastException = null;
        // Maximum attempts equal the initial execution plus the configured retries.
        for (int attempt = 1; attempt <= timing.getMaxAttempts(); attempt++) {
            LogUtils.appendLocalLogLine(taskLog, String.format("Start DataX execution attempt %d/%d", attempt, timing.getMaxAttempts()));
            try {
                DataXResult result = dataXExecutor.run(json);
                // Return immediately after success to avoid entering the remaining retry flow.
                if (result.isSuccess()) {
                    // A success after the first attempt indicates a retry, so append a retry success log.
                    if (attempt > 1) {
                        LogUtils.appendLocalLogLine(taskLog, String.format("DataX execution retry succeeded on attempt %d/%d",
                                attempt, timing.getMaxAttempts()));
                    }
                    return result;
                }
                lastException = new ServiceException("dpp.error.datax.execution.exit.code",
                        "DataX task execution failed with exit code {0}", result.getExitCode());
                LogUtils.appendLocalLogLine(taskLog, "DataX exitCode: " + result.getExitCode());
                LogUtils.appendLocalLogLine(taskLog, "DataX output:" + result.getOutput());
            } catch (Exception e) {
                lastException = e;
                LogUtils.appendLocalLogLine(taskLog, "DataX execution attempt exception: " + JSONUtils.formatJson(JSONUtils.toJson(e.getMessage())));
            }

            // Throw the last failure when the current attempt is the final attempt.
            if (attempt >= timing.getMaxAttempts()) {
                LogUtils.appendLocalLogLine(taskLog, String.format("DataX execution failed after %d attempt(s)", timing.getMaxAttempts()));
                throw lastException;
            }
            // Wait for the configured retry interval before the next attempt when attempts remain.
            LogUtils.appendLocalLogLine(taskLog, String.format("DataX execution attempt %d/%d failed, will retry after %d minutes",
                    attempt, timing.getMaxAttempts(), timing.getRetryIntervalMinutes()));
            sleepBeforeDataXExecution(timing.getRetryIntervalMillis(), taskLog, "Wait before DataX retry");
        }

        throw new ServiceException("dpp.error.datax.execution.fail", "DataX task execution failed");
    }

    /**
     * Parses DataX execution delay and failure retry parameters from the task draft JSON.
     *
     * @param draftJsonText task draft JSON
     * @return DataX execution timing configuration
     */
    static DataXExecutionTiming buildDataXExecutionTiming(String draftJsonText) {
        Map<String, Object> draftJson = Collections.emptyMap();
        // Parse only non-empty draft content and use the default empty configuration when parsing fails.
        if (StringUtils.isNotEmpty(draftJsonText)) {
            Map<String, Object> parsedDraftJson = JSONUtils.convertTaskDefinitionJsonMap(draftJsonText);
            // The JSON utility may return null, so use an empty configuration to prevent later access errors.
            if (parsedDraftJson != null) {
                draftJson = parsedDraftJson;
            }
        }
        long failRetryTimes = normalizeNonNegative(toLong(draftJson.get("failRetryTimes")));
        long failRetryInterval = normalizeNonNegative(toLong(draftJson.get("failRetryInterval")));
        long delayTime = normalizeNonNegative(toLong(draftJson.get("delayTime")));
        long maxAttempts = failRetryTimes + 1;
        // Prevent extreme configuration values from exceeding the integer limit for attempt counts.
        if (maxAttempts > Integer.MAX_VALUE) {
            maxAttempts = Integer.MAX_VALUE;
        }
        return new DataXExecutionTiming(
                (int) maxAttempts,
                failRetryTimes,
                failRetryInterval,
                delayTime,
                TimeUnit.MINUTES.toMillis(failRetryInterval),
                TimeUnit.MINUTES.toMillis(delayTime)
        );
    }

    /**
     * Converts a configuration value to Long.
     *
     * @param value original configuration value
     * @return the converted Long, or {@code null} if conversion is not possible
     */
    private static Long toLong(Object value) {
        // Treat null values as not configured.
        if (value == null) {
            return null;
        }
        // Read numeric values directly as long to support Integer, Long, and other number types.
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        try {
            return Long.parseLong(value.toString());
        } catch (NumberFormatException e) {
            // Treat non-numeric strings as not configured; normalization will convert them to zero.
            return null;
        }
    }

    /**
     * Normalizes null or negative configuration values to zero.
     *
     * @param value original value
     * @return a non-negative value
     */
    private static long normalizeNonNegative(Long value) {
        // Delay and retry parameters cannot be negative; treat invalid values as zero.
        if (value == null || value < 0) {
            return 0L;
        }
        return value;
    }

    /**
     * Waits before DataX execution or retry according to the configuration.
     *
     * @param millis  wait duration in milliseconds
     * @param taskLog current task instance log
     * @param action  description of the wait action
     * @throws InterruptedException if the wait is interrupted
     */
    private void sleepBeforeDataXExecution(long millis, StringBuilder taskLog, String action) throws InterruptedException {
        // Continue immediately when no wait duration is configured.
        if (millis <= 0) {
            return;
        }
        LogUtils.appendLocalLogLine(taskLog, String.format("%s, wait %d minutes", action, TimeUnit.MILLISECONDS.toMinutes(millis)));
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // Restore the thread interruption flag so the upstream scheduler can detect the interrupted state.
            Thread.currentThread().interrupt();
            LogUtils.appendLocalLogLine(taskLog, action + " interrupted");
            throw e;
        }
    }

    /**
     * Creates a local DataX task instance.
     *
     * @param task data integration task details
     * @return the persisted task instance
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public DppEtlTaskInstanceDO createLocalDataXTaskInstance(DppEtlTaskDO task) {
        // Local DataX execution has no external schedule instance ID, so use an application-generated primary key.
        DppEtlTaskInstanceDO instance = DppEtlTaskInstanceDO.builder()
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
     * Marks a local DataX task instance as successful.
     *
     * @param instance current task instance
     */
    private void markLocalDataXTaskSuccess(DppEtlTaskInstanceDO instance) {
        instance.setStatus(String.valueOf(WorkflowExecutionStatus.SUCCESS.getCode()));
        instance.setEndTime(new Date());
        dppEtlTaskInstanceService.updateById(instance);
    }

    /**
     * Marks a local DataX task instance as failed.
     *
     * @param instance current task instance
     * @param e        failure exception
     */
    private void markLocalDataXTaskFail(DppEtlTaskInstanceDO instance, Exception e) {
        instance.setStatus(String.valueOf(WorkflowExecutionStatus.FAILURE.getCode()));
        instance.setEndTime(new Date());
        instance.setRemark(e.getMessage());
        dppEtlTaskInstanceService.updateById(instance);
    }

    /**
     * Saves the local DataX task instance log.
     *
     * @param instance   current task instance
     * @param task       task details
     * @param logContent complete execution log
     */
    private void saveLocalDataXTaskInstanceLog(DppEtlTaskInstanceDO instance, DppEtlTaskDO task, String logContent) {
        DppEtlTaskInstanceLogDO taskInstanceLog = DppEtlTaskInstanceLogDO.builder()
                .taskInstanceId(instance.getId())
                .tm(new Date())
                .taskType(instance.getTaskType() == null ? task.getType() : instance.getTaskType())
                .taskId(instance.getTaskId() == null ? task.getId() : instance.getTaskId())
                .taskCode(instance.getTaskCode() == null ? task.getCode() : instance.getTaskCode())
                .logContent(logContent)
                .validFlag(Boolean.TRUE)
                .delFlag(Boolean.FALSE)
                .build();
        iDppEtlTaskInstanceLogService.saveOrUpdate(taskInstanceLog);
    }

    /**
     * Appends local DataX text to the task log.
     *
     * @param taskLog current task instance log
     * @param text    text to append
     */
    private void appendLocalDataXLogText(StringBuilder taskLog, String text) {
        // Skip empty text to avoid unnecessary blank lines in the log.
        if (StringUtils.isEmpty(text)) {
            return;
        }
        taskLog.append(text);
        // Add a trailing line break to external text so logs remain line-oriented.
        if (!text.endsWith("\n") && !text.endsWith("\r")) {
            taskLog.append(System.lineSeparator());
        }
    }

    /**
     * DataX execution timing configuration.
     * <p>
     * Retains minute values for log display and millisecond values for actual waits.
     */
    static class DataXExecutionTiming {
        private final int maxAttempts;
        private final long retryTimes;
        private final long retryIntervalMinutes;
        private final long delayMinutes;
        private final long retryIntervalMillis;
        private final long delayMillis;

        /**
         * Creates a DataX execution timing configuration.
         *
         * @param maxAttempts          maximum execution attempts
         * @param retryTimes           failure retry count
         * @param retryIntervalMinutes failure retry interval in minutes
         * @param delayMinutes         execution delay in minutes
         * @param retryIntervalMillis  failure retry interval in milliseconds
         * @param delayMillis          execution delay in milliseconds
         */
        DataXExecutionTiming(int maxAttempts, long retryTimes, long retryIntervalMinutes, long delayMinutes,
                             long retryIntervalMillis, long delayMillis) {
            this.maxAttempts = maxAttempts;
            this.retryTimes = retryTimes;
            this.retryIntervalMinutes = retryIntervalMinutes;
            this.delayMinutes = delayMinutes;
            this.retryIntervalMillis = retryIntervalMillis;
            this.delayMillis = delayMillis;
        }

        /**
         * Returns the maximum execution attempts.
         *
         * @return the maximum execution attempts
         */
        int getMaxAttempts() {
            return maxAttempts;
        }

        /**
         * Returns the failure retry count.
         *
         * @return the failure retry count
         */
        long getRetryTimes() {
            return retryTimes;
        }

        /**
         * Returns the failure retry interval in minutes.
         *
         * @return the failure retry interval in minutes
         */
        long getRetryIntervalMinutes() {
            return retryIntervalMinutes;
        }

        /**
         * Returns the execution delay in minutes.
         *
         * @return the execution delay in minutes
         */
        long getDelayMinutes() {
            return delayMinutes;
        }

        /**
         * Returns the failure retry interval in milliseconds.
         *
         * @return the failure retry interval in milliseconds
         */
        long getRetryIntervalMillis() {
            return retryIntervalMillis;
        }

        /**
         * Returns the execution delay in milliseconds.
         *
         * @return the execution delay in milliseconds
         */
        long getDelayMillis() {
            return delayMillis;
        }
    }
}
