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
 * ai chat message DTO object AI_CHAT_MESSAGE
 *
 * @author FXB
 * @date 2026-04-01
 */
@Data
public class AiChatMessageRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Conversation id */
    private Long conversationId;

    /** reply id */
    private Long replyId;

    /**
     * Reply type; 1: Knowledge Q&A 2: Knowledge graph
     */
    private String replyType;

    /** Statistics task id */
//    private Long statisticsTaskId;

    /** User ID */
    private Long userId;

    /** Message type; 1: User 2: Robot */
    private String type;

    /** Message content */
    private String content;

    /** Whether to carry context; 0: no, 1: yes */
    private String contextFlag;

    /** Whether it is valid; 0: invalid, 1: valid */
    private Boolean validFlag;

    /** Deletion flag; 1: deleted, 0: not deleted */
    private Boolean delFlag;


}
