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

package tech.qiantong.qdata.module.dp.api.dataElem.dto;

import lombok.Data;

import java.util.Set;

/**
 * Data Element Asset Relation DTO - DP_DATA_ELEM_ASSET_REL
 *
 * @author qdata
 * @date 2025-01-21
 */
@Data
public class DpDataElemAssetRelReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Data Element Type */
    private String dataElemType;

    /** Data Element ID */
    private Long dataElemId;

    /** Asset ID (Table ID) */
    private Long assetId;

    /** Table Name */
    private String tableName;

    /** Column ID */
    private Long columnId;

    /** Column Name */
    private String columnName;

    /** Valid Flag */
    private Boolean validFlag;

    /** Delete Flag */
    private Boolean delFlag;

    /** Data Element ID Set */
    private Set<Long> elementIds;

}
