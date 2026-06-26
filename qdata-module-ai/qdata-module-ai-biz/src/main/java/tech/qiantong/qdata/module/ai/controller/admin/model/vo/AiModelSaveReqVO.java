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

package tech.qiantong.qdata.module.ai.controller.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * 模型管理 创建/修改 Request VO AI_MODEL
 *
 * @author FXB
 * @date 2026-04-01
 */
@Schema(description = "模型管理 Response VO")
@Data
public class AiModelSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "模型名称;例如 qwen-max", example = "")
    @Size(max = 256, message = "模型名称;例如 qwen-max长度不能超过256个字符")
    private String name;

    @Schema(description = "平台;1:通义千问 2:DeepSeek", example = "")
    @Size(max = 256, message = "平台;1:通义千问 2:DeepSeek长度不能超过256个字符")
    private String platform;

    @Schema(description = "API地址", example = "")
    @Size(max = 256, message = "API地址长度不能超过256个字符")
    private String apiUrl;

    @Schema(description = "API秘钥", example = "")
    @Size(max = 256, message = "API秘钥长度不能超过256个字符")
    private String apiKey;

    @Schema(description = "排序", example = "")
    private Long sortOrder;

    @Schema(description = "描述", example = "")
    @Size(max = 256, message = "描述长度不能超过256个字符")
    private String description;

    @Schema(description = "是否有效;0：无效，1：有效", example = "")
    private Boolean validFlag;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
