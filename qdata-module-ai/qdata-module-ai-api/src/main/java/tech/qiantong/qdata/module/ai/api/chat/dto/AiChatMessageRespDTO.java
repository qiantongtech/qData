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

package tech.qiantong.qdata.module.ai.api.chat.dto;

import lombok.*;

/**
 * ai聊天消息 DTO 对象 AI_CHAT_MESSAGE
 *
 * @author FXB
 * @date 2026-04-01
 */
@Data
public class AiChatMessageRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 对话id */
    private Long conversationId;

    /** 回复id */
    private Long replyId;

    /**
     * 回复类型;1:知识问答 2: 知识图表
     */
    private String replyType;

    /** 统计任务id */
//    private Long statisticsTaskId;

    /** 用户id */
    private Long userId;

    /** 消息类型;1:用户 2：机器人 */
    private String type;

    /** 消息内容 */
    private String content;

    /** 是否携带上下文;0：否，1：是 */
    private String contextFlag;

    /** 是否有效;0：无效，1：有效 */
    private Boolean validFlag;

    /** 删除标志;1：已删除，0：未删除 */
    private Boolean delFlag;


}
