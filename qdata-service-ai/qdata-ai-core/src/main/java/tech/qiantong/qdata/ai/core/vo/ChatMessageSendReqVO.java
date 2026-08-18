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


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "管理后台 - AI 聊天消息发送 Request VO")
@Data
public class ChatMessageSendReqVO {

    @Schema(description = "聊天对话编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long conversationId;

    @Schema(description = "聊天内容,格式{\"msg\":\"xxxxx\"}", requiredMode = Schema.RequiredMode.REQUIRED)
    private String content;

    @Schema(description = "是否携带上下文", example = "true")
    private Boolean contextFlag;

    @Schema(description = "回复类型;1:知识问答 2: 知识图表", example = "true")
    private String replyType;

    @Schema(description = "模型id", example = "true")
    private Long modelId;

}
