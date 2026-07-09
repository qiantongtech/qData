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

package tech.qiantong.qdata.module.dp.api.model.dto;

import lombok.Data;

/**
 * Materialized Model Record DTO - DP_MODEL_MATERIALIZED
 *
 * @author qdata
 * @date 2025-01-21
 */
@Data
public class DpModelMaterializedReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Model Code */
    private String modelName;

    /** Model Name */
    private String modelAlias;

    /** Model Table ID */
    private Long modelId;

    /** Status */
    private String status;

    /** Execution Log Message */
    private String message;

    /** Execution SQL Backup */
    private String sqlCommand;

    /** Datasource ID */
    private String datasourceId;

    /** Datasource Type */
    private String datasourceType;

    /** Datasource Name */
    private String datasourceName;

    /** Asset Table ID */
    private String assetId;

    /** Valid Flag */
    private Boolean validFlag;

    /** Delete Flag */
    private Boolean delFlag;


}
