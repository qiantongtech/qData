package tech.qiantong.qdata.module.mc.controller.admin.tableColumnRelLog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * Metadata database and information and field information relationship - log Request VO object MC_TABLE_COLUMN_REL_LOG
 *
 * @author qdata
 * @date 2026-03-10
 */
@Schema(description = "元数据数据库与信息及字段信息关系-日志 Request VO")
@Data
public class McTableColumnRelLogPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "数据类型;数据类型 1：预发布 2：采集，预留字段，暂时不用", example = "")
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
    private String columnId;

    @Schema(description = "字段版本", example = "")
    private Integer columnVersion;



    @Schema(description = "描述", example = "")
    private String description;


}
