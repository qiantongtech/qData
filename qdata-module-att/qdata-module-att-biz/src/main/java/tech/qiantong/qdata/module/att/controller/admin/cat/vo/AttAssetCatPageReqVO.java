package tech.qiantong.qdata.module.att.controller.admin.cat.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 数据资产类目管理 Request VO 对象 ATT_ASSET_CAT
 *
 * @author qdata
 * @date 2025-01-20
 */
@Schema(description = "数据资产类目管理 Request VO")
@Data
public class AttAssetCatPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;

    @Schema(description = "类别名称", example = "")
    private String name;
    @Schema(description = "层级编码", example = "")
    private String code;


    private Boolean validFlag;






}
