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
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * Desensitize Rule Request VO DG_DESENSITIZE_RULE
 *
 * @author qdata
 * @date 2026-04-10
 */
@Schema(description = "Desensitize Rule Page Request VO")
@Data
public class DgDesensitizeRulePageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "Classification name", example = "")
    private String name;

    @Schema(description = "Data category ID", example = "")
    private Long dataCategoryId;

    @Schema(description = "Application scene; 1: Data asset 2: Data query 3: Data service", example = "")
    private String applicationScene;

    @Schema(description = "Mask type; 1: Underlying mask 2: Display mask", example = "")
    private String maskType;

    @Schema(description = "Replace rule", example = "")
    private String replaceRule;

    @Schema(description = "Replace content", example = "")
    private String replaceContent;

    @Schema(description = "Sort order", example = "")
    private Long sortOrder;

    @Schema(description = "Description", example = "")
    private String description;

    @Schema(description = "Valid flag; 0: Invalid, 1: Valid", example = "")
    private Boolean validFlag;



}
