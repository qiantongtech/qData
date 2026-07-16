package tech.qiantong.qdata.module.mc.dal.dataobject.tableColumnRelLog;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * Metadata database and information and field information relationship - log DO object MC_TABLE_COLUMN_REL_LOG
 *
 * @author qdata
 * @date 2026-03-10
 */
@Data
@TableName(value = "MC_TABLE_COLUMN_REL_LOG")
// Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you do not need to write it.
// @KeySequence("MC_TABLE_COLUMN_REL_LOG_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class McTableColumnRelLogDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** Data type; Data type 1: Pre-release 2: Collection, reserved fields, not used temporarily */
    private String dataType;

    /** Collection task id; reserved field, not used temporarily */
    private Long taskId;

    /** Database ID */
    private Long dbId;

    /** Library version */
    private Integer dbVersion;

    /** table id */
    private Long tableId;

    /** Table version */
    private Integer tableVersion;

    /** Field id */
    private String columnId;

    /** Field version */
    private Integer columnVersion;

    /** Whether it is valid; 0: invalid, 1: valid */
    private Boolean validFlag;

    /** Deletion flag; 1: deleted, 0: not deleted */
    @TableLogic
    private Boolean delFlag;

    /** Description */
    private String description;


}
