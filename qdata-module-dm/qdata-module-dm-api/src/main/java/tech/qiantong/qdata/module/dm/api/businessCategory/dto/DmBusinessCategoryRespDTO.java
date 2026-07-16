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

package tech.qiantong.qdata.module.dm.api.businessCategory.dto;

import lombok.Data;

/**
 * Business Category DTO - DM_BUSINESS_CATEGORY
 *
 * @author qdata
 * @date 2026-04-08
 */
@Data
public class DmBusinessCategoryRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Layer Code */
    private String code;

    /** Business Category Name */
    private String name;

    /** Parent ID */
    private Long parentId;

    /** Sort Order */
    private Long sortOrder;

    /** Description */
    private String description;

    /** English Abbreviation */
    private String engName;

    /** Owner Phone */
    private String ownerPhone;

    /** Owner ID */
    private Long ownerId;

    /** Data Domain ID */
    private Long domainId;

    /** Valid Flag; 0: invalid, 1: valid */
    private Boolean validFlag;

    /** Delete Flag; 1: deleted, 0: not deleted */
    private Boolean delFlag;


}
