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

package tech.qiantong.qdata.ai.core.service;

import tech.qiantong.qdata.ai.core.vo.AiChatConversationSaveRespVO;
import tech.qiantong.qdata.module.ai.controller.admin.chat.vo.AiChatConversationSaveReqVO;

/**
 * 会话服务
 *
 * @author fabian
 */
public interface IChatConversationService {

    /**
     * 创建会话
     *
     * @param reqVO
     * @return
     */
    AiChatConversationSaveRespVO createAiChatConversation(AiChatConversationSaveReqVO reqVO);
}
