package tech.qiantong.qdata.quartz.domain;

import lombok.Builder;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;
import tech.qiantong.qdata.common.constant.ScheduleConstants;

/**
 * 更新调度任务的通用入参。
 * 比创建多一个 quartzId，用来定位已经创建好的 Quartz 任务。
 */
@Data
@Builder
public class ScheduleRespDTO {
    // 更新 Quartz 任务时必须带上 sys_job 的任务 id。
    private Long quartzId;
    private String remark;

    /** 任务ID */
    @Excel(name = "任务序号", cellType = Excel.ColumnType.NUMERIC)
    private Long jobId;

    /** 任务名称 */
    @Excel(name = "任务名称")
    private String jobName;

    /** 任务组名 */
    @Excel(name = "任务组名")
    private String jobGroup;

    /** 调用目标字符串 */
    @Excel(name = "调用目标字符串")
    private String invokeTarget;

    /** cron执行表达式 */
    @Excel(name = "执行表达式 ")
    private String cronExpression;

    /** cron计划策略 */
    @Excel(name = "计划策略 ", readConverterExp = "0=默认,1=立即触发执行,2=触发一次执行,3=不触发立即执行")
    private String misfirePolicy = ScheduleConstants.MISFIRE_DEFAULT;

    /** 是否并发执行（0允许 1禁止） */
    @Excel(name = "并发执行", readConverterExp = "0=允许,1=禁止")
    private String concurrent;

    /** 执行策略（PARALLEL、SERIAL_WAIT、SERIAL_DISCARD、SERIAL_PRIORITY） */
    @Excel(name = "执行策略")
    private String executionType;

    /** 任务状态（0正常 1暂停） */
    @Excel(name = "任务状态", readConverterExp = "0=正常,1=暂停")
    private String status;
}
