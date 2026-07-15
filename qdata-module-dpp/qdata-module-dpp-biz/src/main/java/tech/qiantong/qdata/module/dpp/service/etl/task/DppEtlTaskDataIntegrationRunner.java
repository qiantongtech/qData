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
 * 数据集成 DataX 本地任务执行器。
 * <p>
 * 负责将数据集成任务中的输入、去重、输出节点参数组装为 DataX JSON，
 * 并统一处理任务实例创建、延迟执行、失败重试、状态回写和执行日志保存。
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
     * 启动数据集成本地 DataX 任务。
     *
     * @param dppEtlTaskDO 数据集成任务详情
     */
    @Transactional
    public void startDppEtlTaskDataIntegration(DppEtlTaskDO dppEtlTaskDO, DppEtlTaskInstanceDO instance, StringBuilder taskLog) {
        Date startTime = new Date();

        try {
            // 查询这个任务保存下来的节点配置，里面包含数据源、表名、字段等 DataX 参数。
            List<DppEtlNodeRespVO> nodeList = iDppEtlNodeService.listNodeByTaskId(dppEtlTaskDO.getId());
            // 节点为空时无法生成 DataX 作业，直接按业务异常结束本次执行。
            if (CollectionUtils.isEmpty(nodeList)) {
                throw new ServiceException("本地DataX任务没有配置节点，请先保存任务！");
            }

            // 输入节点负责 reader 参数，输出节点负责 writer 参数。
            DppEtlNodeRespVO readerNode = FlinkxJson.findLocalDataXNode(nodeList, TaskComponentTypeEnum.DB_READER.getCode());
            DppEtlNodeRespVO deduplication = FlinkxJson.findLocalDataXNode(nodeList, TaskComponentTypeEnum.DATA_DEDUPLICATION.getCode());
            DppEtlNodeRespVO writerNode = FlinkxJson.findLocalDataXNode(nodeList, TaskComponentTypeEnum.DB_WRITER.getCode());
            // DataX 本地执行至少需要 reader 和 writer，去重节点为可选配置。
            if (readerNode == null || writerNode == null) {
                throw new ServiceException("本地DataX任务没有配置输入节点或输出节点，请先保存任务！");
            }

            Map<String, Object> readerNodeJsonMap = Collections.emptyMap();
            // reader 节点存在时解析输入端参数，避免空参数影响 JSON 构建。
            if (ObjectUtils.isNotEmpty(readerNode)) {
                readerNodeJsonMap = JSONUtils.convertTaskDefinitionJsonMap(readerNode.getParameters());
            }
            Map<String, Object> writerNodeJsonMap = Collections.emptyMap();
            // writer 节点存在时解析输出端参数，避免空参数影响 JSON 构建。
            if (ObjectUtils.isNotEmpty(writerNode)) {
                writerNodeJsonMap = JSONUtils.convertTaskDefinitionJsonMap(writerNode.getParameters());
            }
            Map<String, Object> definitionJsonMap = Collections.emptyMap();
            // 去重节点为可选节点，仅在配置存在时参与 DataX JSON 构建。
            if (ObjectUtils.isNotEmpty(deduplication)) {
                String deduplicationParameters = deduplication.getParameters();
                definitionJsonMap = JSONUtils.convertTaskDefinitionJsonMap(deduplicationParameters);
            }

            // 生成 DataX JSON
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
            // DataX 返回非成功状态时，统一抛出异常交给失败分支回写实例状态。
            if (!run.isSuccess()) {
                throw new ServiceException("DataX任务执行失败，exitCode=" + run.getExitCode());
            }
            markLocalDataXTaskSuccess(instance);
            LogUtils.appendLocalLogLine(taskLog, "DataX task executed successfully");
        } catch (Exception e) {
            // 任意异常都标记任务失败，并将失败原因写入本地执行日志。
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
     * 按任务配置执行 DataX，并在失败时按配置次数和间隔进行重试。
     *
     * @param json    DataX 作业 JSON
     * @param timing  执行延迟和失败重试配置
     * @param taskLog 本次任务实例日志
     * @return DataX 执行结果
     * @throws Exception DataX 执行失败或等待过程被中断
     */
    private DataXResult executeDataXJobWithRetry(String json, DataXExecutionTiming timing, StringBuilder taskLog) throws Exception {
        sleepBeforeDataXExecution(timing.getDelayMillis(), taskLog, "Delay before DataX execution");

        Exception lastException = null;
        // 最大执行次数 = 首次执行 + 失败重试次数。
        for (int attempt = 1; attempt <= timing.getMaxAttempts(); attempt++) {
            LogUtils.appendLocalLogLine(taskLog, String.format("Start DataX execution attempt %d/%d", attempt, timing.getMaxAttempts()));
            try {
                DataXResult result = dataXExecutor.run(json);
                // 执行成功时立即返回，避免继续进入后续重试流程。
                if (result.isSuccess()) {
                    // 非首次成功说明本次任务经历过重试，补充重试成功日志。
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

            // 当前已经是最后一次执行时，抛出最后一次失败原因。
            if (attempt >= timing.getMaxAttempts()) {
                LogUtils.appendLocalLogLine(taskLog, String.format("DataX execution failed after %d attempt(s)", timing.getMaxAttempts()));
                throw lastException;
            }
            // 未达到最大次数时，按配置的重试间隔等待后继续下一次执行。
            LogUtils.appendLocalLogLine(taskLog, String.format("DataX execution attempt %d/%d failed, will retry after %d minutes",
                    attempt, timing.getMaxAttempts(), timing.getRetryIntervalMinutes()));
            sleepBeforeDataXExecution(timing.getRetryIntervalMillis(), taskLog, "Wait before DataX retry");
        }

        throw new ServiceException("DataX任务执行失败");
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
