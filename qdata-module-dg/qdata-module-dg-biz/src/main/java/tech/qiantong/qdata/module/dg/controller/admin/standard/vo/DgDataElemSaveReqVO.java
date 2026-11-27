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
 * Business License: https://docs.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.module.dg.controller.admin.standard.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * Data Element Create/Update Request VO DG_DATA_ELEM
 *
 * @author qdata
 * @date 2025-01-21
 */
@Schema(description = "Data Element Response VO")
@Data
public class DgDataElemSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "Name", example = "")
    @Size(max = 256, message = "Name length must not exceed 256 characters")
    private String name;

    @Schema(description = "English name", example = "")
    @Size(max = 256, message = "English name length must not exceed 256 characters")
    private String engName;

    @Schema(description = "Category code", example = "")
    @Size(max = 256, message = "Category code length must not exceed 256 characters")
    private String catCode;

    @Schema(description = "Type", example = "")
    @Size(max = 256, message = "Type length must not exceed 256 characters")
    private String type;

    @Schema(description = "Person in charge", example = "")
    @Size(max = 256, message = "Person in charge length must not exceed 256 characters")
    private String personCharge;

    @Schema(description = "Contact number", example = "")
    @Size(max = 256, message = "Contact number length must not exceed 256 characters")
    private String contactNumber;

    @Schema(description = "Column type", example = "")
    @Size(max = 256, message = "Column type length must not exceed 256 characters")
    private String columnType;

    @Schema(description = "Status", example = "")
    @Size(max = 256, message = "Status length must not exceed 256 characters")
    private String status;

    @Schema(description = "Description", example = "")
    @Size(max = 3000, message = "Description length must not exceed 3000 characters")
    private String description;

    private Long documentId;
}
