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

package tech.qiantong.qdata.module.dg.controller.admin.desensitizeList.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * Desensitize List Relationship Create/Update Request VO DG_DESENSITIZE_ASSETCOLUMN
 *
 * @author qdata
 * @date 2026-04-12
 */
@Schema(description = "Desensitize List Association Response VO")
@Data
public class DgDesensitizeAssetcolumnSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "Asset ID", example = "")
    private Long assetId;

    @Schema(description = "Asset Column ID", example = "")
    private Long assetcolumnId;

    @Schema(description = "Data Category ID", example = "")
    private Long dataCategoryId;

    @Schema(description = "Sort order", example = "")
    private Long sortOrder;

    @Schema(description = "Description", example = "")
    @Size(max = 256, message = "{dg.desensitize.assetcolumn.description.length}")
    private String description;

    @Schema(description = "Remark", example = "")
    @Size(max = 256, message = "{dg.desensitize.assetcolumn.remark.length}")
    private String remark;

    /** Whether valid; 0: invalid, 1: valid */
    private Boolean validFlag;


}
