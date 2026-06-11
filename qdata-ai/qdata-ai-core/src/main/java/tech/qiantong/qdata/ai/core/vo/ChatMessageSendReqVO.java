/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
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
