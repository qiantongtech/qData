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

package tech.qiantong.qdata.module.da.api.discovery.dto;

import lombok.Data;

/**
 * Data Discovery Column DTO DA_DISCOVERY_COLUMN
 *
 * @author qdata
 * @date 2025-02-11
 */
@Data
public class DaDiscoveryColumnRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Task ID */
    private Long taskId;

    /** Discovery Table ID */
    private Long tableId;

    /** Column Name / English Name */
    private String columnName;

    /** Column Comment / Chinese Name */
    private String columnComment;

    /** Data Type */
    private String columnType;

    /** Length */
    private Long columnLength;

    /** Decimal Places */
    private Long columnScale;

    /** Required Flag */
    private String nullableFlag;

    /** Primary Key Flag */
    private String pkFlag;

    /** Default Value */
    private String defaultValue;

    /** Valid Flag */
    private Boolean validFlag;

    /** Delete Flag */
    private Boolean delFlag;


}
