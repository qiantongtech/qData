/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 */

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
