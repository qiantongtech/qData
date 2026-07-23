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

package tech.qiantong.qdata.module.dpp.api.etl.dto;

import lombok.Data;

import java.util.Date;

/**
 * Data Quality Log DTO - DPP_QUALITY_LOG
 *
 * @author qdata
 * @date 2025-07-19
 */
@Data
public class DppQualityLogRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Name */
    private String name;

    /** Status */
    private String successFlag;

    /** Start Time */
    private Date startTime;

    /** End Time */
    private Date endTime;

    /** Task ID */
    private String qualityId;

    /** Score */
    private Long score;

    /** Problem Data */
    private Long problemData;

    /** Valid Flag */
    private Boolean validFlag;

    /** Delete Flag */
    private Boolean delFlag;


}
