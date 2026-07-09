/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.module.dm.api.dm.dto;

import lombok.*;

/**
 * Theme Domain DTO - DM_THEME_DOMAIN
 *
 * @author FXB
 * @date 2026-03-24
 */
@Data
public class DmThemeDomainReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Layer Code */
    private String code;

    /** Name */
    private String name;

    /** English Abbreviation */
    private String engName;

    /** Parent ID */
    private Long parentId;

    /** Owner User ID */
    private Long ownerUserId;

    /** Data Warehouse Layer ID */
    private Long dataLayerId;

    /** Description */
    private String description;

    /** Valid Flag */
    private Boolean validFlag;

    /** Delete Flag */
    private Boolean delFlag;


}
