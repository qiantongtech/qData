package tech.qiantong.qdata.module.da.controller.admin.assetchild.theme.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

import java.util.List;

/**
 * 数据资产-主题关联关系 Request VO 对象 DA_ASSET_THEME_REL
 *
 * @author qdata
 * @date 2025-04-14
 */
@Schema(description = "数据资产-主题关联关系 Request VO")
@Data
public class DaAssetThemeRelPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "资产id", example = "")
    private Long assetId;

    @Schema(description = "主题id", example = "")
    private Long themeId;

    private String themeName;

    private List<String> themeIdList;

}
