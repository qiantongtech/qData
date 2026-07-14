package tech.qiantong.qdata.quartz.domain;

import lombok.Builder;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;
import tech.qiantong.qdata.common.constant.ScheduleConstants;

/**
 * Handle task-related data and operations.
 * Handle Quartz scheduling operations.
 */
@Data
@Builder
public class ScheduleRespDTO {
    // Handle Quartz scheduling operations.
    private Long quartzId;
    private String remark;

    /** Task ID */
    @Excel(name = "任务序号", cellType = Excel.ColumnType.NUMERIC)
    private Long jobId;

    /** Handle task-related data and operations. */
    @Excel(name = "任务名称")
    private String jobName;

    /** Handle task-related data and operations. */
    @Excel(name = "任务组名")
    private String jobGroup;

    /** Implementation details. */
    @Excel(name = "调用目标字符串")
    private String invokeTarget;

    /** Implementation details. */
    @Excel(name = "执行表达式 ")
    private String cronExpression;

    /** Implementation details. */
    @Excel(name = "计划策略 ", readConverterExp = "0=默认,1=立即触发执行,2=触发一次执行,3=不触发立即执行")
    private String misfirePolicy = ScheduleConstants.MISFIRE_DEFAULT;

    /** Implementation details. */
    @Excel(name = "并发执行", readConverterExp = "0=允许,1=禁止")
    private String concurrent;

    /** Implementation details. */
    @Excel(name = "执行策略")
    private String executionType;

    /** Handle task-related data and operations. */
    @Excel(name = "任务状态", readConverterExp = "0=正常,1=暂停")
    private String status;
}
