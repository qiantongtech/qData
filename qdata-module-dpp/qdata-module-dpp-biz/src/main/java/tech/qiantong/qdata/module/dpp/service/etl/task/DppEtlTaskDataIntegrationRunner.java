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

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.enums.CommandType;
import tech.qiantong.qdata.common.enums.TaskComponentTypeEnum;
import tech.qiantong.qdata.common.enums.WorkflowExecutionStatus;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.JSONUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.uuid.IdUtils;
import tech.qiantong.qdata.datax.DataXExecutor;
import tech.qiantong.qdata.datax.DataXJsonBuilder;
import tech.qiantong.qdata.datax.DataXResult;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeRespVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskDO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskInstanceDO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskInstanceLogDO;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlNodeService;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlTaskInstanceLogService;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlTaskInstanceService;
import tech.qiantong.qdata.module.dpp.utils.datax.FlinkxJson;
import tech.qiantong.qdata.module.dpp.utils.log.LogUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Handle DataX task configuration and execution.
 * <p>
 * Handle DataX task configuration and execution.
 * Handle task-related data and operations.
 *
 * @author qdata
 */
@Service
public class DppEtlTaskDataIntegrationRunner {
    @Resource
    private IDppEtlNodeService iDppEtlNodeService;
    @Resource
    private IDppEtlTaskInstanceService dppEtlTaskInstanceService;
    @Resource
    private IDppEtlTaskInstanceLogService iDppEtlTaskInstanceLogService;
    @Resource
    private DataXExecutor dataXExecutor;

    /**
     * Handle DataX task configuration and execution.
     *
     * @param dppEtlTaskDO parameter value
     */
    @Transactional
    public void startDppEtlTaskDataIntegration(DppEtlTaskDO dppEtlTaskDO, DppEtlTaskInstanceDO instance, StringBuilder taskLog) {
        Date startTime = new Date();

        try {
            // Handle DataX task configuration and execution.
            List<DppEtlNodeRespVO> nodeList = iDppEtlNodeService.listNodeByTaskId(dppEtlTaskDO.getId());
            // Handle DataX task configuration and execution.
            if (CollectionUtils.isEmpty(nodeList)) {
                throw new ServiceException("本地DataX任务没有配置节点，请先保存任务！");
            }

            // Handle node-related data and operations.
            DppEtlNodeRespVO readerNode = FlinkxJson.findLocalDataXNode(nodeList, TaskComponentTypeEnum.DB_READER.getCode());
            DppEtlNodeRespVO deduplication = FlinkxJson.findLocalDataXNode(nodeList, TaskComponentTypeEnum.DATA_DEDUPLICATION.getCode());
            DppEtlNodeRespVO writerNode = FlinkxJson.findLocalDataXNode(nodeList, TaskComponentTypeEnum.DB_WRITER.getCode());
            // Handle DataX task configuration and execution.
            if (readerNode == null || writerNode == null) {
                throw new ServiceException("本地DataX任务没有配置输入节点或输出节点，请先保存任务！");
            }

            Map<String, Object> readerNodeJsonMap = Collections.emptyMap();
            // Handle JSON data for this operation.
            if (ObjectUtils.isNotEmpty(readerNode)) {
                readerNodeJsonMap = JSONUtils.convertTaskDefinitionJsonMap(readerNode.getParameters());
            }
            Map<String, Object> writerNodeJsonMap = Collections.emptyMap();
            // Handle JSON data for this operation.
            if (ObjectUtils.isNotEmpty(writerNode)) {
                writerNodeJsonMap = JSONUtils.convertTaskDefinitionJsonMap(writerNode.getParameters());
            }
            Map<String, Object> definitionJsonMap = Collections.emptyMap();
            // Handle DataX task configuration and execution.
            if (ObjectUtils.isNotEmpty(deduplication)) {
                String deduplicationParameters = deduplication.getParameters();
                definitionJsonMap = JSONUtils.convertTaskDefinitionJsonMap(deduplicationParameters);
            }

            // Handle DataX task configuration and execution.
            String json = DataXJsonBuilder.buildJson(readerNodeJsonMap, writerNodeJsonMap, definitionJsonMap);
            LogUtils.appendLocalLogLine(taskLog, "DataX JSON: " + json);

            LogUtils.appendLocalLogLine(taskLog, "********************************* Execute DataX task instance ********************************");
            LogUtils.appendLocalLogLine(taskLog, "Start executing DataX job");

            DataXExecutionTiming timing = buildDataXExecutionTiming(dppEtlTaskDO.getDraftJson());
            LogUtils.appendLocalLogLine(taskLog, String.format("DataX execution timing: delay=%d minutes, retryTimes=%d, retryInterval=%d minutes",
                    timing.getDelayMinutes(), timing.getRetryTimes(), timing.getRetryIntervalMinutes()));

            DataXResult run = executeDataXJobWithRetry(json, timing, taskLog);
            LogUtils.appendLocalLogLine(taskLog, "DataX exitCode: " + run.getExitCode());
            LogUtils.appendLocalLogLine(taskLog, "DataX output:" + run.getOutput());
            LogUtils.appendLocalLogLine(taskLog, "*********************************** Execute DataX task end *************************************");
            // Handle DataX task configuration and execution.
            if (!run.isSuccess()) {
                throw new ServiceException("DataX任务执行失败，exitCode=" + run.getExitCode());
            }
            markLocalDataXTaskSuccess(instance);
            LogUtils.appendLocalLogLine(taskLog, "DataX task executed successfully");
        } catch (Exception e) {
            // Handle task-related data and operations.
            markLocalDataXTaskFail(instance, e);
            LogUtils.appendLocalLogLine(taskLog, "DataX task execution failed: " + JSONUtils.formatJson(JSONUtils.toJson(e.getMessage())));
        } finally {
            // Handle task-related data and operations.
            long duration = (new Date().getTime() - startTime.getTime()) / 1000L;
            LogUtils.appendLocalLogLine(taskLog, "DataX task execution finished, duration: " + duration + " seconds");
            saveLocalDataXTaskInstanceLog(instance, dppEtlTaskDO, taskLog.toString());
        }
    }

