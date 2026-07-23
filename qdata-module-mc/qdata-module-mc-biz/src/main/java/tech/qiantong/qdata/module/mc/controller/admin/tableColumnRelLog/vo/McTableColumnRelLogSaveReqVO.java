package tech.qiantong.qdata.module.mc.controller.admin.tableColumnRelLog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * Metadata database and information and field information relationship - log creation/modification Request VO MC_TABLE_COLUMN_REL_LOG
 *
 * @author qdata
 * @date 2026-03-10
 */
@Schema(description = "元数据数据库与信息及字段信息关系-日志 Response VO")
@Data
public class McTableColumnRelLogSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "数据类型;数据类型 1：预发布 2：采集，预留字段，暂时不用", example = "")
    @Size(max = 256, message = "数据类型;数据类型 1：预发布 2：采集，预留字段，暂时不用长度不能超过256个字符")
    private String dataType;

    @Schema(description = "采集任务 id;预留字段，暂时不用", example = "")
    private Long taskId;

    @Schema(description = "库 id", example = "")
    private Long dbId;

    @Schema(description = "库版本", example = "")
    private Integer dbVersion;

    @Schema(description = "表 id", example = "")
    private Long tableId;

    @Schema(description = "表版本", example = "")
    private Integer tableVersion;

    @Schema(description = "字段 id", example = "")
    @Size(max = 256, message = "字段 id长度不能超过256个字符")
    private String columnId;

    @Schema(description = "字段版本", example = "")
    private Integer columnVersion;

    @Schema(description = "备注", example = "")
    @Size(max = 512, message = "备注长度不能超过256个字符")
    private String remark;

    @Schema(description = "描述", example = "")
    @Size(max = 512, message = "描述长度不能超过256个字符")
    private String description;


}
