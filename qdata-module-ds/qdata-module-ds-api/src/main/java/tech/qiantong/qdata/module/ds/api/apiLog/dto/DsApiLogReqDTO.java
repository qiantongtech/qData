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

package tech.qiantong.qdata.module.ds.api.apiLog.dto;

import lombok.Data;

import java.util.Date;

/**
 * API service call log DTO DS_API_LOG
 *
 * @author lhs
 * @date 2025-02-12
 */
@Data
public class DsApiLogReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long ID;

    /** Called API service ID */
    private String apiId;

    /** Caller ID */
    private String callerId;

    /** Caller */
    private Long callerBy;

    /** Caller IP */
    private String callerIp;

    /** Called URL */
    private String callerUrl;

    /** Call parameters */
    private String callerParams;

    /** Call start time */
    private Date callerStartDate;

    /** Call end time */
    private Date callerEndDate;

    /** Called record count */
    private Long callerSize;

    /** Call duration in milliseconds */
    private Long callerTime;

    /** Information record */
    private String MSG;

    /** Status */
    private String STATUS;

    /** Whether the record is active */
    private Boolean validFlag;

    /** Deletion flag */
    private Boolean delFlag;


}
