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
 * 业务分类数据域关联关系 DTO 对象 DM_BUSINESS_DOMAIN_REL
 *
 * @author qdata
 * @date 2026-04-12
 */
@Data
public class DmBusinessDomainRelReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 业务分类ID */
    private Long businessCategoryId;

    /** 数据域ID */
    private Long dataDomainId;

    /** 业务分类名称 */
    private String businessCategoryName;

    /** 数据域名称 */
    private String dataDomainName;

    /** 排序 */
    private Long sortOrder;

    /** 描述 */
    private String description;

    /** 是否有效;0：无效，1：有效 */
    private Boolean validFlag;

    /** 删除标志;1：已删除，0：未删除 */
    private Boolean delFlag;


}
