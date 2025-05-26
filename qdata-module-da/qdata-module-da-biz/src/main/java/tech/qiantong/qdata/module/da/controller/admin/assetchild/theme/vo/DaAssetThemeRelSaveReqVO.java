package tech.qiantong.qdata.module.da.controller.admin.assetchild.theme.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * 数据资产-主题关联关系 创建/修改 Request VO DA_ASSET_THEME_REL
 *
 * @author qdata
 * @date 2025-04-14
 */
@Schema(description = "数据资产-主题关联关系 Response VO")
@Data
public class DaAssetThemeRelSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "资产id", example = "")
    private Long assetId;

    @Schema(description = "主题id", example = "")
    private Long themeId;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
