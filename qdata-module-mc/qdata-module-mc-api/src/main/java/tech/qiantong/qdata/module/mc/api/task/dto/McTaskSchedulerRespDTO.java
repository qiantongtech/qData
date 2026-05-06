package tech.qiantong.qdata.module.mc.api.task.dto;

import lombok.Data;

import java.util.Date;

/**
 * 数据集成调度信息 DTO 对象 MC_TASK_SCHEDULER
 *
 * @author qdata
 * @date 2025-12-16
 */
@Data
public class McTaskSchedulerRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 任务id */
    private Long taskId;

    /** xxlJob_ID */
    private String jobId;

    /** 开始时间 */
    private Date startTime;

    /** 结束时间 */
    private Date endTime;

    /** 时区 */
    private String timezoneId;

    /** cron表达式 */
    private String cronExpression;

    /** 失败策略 */
    private String failureStrategy;

    /** 调度状态 */
    private String status;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
