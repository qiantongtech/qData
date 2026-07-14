package tech.qiantong.qdata.module.mc.dal.dataobject.task;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * Handle scheduling configuration and operations.
 *
 * @author qdata
 * @date 2025-12-16
 */
@Data
@TableName(value = "MC_TASK_SCHEDULER")
// Handle JDBC SQL execution.
// @KeySequence("MC_TASK_SCHEDULER_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class McTaskSchedulerDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** Task ID */
    private Long taskId;

    /** Handle scheduling configuration and operations. */
    private String jobId;

    /** Handle DolphinScheduler operations. */
    private String taskCode;

    /** Scheduling engine */
    private String taskScheduler;

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
    @TableLogic
    private Boolean delFlag;


}
