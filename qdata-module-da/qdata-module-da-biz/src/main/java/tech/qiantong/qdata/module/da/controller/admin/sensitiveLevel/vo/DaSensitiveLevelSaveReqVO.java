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

package tech.qiantong.qdata.module.da.controller.admin.sensitiveLevel.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * 敏感等级 创建/修改 Request VO DA_SENSITIVE_LEVEL
 *
 * @author qdata
 * @date 2025-01-21
 */
@Schema(description = "敏感等级 Response VO")
@Data
public class DaSensitiveLevelSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "敏感级别", example = "")
    @Size(max = 256, message = "敏感级别长度不能超过256个字符")
    private String sensitiveLevel;

    @Schema(description = "敏感规则", example = "")
    @Size(max = 256, message = "敏感规则长度不能超过256个字符")
    private String sensitiveRule;

    @Schema(description = "起始字符位置", example = "")
    private Long startCharLoc;

    @Schema(description = "截止字符位置", example = "")
    private Long endCharLoc;

    @Schema(description = "遮盖字符", example = "")
    @Size(max = 256, message = "遮盖字符长度不能超过256个字符")
    private String maskCharacter;

    @Schema(description = "上下线标识", example = "")
    @Size(max = 256, message = "上下线标识长度不能超过256个字符")
    private String onlineFlag;

    @Schema(description = "描述", example = "")
    @Size(max = 256, message = "描述长度不能超过256个字符")
    private String description;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
