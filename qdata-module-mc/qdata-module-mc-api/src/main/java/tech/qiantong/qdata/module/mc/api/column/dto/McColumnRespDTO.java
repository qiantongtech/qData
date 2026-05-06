package tech.qiantong.qdata.module.mc.api.column.dto;

import lombok.Data;

/**
 * <P>
 * 用途:元数据字段信息 - 日志 DTO 对象 MC_COLUMN
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
     * 字段 id
     */
    private Long columnId;

    /**
     * 版本
     */
    private Integer version;

    /**
     * 表信息 id
     */
    private Long tableId;

    /**
     * 字段名称
     */
    private String columnName;

    /**
     * 字段注释
     */
    private String columnComment;

    /**
     * 字段类型
     */
    private String columnType;

    /**
     * 数据长度
     */
    private Integer columnLength;

    /**
     * 数据精度
     */
    private Integer columnPrecision;

    /**
     * 数据小数位
     */
    private Integer columnScale;

    /**
     * 数据默认值
     */
    private String defaultValue;

    /**
     * 是否主键;0:否 1:是
     */
    private String pkFlag;

    /**
     * 是否外键;0:否 1:是
     */
    private String fkFlag;

    /**
     * 是否可空;0:否 1:是
     */
    private String nullableFlag;
}
