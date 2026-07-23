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

import java.util.Date;

/**
 * App API Service Association DO ATT_CLIENT_API_REL
 *
 * @author FXB
 * @date 2025-08-21
 */
@Data
@TableName(value = "ATT_CLIENT_API_REL")
// Used for auto-increment primary keys in Oracle, PostgreSQL, Kingbase, DB2, H2 databases. Can be omitted for MySQL and similar databases.
// @KeySequence("ATT_CLIENT_API_REL_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AttClientApiRelDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * App ID
     */
    private Long clientId;

    /**
     * API Service ID
     */
    private Long apiId;

    /**
     * API Service Name
     */
    @TableField(exist = false)
    private String apiName;

    /**
     * API Service URL
     */
    @TableField(exist = false)
    private String apiUrl;

    /**
     * Request Method 1: GET, 2: POST (ds_api_bas_info_api_method_type)
     */
    @TableField(exist = false)
    private String reqMethod;

    /**
     * Permanent Valid Flag
     */
    private String pvFlag;

    /**
     * Start Time
     */
    private Date startTime;

    /**
     * End Time
     */
    private Date endTime;

    /**
     * Authorization Status
     */
    private String status;

    /**
     * Valid Flag
     */
    private Boolean validFlag;

    /**
     * Delete Flag
     */
    @TableLogic
    private Boolean delFlag;
}
