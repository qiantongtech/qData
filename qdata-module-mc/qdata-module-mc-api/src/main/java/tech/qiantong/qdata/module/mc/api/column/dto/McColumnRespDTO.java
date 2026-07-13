package tech.qiantong.qdata.module.mc.api.column.dto;

import lombok.Data;

/**
 * <P>
 * Purpose: Metadata field information - log DTO object MC_COLUMN
 * </p>
 *
 * @author: FXB
 * @create: 2026-04-28 14:05
 **/
@Data
public class McColumnRespDTO {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * Field id
     */
    private Long columnId;

    /**
     * Version
     */
    private Integer version;

    /**
     * Table information id
     */
    private Long tableId;

    /**
     * Field name
     */
    private String columnName;

    /**
     * Field annotation
     */
    private String columnComment;

    /**
     * Field type
     */
    private String columnType;

    /**
     * Data length
     */
    private Integer columnLength;

    /**
     * Data accuracy
     */
    private Integer columnPrecision;

    /**
     * Data decimal places
     */
    private Integer columnScale;

    /**
     * Data defaults
     */
    private String defaultValue;

    /**
     * Whether it is the primary key; 0: No 1: Yes
     */
    private String pkFlag;

    /**
     * Whether it is a foreign key; 0: No 1: Yes
     */
    private String fkFlag;

    /**
     * Whether it is nullable; 0: No 1: Yes
     */
    private String nullableFlag;
}
