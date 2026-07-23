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

package tech.qiantong.qdata.module.att.api.rule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Audit Rule DTO ATT_AUDIT_RULE
 *
 * @author qdata
 * @date 2025-01-20
 */
@Data
public class AttAuditRuleReqDTO {

    private static final long serialVersionUID = 1L;

    /** Rule ID */
    private Long id;

    /** Rule Name */
    private String name;

    /** Quality Dimension */
    private String qualityDim;

    /** Rule Type */
    private String type;

    /** Rule Level */
    private String level;

    /** Rule Description */
    private String description;

    /** Valid Flag */
    private Boolean validFlag;

    /** Delete Flag */
    private Boolean delFlag;

    @Schema(description = "Rule Code", example = "101")
    private String code;

    @Schema(description = "Use Case", example = "For checking non-empty ID card number")
    private String useCase;

    @Schema(description = "Example", example = "Field value cannot be empty, e.g.: ID=123456")
    private String example;


    @Schema(description = "Icon Path", example = "/images/icon.png")
    private String iconPath;

    @Schema(description = "Strategy Key", example = "NOT_NULL_ID_CHECK")
    private String strategyKey;

}
