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
 * Business Category Data Domain Relation DTO - DM_BUSINESS_DOMAIN_REL
 *
 * @author qdata
 * @date 2026-04-12
 */
@Data
public class DmBusinessDomainRelRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Business Category ID */
    private Long businessCategoryId;

    /** Data Domain ID */
    private Long dataDomainId;

    /** Business Category Name */
    private String businessCategoryName;

    /** Data Domain Name */
    private String dataDomainName;

    /** Sort Order */
    private Long sortOrder;

    /** Description */
    private String description;

    /** Valid Flag; 0: invalid, 1: valid */
    private Boolean validFlag;

    /** Delete Flag; 1: deleted, 0: not deleted */
    private Boolean delFlag;


}
