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

package tech.qiantong.qdata.module.dm.dal.dataobject.businessCategory;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * Business Category Domain Relation DO - DM_BUSINESS_DOMAIN_REL
 *
 * @author qdata
 * @date 2026-04-12
 */
@Data
@TableName(value = "DM_BUSINESS_DOMAIN_REL")
// Used for auto-increment primary keys in Oracle, PostgreSQL, Kingbase, DB2, H2 databases. Can be omitted for MySQL and similar databases.
// @KeySequence("DM_BUSINESS_DOMAIN_REL_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DmBusinessDomainRelDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
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

    /** Valid Flag; 0: Invalid, 1: Valid */
    private Boolean validFlag;

    /** Delete Flag; 1: Deleted, 0: Not Deleted */
    @TableLogic
    private Boolean delFlag;


}
