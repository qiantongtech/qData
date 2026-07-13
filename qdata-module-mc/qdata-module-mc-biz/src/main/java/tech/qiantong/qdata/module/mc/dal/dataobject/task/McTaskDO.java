package tech.qiantong.qdata.module.mc.dal.dataobject.task;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * Collection task DO object MC_TASK
 *
 * @author qdata
 * @date 2025-12-16
 */
@Data
@TableName(value = "MC_TASK")
// Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you do not need to write it.
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

    /** Source system ID */
    private Long sourceSystemId;

    /** Source system name */
    private String sourceSystemName;

    /** Task name */
    private String name;

    /** Data connection id */
    private Long datasourceId;

    /** Database type */
    private String dbType;

    /** Responsible person */
    private Long leader;

    /** Responsible person’s phone number */
    private String leaderPhone;

    /** Collection mode */
    private String collectionMode;

    /** Collection range */
    private String collectionScope;

    /** Task status */
    private String status;

    /** Is it valid */
    private Boolean validFlag;

    /** Delete flag */
    @TableLogic
    private Boolean delFlag;

    /** Description */
    private String description;

    /**
     * Collection task type: 1-Collection, 2-DDL
     */
    @Schema(description = "采集任务类型：1-采集，2-DDL", example = "1")
    private String collectType;

    /**
     * Collection blacklist
     */
    @Schema(description = "采集黑名单", example = "")
    private String blacklist;


    /**
     * cron expression
     */
    @TableField(exist = false)
    private String cronExpression;

    /**
     * Scheduling status
     */
    @TableField(exist = false)
    private String schedulerStatus;

    /**
     * Data source name
     */
    @TableField(exist = false)
    private String datasourceName;


    /**
     * Data source type
     */
    @TableField(exist = false)
    private String datasourceType;

    /**
     * Contact name
     */
    @TableField(exist = false)
    private String personChargeName;

    /**
     * Last execution time
     */
    @TableField(exist = false)
    private String lastExecuteTime;

    /**
     * Contact phone number
     */
    @TableField(exist = false)
    private String createPhoneNumber;

    /**
     * Responsible department
     */
    private Long responsibleDept;
}
