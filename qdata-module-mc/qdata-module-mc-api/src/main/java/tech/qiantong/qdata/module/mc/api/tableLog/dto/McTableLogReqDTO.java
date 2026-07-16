package tech.qiantong.qdata.module.mc.api.tableLog.dto;

import lombok.Data;

/**
 * Metadata information - Log DTO object MC_TABLE_LOG
 *
 * @author qdata
 * @date 2026-03-10
 */
@Data
public class McTableLogReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Data type; Data type 1: Pre-release 2: Collection, reserved fields, not used temporarily */
    private String dataType;

    /** Collection task id; reserved field, not used temporarily */
    private Long taskId;

    /** table id */
    private Long tableId;

    /** version */
    private String version;

    /** Database ID */
    private Long dbId;

    /** Data source id; redundant field */
    private Long datasourceId;

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

    /** Change type */
    private String updateType;

    /** Change description */
    private String updateMsg;

    /** Whether it is valid; 0: invalid, 1: valid */
    private Boolean validFlag;

    /** Deletion flag; 1: deleted, 0: not deleted */
    private Boolean delFlag;

    /** Description */
    private String description;


}
