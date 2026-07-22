package tech.qiantong.qdata.module.mc.dal.dataobject.task;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * Data integration scheduling information DO object MC_TASK_SCHEDULER
 *
 * @author qdata
 * @date 2025-12-16
 */
@Data
@TableName(value = "MC_TASK_SCHEDULER")
// Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you do not need to write it.
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

    /** task id */
    private Long taskId;

    /** Scheduler ID */
    private String jobId;

    /** DolphinScheduler task encoding (for API calls) */
    private String taskCode;

    /** 调度引擎 */
    private String scheduler;

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
    @TableLogic
    private Boolean delFlag;


}
