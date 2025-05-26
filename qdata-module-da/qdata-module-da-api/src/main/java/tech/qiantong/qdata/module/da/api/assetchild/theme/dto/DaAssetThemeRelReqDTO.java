package tech.qiantong.qdata.module.da.api.assetchild.theme.dto;

import lombok.Data;

/**
 * 数据资产-主题关联关系 DTO 对象 DA_ASSET_THEME_REL
 *
 * @author qdata
 * @date 2025-04-14
 */
@Data
public class DaAssetThemeRelReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 资产id */
    private Long assetId;

    /** 主题id */
    private Long themeId;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
