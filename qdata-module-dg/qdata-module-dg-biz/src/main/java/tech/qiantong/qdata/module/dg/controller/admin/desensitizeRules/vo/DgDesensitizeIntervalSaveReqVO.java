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

package tech.qiantong.qdata.module.dg.controller.admin.desensitizeRules.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * Desensitize Interval Create/Update Request VO DG_DESENSITIZE_INTERVAL
 *
 * @author qdata
 * @date 2026-04-10
 */
@Schema(description = "Desensitize Interval Save Request VO")
@Data
public class DgDesensitizeIntervalSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "Desensitize rule ID", example = "")
    private Long desensitizeRuleId;

    @Schema(description = "Interval number", example = "")
    private Long intervalNo;

    @Schema(description = "Start value", example = "")
    private Long startNum;

    @Schema(description = "End value", example = "")
    private Long endNum;

    @Schema(description = "Remark", example = "")
    @Size(max = 256, message = "{valid.max.size}")
    private String remark;

    /** Whether valid; 0: invalid, 1: valid */
    private Boolean validFlag;
}
