package tech.qiantong.qdata.module.mc.api.column.dto;

import lombok.Data;

/**
 * <P>
 * Purpose: metadata field information - log DTO object MC_COLUMN
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
     * Field ID
     */
    private Long columnId;

    /**
     * Version
     */
    private Integer version;

    /**
     * Table information ID
     */
    private Long tableId;

    /**
     * Field name
     */
    private String columnName;

    /**
     * Field comment
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
     * Data precision
     */
    private Integer columnPrecision;

    /**
     * Data scale
     */
    private Integer columnScale;

    /**
     * Data default value
     */
    private String defaultValue;

    /**
     * Whether it is a primary key; 0: no, 1: yes
     */
    private String pkFlag;

    /**
     * Whether it is a foreign key; 0: no, 1: yes
     */
    private String fkFlag;

    /**
     * Whether it is nullable; 0: no, 1: yes
     */
    private String nullableFlag;
}
