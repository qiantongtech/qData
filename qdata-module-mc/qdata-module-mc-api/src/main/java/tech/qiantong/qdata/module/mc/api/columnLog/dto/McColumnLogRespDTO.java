package tech.qiantong.qdata.module.mc.api.columnLog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Metadata field information - Log DTO object MC_COLUMN_LOG
 *
 * @author qdata
 * @date 2026-03-10
 */
@Data
public class McColumnLogRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
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
    private Boolean delFlag;

    /** Description */
    private String description;

    /**
     * Whether to display on the portal: 0-not displayed, 1-displayed
     */
    @Schema(description = "是否在门户展示：0-不展示，1-展示", example = "0")
    private String portalVisible;


}
