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

package tech.qiantong.qdata.module.dm.dal.dataobject.dm;

import lombok.*;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * Theme Domain DO - DM_THEME_DOMAIN
 *
 * @author FXB
 * @date 2026-03-24
 */
@Data
@TableName(value = "DM_THEME_DOMAIN")
// Used for auto-increment primary keys in Oracle, PostgreSQL, Kingbase, DB2, H2 databases. Can be omitted for MySQL and similar databases.
// @KeySequence("DM_THEME_DOMAIN_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DmThemeDomainDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * Level Code
     */
    private String code;

    /**
     * Name
     */
    private String name;

    /**
     * English Abbreviation
     */
    private String engName;

    /**
     * Parent ID
     */
    private Long parentId;

    /**
     * Owner User ID
     */
    private Long ownerUserId;

    /**
     * Data Warehouse Layer ID
     */
    private Long dataLayerId;

    /**
     * Description
     */
    private String description;

    /**
     * Valid Flag
     */
    private Boolean validFlag;

    /**
     * Delete Flag
     */
    @TableLogic
    private Boolean delFlag;


    /**
     * Owner Name
     */
    @TableField(exist = false)
    private String ownerUserName;

    /**
     * Data Warehouse Layer Name
     */
    @TableField(exist = false)
    private String dataLayerName;

    /**
     * Owner Contact
     */
    @TableField(exist = false)
    private String ownerUserPhoneNumber;

    /**
     * Count
     */
    @TableField(exist = false)
    private Long num;
}
