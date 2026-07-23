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

package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * Online Form Data Create/Update Request VO DPP_ONL_DESFORM_DATA
 *
 * @author qdata
 * @date 2025-04-09
 */
@Schema(description = "Online Form Data Response VO")
@Data
public class DppOnlDesformDataSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "Form Code", example = "")
    @Size(max = 256, message = "Form code length cannot exceed 256 characters")
    private String desformCode;

    @Schema(description = "Form Name", example = "")
    @Size(max = 256, message = "Form name length cannot exceed 256 characters")
    private String desformName;

    @Schema(description = "Form ID", example = "")
    @Size(max = 256, message = "Form ID length cannot exceed 256 characters")
    private String desformId;

    @Schema(description = "Form Data", example = "")
//    @Size(max = 256, message = "Form data length cannot exceed 256 characters")
    private String desformData;

    @Schema(description = "Remarks", example = "")
//    @Size(max = 256, message = "Remarks length cannot exceed 256 characters")
    private String remark;


}
