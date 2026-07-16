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

package tech.qiantong.qdata.module.att.api.client.dto;

import lombok.Data;

/**
 * Application Management DTO ATT_CLIENT
 *
 * @author qdata
 * @date 2025-02-18
 */
@Data
public class AttClientReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** App Name */
    private String name;

    /** App Type */
    private String type;

    /** App Secret */
    private String secret;

    /** Homepage URL */
    private String homepageUrl;

    /** Allowed Authorization URL */
    private String allowUrl;

    /** Sync URL */
    private String syncUrl;

    /** App Logo */
    private String logo;

    /** App Description */
    private String description;

    /** Public Flag */
    private String publicFlag;

    /** Valid Flag */
    private Boolean validFlag;

    /** Delete Flag */
    private Boolean delFlag;


}
