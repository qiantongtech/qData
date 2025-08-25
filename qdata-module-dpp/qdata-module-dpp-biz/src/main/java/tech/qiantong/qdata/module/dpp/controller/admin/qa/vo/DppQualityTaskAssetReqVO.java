package tech.qiantong.qdata.module.dpp.controller.admin.qa.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

@Data
public class DppQualityTaskAssetReqVO  extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;

    @Schema(description = "资产id")
    private Long assetId;
}
