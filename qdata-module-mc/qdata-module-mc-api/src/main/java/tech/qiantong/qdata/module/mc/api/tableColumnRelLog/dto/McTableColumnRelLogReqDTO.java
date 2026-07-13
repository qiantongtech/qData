package tech.qiantong.qdata.module.mc.api.tableColumnRelLog.dto;

import lombok.Data;

/**
 * Metadata database and information and field information relationship - log DTO object MC_TABLE_COLUMN_REL_LOG
 *
 * @author qdata
 * @date 2026-03-10
 */
@Data
public class McTableColumnRelLogReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
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
    private Boolean delFlag;

    /** Description */
    private String description;


}
