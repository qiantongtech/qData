package tech.qiantong.qdata.module.mc.dal.dataobject.columnLog;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * Metadata field information - log DO object MC_COLUMN_LOG
 *
 * @author qdata
 * @date 2026-03-10
 */
@Data
@TableName(value = "MC_COLUMN_LOG")
// Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you do not need to write it.
// @KeySequence("MC_COLUMN_LOG_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class McColumnLogDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** Data type; Data type 1: Pre-release 2: Collection, reserved fields, not used temporarily */
    private String dataType;

    /** Collection task id; reserved field, not used temporarily */
    private Long taskId;

    /** Field id */
    private Long columnId;

    /** version */
    private Integer version;

    /** Database ID */
    private Long dbId;

    /** Table information id */
    private Long tableId;

    /** Data source id; redundant field */
    private Long datasourceId;

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
    private String busDefinition;

    /** Unit of measurement */
    private String measuringUnit;

    /** Data quality */
    private Integer dataQuality;

    /** Change type */
    private String updateType;

    /** Change description */
    private String updateMsg;

    /** Whether it is valid; 0: invalid, 1: valid */
    private Boolean validFlag;

    /** Deletion flag; 1: deleted, 0: not deleted */
    @TableLogic
    private Boolean delFlag;

    /** Description */
    private String description;


}
