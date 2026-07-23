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

import java.util.List;

/**
 * Business Category DO - DM_BUSINESS_CATEGORY
 *
 * @author qdata
 * @date 2026-04-08
 */
@Data
@TableName(value = "DM_BUSINESS_CATEGORY")
// Used for auto-increment primary keys in Oracle, PostgreSQL, Kingbase, DB2, H2 databases. Can be omitted for MySQL and similar databases.
// @KeySequence("DM_BUSINESS_CATEGORY_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DmBusinessCategoryDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * Level Code
     */
    private String code;

    /** Business Category Name */
    private String name;

    /** Parent ID */
    private Long parentId;

    /** Parent Name */
    @TableField(exist = false)
    private String parentName;

    /** Sort Order */
    private Long sortOrder;

    /** Description */
    private String description;

    /** English Abbreviation */
    private String engName;

    /** Owner Phone */
    private String ownerPhone;

    /** Owner User ID */
    private Long ownerId;

    /** Owner Name */
    @TableField(exist = false)
    private String ownerName;

    /** Data Domain ID */
    private Long domainId;
    /** Data Domain List */
    @TableField(exist = false)
    private List<DmBusinessDomainRelDO> domainList;

    @TableField(exist = false)
    private List<String> domainIds;

//    @TableField(exist = false)
//    private List<DmDataDomainDO> dataDomainList;

    /** Valid Flag; 0: Invalid, 1: Valid */
    private Boolean validFlag;

    /** Delete Flag; 1: Deleted, 0: Not Deleted */
    @TableLogic
    private Boolean delFlag;


}
