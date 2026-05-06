package tech.qiantong.qdata.module.mc.api.columnLog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 元数据字段信息 - 日志 DTO 对象 MC_COLUMN_LOG
 *
 * @author qdata
 * @date 2026-03-10
 */
@Data
public class McColumnLogRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 数据类型;数据类型 1：预发布 2：采集，预留字段，暂时不用 */
    private String dataType;

    /** 采集任务 id;预留字段，暂时不用 */
    private Long taskId;

    /** 字段 id */
    private Long columnId;

    /** 版本 */
    private Integer version;

    /** 库 id */
    private Long dbId;

    /** 表信息 id */
    private Long tableId;

    /** 数据源 id;冗余字段 */
    private Long datasourceId;

    /** 安全等级 id */
    private Long safetyLevelId;

    /** 数据元 id */
    private Long dataElemId;

    /** 字段名称 */
    private String columnName;

    /** 字段注释 */
    private String columnComment;

    /** 字段类型 */
    private String columnType;

    /** 数据长度 */
    private Integer columnLength;

    /** 数据精度 */
    private Integer columnPrecision;

    /** 数据小数位 */
    private Integer columnScale;

    /** 数据默认值 */
    private String defaultValue;

    /** 是否主键;0:否 1:是 */
    private String pkFlag;

    /** 是否外键;0:否 1:是 */
    private String fkFlag;

    /** 是否可空;0:否 1:是 */
    private String nullableFlag;

    /** 业务定义 */
    private String busDefinition;

    /** 度量单位 */
    private String measuringUnit;

    /** 数据质量 */
    private Integer dataQuality;

    /** 变更类型 */
    private String updateType;

    /** 变更说明 */
    private String updateMsg;

    /** 是否有效;0：无效，1：有效 */
    private Boolean validFlag;

    /** 删除标志;1：已删除，0：未删除 */
    private Boolean delFlag;

    /** 描述 */
    private String description;

    /**
     * 是否在门户展示：0-不展示，1-展示
     */
    @Schema(description = "是否在门户展示：0-不展示，1-展示", example = "0")
    private String portalVisible;


}
