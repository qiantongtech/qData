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

package tech.qiantong.qdata.module.da.api.assetColumn.dto;

import lombok.Data;

/**
 * Data Asset Column DTO DA_ASSET_COLUMN
 *
 * @author lhs
 * @date 2025-01-21
 */
@Data
public class DaAssetColumnRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Asset ID */
    private String assetId;

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

    /** Code Flag */
    private String dataElemCodeFlag;

    /** Code ID */
    private String dataElemCodeId;

    /** Sensitive Level ID */
    private String sensitiveLevelId;

    /** Related Data Element Flag */
    private String relDataElmeFlag;

    /** Related Clean Rule Flag */
    private String relCleanFlag;

    /** Related Audit Rule Flag */
    private String relAuditFlag;

    /** Description */
    private String description;

    /** Valid Flag */
    private Boolean validFlag;

    /** Delete Flag */
    private Boolean delFlag;


}
