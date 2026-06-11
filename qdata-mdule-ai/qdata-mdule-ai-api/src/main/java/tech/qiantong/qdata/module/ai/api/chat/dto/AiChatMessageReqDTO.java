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

package tech.qiantong.qdata.module.ai.api.chat.dto;

import lombok.*;

/**
 * ai聊天消息 DTO 对象 AI_CHAT_MESSAGE
 *
 * @author FXB
 * @date 2026-04-01
 */
@Data
public class AiChatMessageReqDTO {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 对话id
     */
    private Long conversationId;

    /**
     * 回复id
     */
    private Long replyId;

    /**
     * 回复类型;1:知识问答 2: 知识图表
     */
    private String replyType;

    /**
     * 统计任务id
     */
//    private Long statisticsTaskId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 消息类型;1:用户 2：机器人
     */
    private String type;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 是否携带上下文;0：否，1：是
     */
    private String contextFlag;

    /**
     * 是否有效;0：无效，1：有效
     */
    private Boolean validFlag;

    /**
     * 删除标志;1：已删除，0：未删除
     */
    private Boolean delFlag;


}
