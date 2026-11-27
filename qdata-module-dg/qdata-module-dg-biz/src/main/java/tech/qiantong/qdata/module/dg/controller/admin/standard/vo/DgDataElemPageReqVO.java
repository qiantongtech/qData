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
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * Data Element Request VO Object DG_DATA_ELEM
 *
 * @author qdata
 * @date 2025-01-21
 */
@Schema(description = "Data Element Request VO")
@Data
public class DgDataElemPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;

    @Schema(description = "Name", example = "")
    private String name;

    @Schema(description = "English name", example = "")
    private String engName;

    @Schema(description = "Category code", example = "")
    private String catCode;

    @Schema(description = "Type", example = "")
    private String type;

    @Schema(description = "Person in charge", example = "")
    private String personCharge;

    @Schema(description = "Contact number", example = "")
    private String contactNumber;

    @Schema(description = "Column type", example = "")
    private String columnType;

    @Schema(description = "Status", example = "")
    private String status;

    @Schema(description = "Description", example = "")
    private String description;

    private Long documentId;
}
