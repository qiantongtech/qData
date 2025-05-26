package tech.qiantong.qdata.module.att.api.theme.dto;

import lombok.Data;

/**
 * 主题 DTO 对象 ATT_THEME
 *
 * @author qdata
 * @date 2025-01-20
 */
@Data
public class AttThemeRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 主题名称 */
    private String name;

    /** 图标url */
    private String icon;

    /** 排序 */
    private Long sortOrder;

    /** 描述 */
    private String description;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