    /**
     * Handle DataX task configuration and execution.
     *
     * @param json parameter value
     * @param timing parameter value
     * @param taskLog parameter value
     * @return the operation result
     * @throws Exception when the operation fails
     */
    private DataXResult executeDataXJobWithRetry(String json, DataXExecutionTiming timing, StringBuilder taskLog) throws Exception {
        sleepBeforeDataXExecution(timing.getDelayMillis(), taskLog, "Delay before DataX execution");

        Exception lastException = null;
        // Implementation details.
        for (int attempt = 1; attempt <= timing.getMaxAttempts(); attempt++) {
            LogUtils.appendLocalLogLine(taskLog, String.format("Start DataX execution attempt %d/%d", attempt, timing.getMaxAttempts()));
            try {
                DataXResult result = dataXExecutor.run(json);
                // Return the operation result.
                if (result.isSuccess()) {
                    // Handle task-related data and operations.
                    if (attempt > 1) {
                        LogUtils.appendLocalLogLine(taskLog, String.format("DataX execution retry succeeded on attempt %d/%d",
                                attempt, timing.getMaxAttempts()));
                    }
                    return result;
                }
                lastException = new ServiceException("DataX任务执行失败，exitCode=" + result.getExitCode());
                LogUtils.appendLocalLogLine(taskLog, "DataX exitCode: " + result.getExitCode());
                LogUtils.appendLocalLogLine(taskLog, "DataX output:" + result.getOutput());
            } catch (Exception e) {
                lastException = e;
                LogUtils.appendLocalLogLine(taskLog, "DataX execution attempt exception: " + JSONUtils.formatJson(JSONUtils.toJson(e.getMessage())));
            }

            // Implementation details.
            if (attempt >= timing.getMaxAttempts()) {
                LogUtils.appendLocalLogLine(taskLog, String.format("DataX execution failed after %d attempt(s)", timing.getMaxAttempts()));
                throw lastException;
            }
            // Implementation details.
            LogUtils.appendLocalLogLine(taskLog, String.format("DataX execution attempt %d/%d failed, will retry after %d minutes",
                    attempt, timing.getMaxAttempts(), timing.getRetryIntervalMinutes()));
            sleepBeforeDataXExecution(timing.getRetryIntervalMillis(), taskLog, "Wait before DataX retry");
        }

        throw new ServiceException("DataX任务执行失败");
    }

