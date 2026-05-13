package tech.qiantong.qdata.module.mc.controller.admin.columnLog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * 元数据字段信息 - 日志 创建/修改 Request VO MC_COLUMN_LOG
 *
 * @author qdata
 * @date 2026-03-10
 */
@Schema(description = "元数据字段信息 - 日志 Response VO")
@Data
public class McColumnLogSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "数据类型;数据类型 1：预发布 2：采集，预留字段，暂时不用", example = "")
    @Size(max = 256, message = "数据类型;数据类型 1：预发布 2：采集，预留字段，暂时不用长度不能超过256个字符")
    private String dataType;

    @Schema(description = "采集任务 id;预留字段，暂时不用", example = "")
    private Long taskId;

    @Schema(description = "字段 id", example = "")
    private Long columnId;

    @Schema(description = "版本", example = "")
    private Integer version;

    @Schema(description = "库 id", example = "")
    private Long dbId;

    @Schema(description = "表信息 id", example = "")
    private Long tableId;

    @Schema(description = "数据源 id;冗余字段", example = "")
    private Long datasourceId;

    @Schema(description = "安全等级 id", example = "")
    private Long safetyLevelId;

    @Schema(description = "数据元 id", example = "")
    private Long dataElemId;

    @Schema(description = "字段名称", example = "")
    @Size(max = 256, message = "字段名称长度不能超过256个字符")
    private String columnName;

    @Schema(description = "字段注释", example = "")
    @Size(max = 256, message = "字段注释长度不能超过256个字符")
    private String columnComment;

    @Schema(description = "字段类型", example = "")
    @Size(max = 256, message = "字段类型长度不能超过256个字符")
    private String columnType;

    @Schema(description = "数据长度", example = "")
    private Integer columnLength;

    @Schema(description = "数据精度", example = "")
    private Integer columnPrecision;

    @Schema(description = "数据小数位", example = "")
    private Integer columnScale;

    @Schema(description = "数据默认值", example = "")
    @Size(max = 256, message = "数据默认值长度不能超过256个字符")
    private String defaultValue;

    @Schema(description = "是否主键;0:否 1:是", example = "")
    @Size(max = 256, message = "是否主键;0:否 1:是长度不能超过256个字符")
    private String pkFlag;

    @Schema(description = "是否外键;0:否 1:是", example = "")
    @Size(max = 256, message = "是否外键;0:否 1:是长度不能超过256个字符")
    private String fkFlag;

    @Schema(description = "是否可空;0:否 1:是", example = "")
    @Size(max = 256, message = "是否可空;0:否 1:是长度不能超过256个字符")
    private String nullableFlag;

    @Schema(description = "业务定义", example = "")
    @Size(max = 256, message = "业务定义长度不能超过256个字符")
    private String busDefinition;

    @Schema(description = "度量单位", example = "")
    @Size(max = 256, message = "度量单位长度不能超过256个字符")
    private String measuringUnit;

    @Schema(description = "数据质量", example = "")
    private Integer dataQuality;

    @Schema(description = "变更类型", example = "")
    @Size(max = 256, message = "变更类型长度不能超过256个字符")
    private String updateType;

    @Schema(description = "变更说明", example = "")
    @Size(max = 256, message = "变更说明长度不能超过256个字符")
    private String updateMsg;

    @Schema(description = "备注", example = "")
    @Size(max = 512, message = "备注长度不能超过256个字符")
    private String remark;

    @Schema(description = "描述", example = "")
    @Size(max = 512, message = "描述长度不能超过256个字符")
    private String description;


}
