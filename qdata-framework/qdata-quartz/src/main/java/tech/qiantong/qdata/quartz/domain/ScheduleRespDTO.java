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
 * Business License: https://docs.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.quartz.domain;

import lombok.Builder;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;
import tech.qiantong.qdata.common.constant.ScheduleConstants;

/**
 * Common response data for an updated scheduled task.
 * Includes quartzId to identify the existing Quartz task.
 */
@Data
@Builder
public class ScheduleRespDTO {
    // Updating a Quartz task requires the task ID from sys_job.
    private Long quartzId;
    private String remark;

    /** Task ID */
    @Excel(name = "任务序号", cellType = Excel.ColumnType.NUMERIC)
    private Long jobId;

    /** Task name */
    @Excel(name = "任务名称")
    private String jobName;

    /** Task group name */
    @Excel(name = "任务组名")
    private String jobGroup;

    /** Invocation target string */
    @Excel(name = "调用目标字符串")
    private String invokeTarget;

    /** Cron expression */
    @Excel(name = "执行表达式 ")
    private String cronExpression;

    /** Cron scheduling policy */
    @Excel(name = "计划策略 ", readConverterExp = "0=默认,1=立即触发执行,2=触发一次执行,3=不触发立即执行")
    private String misfirePolicy = ScheduleConstants.MISFIRE_DEFAULT;

    /** Whether concurrent execution is allowed (0: allowed, 1: prohibited) */
    @Excel(name = "并发执行", readConverterExp = "0=允许,1=禁止")
    private String concurrent;

    /** Execution strategy (PARALLEL, SERIAL_WAIT, SERIAL_DISCARD, or SERIAL_PRIORITY) */
    @Excel(name = "执行策略")
    private String executionType;

    /** Task status (0: active, 1: paused) */
    @Excel(name = "任务状态", readConverterExp = "0=正常,1=暂停")
    private String status;
}
