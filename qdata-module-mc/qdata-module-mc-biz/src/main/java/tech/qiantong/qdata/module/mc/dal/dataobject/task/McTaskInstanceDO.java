package tech.qiantong.qdata.module.mc.dal.dataobject.task;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * Handle task-related data and operations.
 *
 * @author qdata
 * @date 2025-12-16
 */
@Data
@TableName(value = "MC_TASK_INSTANCE")
// Handle JDBC SQL execution.
// @KeySequence("MC_TASK_INSTANCE_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class McTaskInstanceDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** Implementation details. */
    private Long sourceSystemId;

    /** Implementation details. */
    private String sourceSystemName;

    /** Handle task-related data and operations. */
    private Long taskId;

    /** Implementation details. */
    private String collectionMode;

    /** Implementation details. */
    private String collectionScope;

    /** Implementation details. */
    private Long totalCount;

    /** Implementation details. */
    private Long successCount;

    /** Implementation details. */
    private Long failCount;

    /** Implementation details. */
    private String failCause;

    /** Implementation details. */
    private Long addCount;

    /** Implementation details. */
    private Long delCount;

    /** Implementation details. */
    private Long updateCount;

    /** Implementation details. */
    private Date startTime;

    /** Implementation details. */
    private Date endTime;

    /** Implementation details. */
    private Long duration;

    /** Implementation details. */
    private String status;

    /** Scheduling engine */
    private String taskScheduler;

    /** Handle Quartz scheduling operations. */
    private Long quartzId;

    /** Whether the record is valid. */
    private Boolean validFlag;

    /** Delete the related record. */
    @TableLogic
    private Boolean delFlag;

    /** Implementation details. */
    private String description;

    /**
     * Handle task-related data and operations.
     */
    @TableField(exist = false)
    private String name;
    /**
     * Handle task-related data and operations.
     */
    @TableField(exist = false)
    private String taskStatus;

    /**
     * Create the required record.
     */
    @TableField(exist = false)
    private String createPhoneNumber;

    /**
     * Handle database and data source configuration.
     */
    @TableField(exist = false)
    private String datasourceName;


    /**
     * Handle database and data source configuration.
     */
    @TableField(exist = false)
    private String datasourceType;
}
