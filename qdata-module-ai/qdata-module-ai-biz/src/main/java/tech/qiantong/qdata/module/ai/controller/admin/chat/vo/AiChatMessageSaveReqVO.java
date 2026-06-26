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

package tech.qiantong.qdata.module.ai.controller.admin.chat.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * ai聊天消息 创建/修改 Request VO AI_CHAT_MESSAGE
 *
 * @author FXB
 * @date 2026-04-01
 */
@Schema(description = "ai聊天消息 Response VO")
@Data
public class AiChatMessageSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "对话id", example = "")
    private Long conversationId;

    @Schema(description = "回复id", example = "")
    private Long replyId;

    /**
     * 回复类型;1:知识问答 2: 知识图表
     */
    @Schema(description = "回复类型;1:知识问答 2: 知识图表", example = "")
    private String replyType;

//    @Schema(description = "统计任务id", example = "")
//    private Long statisticsTaskId;

    @Schema(description = "用户id", example = "")
    private Long userId;

    @Schema(description = "消息类型;1:用户 2：机器人", example = "")
    @Size(max = 256, message = "消息类型;1:用户 2：机器人长度不能超过256个字符")
    private String type;

    @Schema(description = "消息内容", example = "")
    @Size(max = 256, message = "消息内容长度不能超过256个字符")
    private String content;

    @Schema(description = "是否携带上下文;0：否，1：是", example = "")
    @Size(max = 256, message = "是否携带上下文;0：否，1：是长度不能超过256个字符")
    private String contextFlag;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
