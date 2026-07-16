package tech.qiantong.qdata.module.mc.dal.dataobject.metadata;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * Metadata field information DO object MC_COLUMN
 *
 * @author qdata
 * @date 2026-02-11
 */
@Data
@TableName(value = "MC_COLUMN")
// Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you do not need to write it.
// @KeySequence("MC_COLUMN_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class McColumnDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** Collection task id; reserved field, not used temporarily */
    private Long taskId;

    /** Database ID */
    private Long dbId;

    /** Table information id */
    private Long tableId;

    /** Data source id; redundant field */
    private Long datasourceId;

    /** version */
    private Integer version;

    /** Security level id */
    private Long safetyLevelId;

    /** Data element id */
    private Long dataElemId;

    /** Field name */
    private String columnName;

    /** Field comments */
    private String columnComment;

    /** Field type */
    private String columnType;

    /** Data length */
    private Integer columnLength;

    /** Data accuracy */
    private Integer columnPrecision;

    /** Data decimal places */
    private Integer columnScale;

    /** Data default value */
    private String defaultValue;

    /** Whether it is the primary key; 0: No 1: Yes */
    private String pkFlag;

    /** Whether it is a foreign key; 0: No 1: Yes */
    private String fkFlag;

    /** Whether it is nullable; 0: No 1: Yes */
    private String nullableFlag;

    /** Business definition */
    private String businessDefinition;

    /** Unit of measurement */
    private String measuringUnit;

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
    private String dataElemName;

    @TableField(exist = false)
    private String dbName;

    @TableField(exist = false)
    private String tableName;

    /**
     * Security level name
     */
    @TableField(exist = false)
    private String safetyLevelName;

    @TableField(exist = false)
    private Long mcTableLogId;

    //Field changes
    @TableField(exist = false)
    private String updateMsg;

    //Field change type - Numeric code represents different change types: 1-Field comment change, 2-Field type change, 3-Field length change, 4-Field precision change, 5-Field decimal place change, 6-Field default value change, 7-Primary key identification change, 8-Foreign key identification change, 9-Nullable identification change
    @TableField(exist = false)
    private String updateType;

    /**
     * Whether to increment automatically
     */
    private String autoIncrementFlag;

    /**
     * Whether to partition fields
     */
    private String partitionFlag;

    /**
     * Field specification
     */
    private String columnStandard;

    /**
     * Value logic
     */
    private String valueRule;

    /** Responsible person */
    private Long businessLeader;

    /**
     * Responsible department
     */
    private Long responsibleDept;

    /**
     * Is it unique?
     */
    private String uniqueFlag;
}
