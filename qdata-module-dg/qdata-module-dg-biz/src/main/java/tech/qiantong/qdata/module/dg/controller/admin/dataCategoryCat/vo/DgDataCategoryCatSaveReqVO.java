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

package tech.qiantong.qdata.module.dg.controller.admin.dataCategoryCat.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * Data Category - Cat Create/Update Request VO - DG_DATA_CATEGORY_CAT
 *
 * @author FXB
 * @date 2026-04-07
 */
@Schema(description = "Data Category - Category Response VO")
@Data
public class DgDataCategoryCatSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "Category name", example = "")
    @Size(max = 256, message = "Category name length cannot exceed 256 characters")
    private String name;

    @Schema(description = "Parent category ID", example = "")
    private Long parentId;

    @Schema(description = "Sort order", example = "")
    private Long sortOrder;

    @Schema(description = "Level code", example = "")
    @Size(max = 256, message = "Level code length cannot exceed 256 characters")
    private String code;

    @Schema(description = "Description", example = "")
    @Size(max = 256, message = "Description length cannot exceed 256 characters")
    private String description;

    @Schema(description = "Remark", example = "")
    @Size(max = 256, message = "Remark length cannot exceed 256 characters")
    private String remark;


}
