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

package tech.qiantong.qdata.module.da.api.asset.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Data Asset DTO DA_ASSET
 *
 * @author lhs
 * @date 2025-01-21
 */
@Data
public class DaAssetRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Asset Name */
    private String name;

    @Schema(description = "资产类型", example = "")
    private String type;

    /** Category Code */
    private String catCode;

    /** Datasource ID */
    private String datasourceId;

    /** Table Name */
    private String tableName;

    /** Table Description */
    private String tableComment;

    /** Data Count */
    private Long dataCount;

    /** Field Count */
    private Long fieldCount;

    /** Source; 1: Data Discovery; 2: Data Model; */
    private String source;

    /** Status */
    private String STATUS;

    /** Description */
    private String description;

    /** Valid Flag */
    private Boolean validFlag;

    /** Delete Flag */
    private Boolean delFlag;
    @Schema(description = "创建类型", example = "")
    private String createType;


}
