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

import java.util.Date;

/**
 * App-API Service Relation DTO ATT_CLIENT_API_REL
 *
 * @author FXB
 * @date 2025-08-21
 */
@Data
public class AttClientApiRelRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** App ID */
    private Long clientId;

    /** API Service ID */
    private Long apiId;

    /** Permanent Valid Flag */
    private String pvFlag;

    /** Start Time */
    private Date startTime;

    /** End Time */
    private Date endTime;

    /** Authorization Status */
    private String status;

    /** Valid Flag */
    private Boolean validFlag;

    /** Deletion Flag */
    private Boolean delFlag;


}
