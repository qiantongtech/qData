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

package tech.qiantong.qdata.module.system.dal.dataobject.auth;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;


/**
 * User and Authentication Center Relationship DO object rel_user_auth_product
 *
 * @author qdata
 * @date 2024-11-07
 */
@Data
@TableName(value = "rel_user_auth_product")
// Used for auto-increment primary key in Oracle, PostgreSQL, Kingbase, DB2, H2 databases. For MySQL etc., this can be omitted.
// @KeySequence("rel_user_auth_product_seq")
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelUserAuthProductDO {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** User ID */
    private Long userId;

    /** Unified identity authentication ID */
    private String authId;

    /** Authentication platform type */
    private Integer authProductType;


}
