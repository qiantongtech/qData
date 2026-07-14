package tech.qiantong.qdata.module.mc.api.task.dto;

import lombok.Data;

import java.util.Date;

/**
 * Handle scheduling configuration and operations.
 *
 * @author qdata
 * @date 2025-12-16
 */
@Data
public class McTaskSchedulerRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Task ID */
    private Long taskId;

    /** Handle scheduling configuration and operations. */
    private String jobId;

    /** Scheduling engine */
    private String taskScheduler;

    /** Handle Quartz scheduling operations. */
    private Long quartzId;

    /** Implementation details. */
    private Date startTime;

    /** Implementation details. */
    private Date endTime;

    /** Implementation details. */
    private String timezoneId;

    /** Implementation details. */
    private String cronExpression;

    /** Implementation details. */
    private String failureStrategy;

    /** Handle scheduling configuration and operations. */
    private String status;

    /** Whether the record is valid. */
    private Boolean validFlag;

    /** Delete the related record. */
    private Boolean delFlag;


}