    /**
     * Handle DataX task configuration and execution.
     *
     * @param draftJsonText parameter value
     * @return the operation result
     */
    static DataXExecutionTiming buildDataXExecutionTiming(String draftJsonText) {
        Map<String, Object> draftJson = Collections.emptyMap();
        // Implementation details.
        if (StringUtils.isNotEmpty(draftJsonText)) {
            Map<String, Object> parsedDraftJson = JSONUtils.convertTaskDefinitionJsonMap(draftJsonText);
            // Handle JSON data for this operation.
            if (parsedDraftJson != null) {
                draftJson = parsedDraftJson;
            }
        }
        long failRetryTimes = normalizeNonNegative(toLong(draftJson.get("failRetryTimes")));
        long failRetryInterval = normalizeNonNegative(toLong(draftJson.get("failRetryInterval")));
        long delayTime = normalizeNonNegative(toLong(draftJson.get("delayTime")));
        long maxAttempts = failRetryTimes + 1;
        // Implementation details.
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
     * Implementation details.
     *
     * @param value parameter value
     * @return the operation result
     */
    private static Long toLong(Object value) {
        // Implementation details.
        if (value == null) {
            return null;
        }
        // Maintain compatibility with existing data and configurations.
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        try {
            return Long.parseLong(value.toString());
        } catch (NumberFormatException e) {
            // Implementation details.
            return null;
        }
    }

    /**
     * Implementation details.
     *
     * @param value parameter value
     * @return the operation result
     */
    private static long normalizeNonNegative(Long value) {
        // Implementation details.
        if (value == null || value < 0) {
            return 0L;
        }
        return value;
    }

    /**
     * Handle DataX task configuration and execution.
     *
     * @param millis parameter value
     * @param taskLog parameter value
     * @param action parameter value
     * @throws InterruptedException when the operation fails
     */
    private void sleepBeforeDataXExecution(long millis, StringBuilder taskLog, String action) throws InterruptedException {
        // Implementation details.
        if (millis <= 0) {
            return;
        }
        LogUtils.appendLocalLogLine(taskLog, String.format("%s, wait %d minutes", action, TimeUnit.MILLISECONDS.toMinutes(millis)));
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // Handle scheduling configuration and operations.
            Thread.currentThread().interrupt();
            LogUtils.appendLocalLogLine(taskLog, action + " interrupted");
            throw e;
        }
    }

    /**
     * Handle DataX task configuration and execution.
     *
     * @param task parameter value
     * @return the operation result
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public DppEtlTaskInstanceDO createLocalDataXTaskInstance(DppEtlTaskDO task) {
        // Handle DataX task configuration and execution.
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
     * Handle DataX task configuration and execution.
     *
     * @param instance parameter value
     */
    private void markLocalDataXTaskSuccess(DppEtlTaskInstanceDO instance) {
        instance.setStatus(String.valueOf(WorkflowExecutionStatus.SUCCESS.getCode()));
        instance.setEndTime(new Date());
        dppEtlTaskInstanceService.updateById(instance);
    }

    /**
     * Handle DataX task configuration and execution.
     *
     * @param instance parameter value
     * @param e parameter value
     */
    private void markLocalDataXTaskFail(DppEtlTaskInstanceDO instance, Exception e) {
        instance.setStatus(String.valueOf(WorkflowExecutionStatus.FAILURE.getCode()));
        instance.setEndTime(new Date());
        instance.setRemark(e.getMessage());
        dppEtlTaskInstanceService.updateById(instance);
    }

    /**
     * Handle DataX task configuration and execution.
     *
     * @param instance parameter value
     * @param task parameter value
     * @param logContent parameter value
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
     * Handle DataX task configuration and execution.
     *
     * @param taskLog parameter value
     * @param text parameter value
     */
    private void appendLocalDataXLogText(StringBuilder taskLog, String text) {
        // Handle execution logging.
        if (StringUtils.isEmpty(text)) {
            return;
        }
        taskLog.append(text);
        // Handle execution logging.
        if (!text.endsWith("\n") && !text.endsWith("\r")) {
            taskLog.append(System.lineSeparator());
        }
    }

    /**
     * Handle DataX task configuration and execution.
     * <p>
     * Handle execution logging.
     */
    static class DataXExecutionTiming {
        private final int maxAttempts;
        private final long retryTimes;
        private final long retryIntervalMinutes;
        private final long delayMinutes;
        private final long retryIntervalMillis;
        private final long delayMillis;

        /**
         * Handle DataX task configuration and execution.
         *
         * @param maxAttempts parameter value
         * @param retryTimes parameter value
         * @param retryIntervalMinutes parameter value
         * @param delayMinutes parameter value
         * @param retryIntervalMillis parameter value
         * @param delayMillis parameter value
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
         * Retrieve the required data.
         *
         * @return the operation result
         */
        int getMaxAttempts() {
            return maxAttempts;
        }

        /**
         * Retrieve the required data.
         *
         * @return the operation result
         */
        long getRetryTimes() {
            return retryTimes;
        }

        /**
         * Retrieve the required data.
         *
         * @return the operation result
         */
        long getRetryIntervalMinutes() {
            return retryIntervalMinutes;
        }

        /**
         * Retrieve the required data.
         *
         * @return the operation result
         */
        long getDelayMinutes() {
            return delayMinutes;
        }

        /**
         * Retrieve the required data.
         *
         * @return the operation result
         */
        long getRetryIntervalMillis() {
            return retryIntervalMillis;
        }

        /**
         * Retrieve the required data.
         *
         * @return the operation result
         */
        long getDelayMillis() {
            return delayMillis;
        }
    }
}
