package tech.qiantong.qdata.module.mc.dal.dataobject.task;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * Handle task-related data and operations.
 *
 * @author qdata
 * @date 2025-12-16
 */
@Data
@TableName(value = "MC_TASK")
// Handle JDBC SQL execution.
// @KeySequence("MC_TASK_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class McTaskDO extends BaseEntity {
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
    private String name;

    /** Implementation details. */
    private Long datasourceId;

    /** Handle database and data source configuration. */
    private String dbType;

    /** Implementation details. */
    private Long leader;

    /** Implementation details. */
    private String leaderPhone;

    /** Implementation details. */
    private String collectionMode;

    /** Implementation details. */
    private String collectionScope;

    /** Handle task-related data and operations. */
    private String status;

    /** Scheduler */
    private String scheduler;

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
    @Schema(description = "采集任务类型：1-采集，2-DDL", example = "1")
    private String collectType;

    /**
     * Implementation details.
     */
    @Schema(description = "采集黑名单", example = "")
    private String blacklist;


    /**
     * Implementation details.
     */
    @TableField(exist = false)
    private String cronExpression;

    /**
     * Handle scheduling configuration and operations.
     */
    @TableField(exist = false)
    private String schedulerStatus;

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

    /**
     * Implementation details.
     */
    @TableField(exist = false)
    private String personChargeName;

    /**
     * Implementation details.
     */
    @TableField(exist = false)
    private String lastExecuteTime;

    /**
     * Implementation details.
     */
    @TableField(exist = false)
    private String createPhoneNumber;

    /**
     * Implementation details.
     */
    private Long responsibleDept;
}
