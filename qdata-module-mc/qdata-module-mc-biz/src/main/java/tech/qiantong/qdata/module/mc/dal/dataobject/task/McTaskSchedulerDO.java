package tech.qiantong.qdata.module.mc.dal.dataobject.task;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 数据集成调度信息 DO 对象 MC_TASK_SCHEDULER
 *
 * @author qdata
 * @date 2025-12-16
 */
@Data
@TableName(value = "MC_TASK_SCHEDULER")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
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

    /** 任务id */
    private Long taskId;

    /** 调度器ID */
    private String jobId;

    /** DolphinScheduler任务编码（用于API调用） */
    private String taskCode;

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
    @TableLogic
    private Boolean delFlag;


}
