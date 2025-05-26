package tech.qiantong.qdata.module.da.controller.admin.assetColumn.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 数据资产字段 Request VO 对象 DA_ASSET_COLUMN
 *
 * @author lhs
 * @date 2025-01-21
 */
@Schema(description = "数据资产字段 Request VO")
@Data
public class DaAssetColumnPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "资产id", example = "")
    private String assetId;

    @Schema(description = "字段名称/英文名称", example = "")
    private String columnName;

    @Schema(description = "字段注释/中文名称", example = "")
    private String columnComment;

    @Schema(description = "数据类型", example = "")
    private String columnType;

    @Schema(description = "长度", example = "")
    private Long columnLength;

    @Schema(description = "小数位", example = "")
    private Long columnScale;

    @Schema(description = "是否必填", example = "")
    private String nullableFlag;

    @Schema(description = "是否主键", example = "")
    private String pkFlag;

    @Schema(description = "默认值", example = "")
    private String defaultValue;

    @Schema(description = "是否代码", example = "")
        private String dataElemCodeFlag;

    @Schema(description = "代码id", example = "")
    private String dataElemCodeId;

    @Schema(description = "敏感等级id", example = "")
    private String sensitiveLevelId;

    @Schema(description = "敏感等级名称", example = "")
    private String sensitiveLevelName;

    @Schema(description = "关联数据元", example = "")
    private String relDataElmeFlag;

    @Schema(description = "关联清洗规则", example = "")
    private String relCleanFlag;

    @Schema(description = "关联稽查规则", example = "")
    private String relAuditFlag;

    @Schema(description = "描述", example = "")
    private String description;




}
