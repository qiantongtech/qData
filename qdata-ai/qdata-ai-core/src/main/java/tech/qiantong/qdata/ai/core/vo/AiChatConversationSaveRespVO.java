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

package tech.qiantong.qdata.ai.core.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <P>
 * Purpose: session creation
 * </p>
 *
 * @author: FXB
 * @create: 2026-04-11 11:16
 **/
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Schema(description = "会话创建 Response VO")
public class AiChatConversationSaveRespVO {

    @Schema(description = "会话id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "创建会话状态码；200：成功 10001：匹配字段关联信息识别失败", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long code;
}
