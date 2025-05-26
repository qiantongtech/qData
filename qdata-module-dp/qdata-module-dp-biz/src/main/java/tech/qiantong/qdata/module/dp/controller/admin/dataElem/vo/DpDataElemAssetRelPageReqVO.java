package tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 数据元数据资产关联信息 Request VO 对象 DP_DATA_ELEM_ASSET_REL
 *
 * @author qdata
 * @date 2025-01-21
 */
@Schema(description = "数据元数据资产关联信息 Request VO")
@Data
public class DpDataElemAssetRelPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "数据元类型", example = "")
    private String dataElemType;

    @Schema(description = "数据元id", example = "")
    private String dataElemId;

    @Schema(description = "资产id(数据表id)", example = "")
    private String assetId;

    @Schema(description = "数据表", example = "")
    private String tableName;

    @Schema(description = "关联字段id", example = "")
    private String columnId;

    @Schema(description = "关联字段", example = "")
    private String columnName;




}
