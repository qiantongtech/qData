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

package tech.qiantong.qdata.module.att.dal.dataobject.client;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * App Management DO ATT_CLIENT
 *
 * @author qdata
 * @date 2025-02-18
 */
@Data
@TableName(value = "ATT_CLIENT")
// Used for auto-increment primary keys in Oracle, PostgreSQL, Kingbase, DB2, H2 databases. Can be omitted for MySQL and similar databases.
// @KeySequence("ATT_CLIENT_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AttClientDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** App Name */
    private String name;

    /** App Type */
    private String type;

    /** App Secret */
    private String secret;

    /** Homepage URL */
    private String homepageUrl;

    /** Authorized URL */
    private String allowUrl;

    /** Sync URL */
    private String syncUrl;

    /** App Icon */
    private String logo;

    /** App Description */
    private String description;

    /** Public Flag */
    private String publicFlag;

    /** Valid Flag */
    private Boolean validFlag;

    /** Delete Flag */
    @TableLogic
    private Boolean delFlag;


}
