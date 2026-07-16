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
import java.util.List;

import tech.qiantong.qdata.common.core.domain.BaseEntity;
import tech.qiantong.qdata.module.dg.dal.dataobject.desensitizeRules.DgDesensitizeIntervalDO;

/**
 * Desensitize Rule Create/Update Request VO DG_DESENSITIZE_RULE
 *
 * @author qdata
 * @date 2026-04-10
 */
@Schema(description = "Desensitize Rule Save Request VO")
@Data
public class DgDesensitizeRuleSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "Classification name", example = "")
    @Size(max = 256, message = "{valid.max.size}")
    private String name;

    @Schema(description = "Data category ID", example = "")
    private Long dataCategoryId;

    @Schema(description = "Application scene; 1: Data asset 2: Data query 3: Data service", example = "")
    @Size(max = 256, message = "{valid.max.size}")
    private String applicationScene;

    @Schema(description = "Mask type; 1: Underlying mask 2: Display mask", example = "")
    @Size(max = 256, message = "{valid.max.size}")
    private String maskType;

    @Schema(description = "Replace rule", example = "")
    @Size(max = 256, message = "{valid.max.size}")
    private String replaceRule;

    @Schema(description = "Replace content", example = "")
    @Size(max = 256, message = "{valid.max.size}")
    private String replaceContent;

    @Schema(description = "Desensitize intervals", example = "")
    private List<DgDesensitizeIntervalDO> intervalList;

    @Schema(description = "Sort order", example = "")
    private Long sortOrder;

    @Schema(description = "Description", example = "")
    @Size(max = 256, message = "{valid.max.size}")
    private String description;

    @Schema(description = "Remark", example = "")
    @Size(max = 256, message = "{valid.max.size}")
    private String remark;

    /** Whether valid; 0: invalid, 1: valid */
    private Boolean validFlag;
}
