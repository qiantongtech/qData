package tech.qiantong.qdata.module.mc.api.task.dto;

import lombok.Data;

import java.util.Date;

/**
 * Data integration scheduling information DTO object MC_TASK_SCHEDULER
 *
 * @author qdata
 * @date 2025-12-16
 */
@Data
public class McTaskSchedulerRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** task id */
    private Long taskId;

    /** scheduler id */
    private String jobId;

    /** 调度引擎 */
    private String taskScheduler;

    /** Quartz调度任务id */
    private Long quartzId;

    /** 开始时间 */
    private Date startTime;

    /** End time */
    private Date endTime;

    /** Time zone */
    private String timezoneId;

    /** cron expression */
    private String cronExpression;

    /** Failure strategy */
    private String failureStrategy;

    /** Scheduling status */
    private String status;

    /** Is it valid */
    private Boolean validFlag;

    /** Delete flag */
    private Boolean delFlag;


}
