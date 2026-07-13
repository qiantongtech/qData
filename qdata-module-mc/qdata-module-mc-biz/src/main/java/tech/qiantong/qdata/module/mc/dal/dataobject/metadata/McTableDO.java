package tech.qiantong.qdata.module.mc.dal.dataobject.metadata;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * Metadata information DO object MC_TABLE
 *
 * @author qdata
 * @date 2026-02-11
 */
@Data
@TableName(value = "MC_TABLE")
// Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you do not need to write it.
// @KeySequence("MC_TABLE_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class McTableDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** Collection task id; reserved field, not used temporarily */
    private Long taskId;

    /** Database ID */
    private Long dbId;

    /** Data source id; redundant field */
    private Long datasourceId;

    /** version */
    private Integer version;

    /** Table name (table English name) */
    private String tableName;

    /** Table comment/table description (table Chinese name) */
    private String tableComment;

    /** Security level id */
    private Long safetyLevelId;

    /** Database name */
    private String dbName;

    /** Pattern name; can be empty */
    private String schemaName;

    /** Storage type */
    private String storageType;

    /** Storage size */
    private Integer storageSize;

    /** Business Responsible Person */
    private Long businessLeader;

    /** Phone number of business person in charge */
    private String businessLeaderPhone;

    /** Technical Responsible Person */
    private Long techLeader;

    /** Telephone number of technical person in charge */
    private String techLeaderPhone;

    /** Whether it is the main table; 0: no, 1: yes */
    private String masterFlag;

    /** Whether it is a temporary table; 0: no, 1: yes */
    private String tempFlag;

    /** Data quality */
    private Integer dataQuality;

    /** Review status; 1: Approval in progress, 2: Approval passed, 3: Approval rejected, 4: Approval withdrawn, 5: Approval abnormal */
    private String auditStatus;

    /** Review time */
    private Date auditTime;

    /** Status; 0: not released, 1: released */
    private String status;

    /** Whether it is valid; 0: invalid, 1: valid */
    private Boolean validFlag;

    /** Deletion flag; 1: deleted, 0: not deleted */
    @TableLogic
    private Boolean delFlag;

    /** Description */
    private String description;

    /**
     * Whether to display on the portal: 0-not displayed, 1-displayed
     */
    @Schema(description = "是否在门户展示：0-不展示，1-展示", example = "0")
    private String portalVisible;

    @TableField(exist = false)
    private Long sourceSystemId;

    @TableField(exist = false)
    private String sourceSystemName;

    /**
     * Name of person responsible for business
     */
    @TableField(exist = false)
    private String businessLeaderName;

    /**
     * Name of technical person in charge
     */
    @TableField(exist = false)
    private String techLeaderName;

    /**
     * Security level name
     */
    @TableField(exist = false)
    private String safetyLevelName;


    /** cron expression */
    @TableField(exist = false)
    private String cronExpression;

    /** Number of fields */
    private Long columnCount;

    /** Index */
    private String tbIndex;

    /** Number of lines */
    private Long rowCount;

    /** Partition field */
    private String partitionKey;
    /**
     * Storage engine
     */
    private String storageEngine;

    /**
     * Responsible department
     */
    private Long responsibleDept;

    /**
     * Primary key field
     */
    private String primaryKey;

    /**
     * Table creation time
     */
    private Date tbCreateTime;

    /**
     * Data update time
     */
    private Date dataUpdateTime;

    /**
     * Whether assets have been associated
     */
    @TableField(exist = false)
    private Boolean dssetFlag;

    /**
     * Creator phone number
     */
    @TableField(exist = false)
    private String createPhoneNumber;

    /**
     * Updater’s phone number
     */
    @TableField(exist = false)
    private String updatePhoneNumber;

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
}
