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

package tech.qiantong.qdata.module.ai.service.chat;

import java.util.List;
import java.util.Map;
import java.util.Collection;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.ai.controller.admin.chat.vo.AiChatMessageRespVO;
import tech.qiantong.qdata.module.ai.controller.admin.chat.vo.AiChatMessageSaveReqVO;
import tech.qiantong.qdata.module.ai.controller.admin.chat.vo.AiChatMessagePageReqVO;
import tech.qiantong.qdata.module.ai.dal.dataobject.chat.AiChatMessageDO;

/**
 * ai chat message service interface
 *
 * @author FXB
 * @date 2026-04-01
 */
public interface IAiChatMessageService extends IService<AiChatMessageDO> {

    /**
     * Get a paginated list of ai chat messages
     *
     * @param pageReqVO paging request
     * @return ai chat message paginated list
     */
    PageResult<AiChatMessageDO> getAiChatMessagePage(AiChatMessagePageReqVO pageReqVO);

    /**
     * Create ai chat message
     *
     * @param createReqVO ai chat message information
     * @return AI chat message ID
     */
    Long createAiChatMessage(AiChatMessageSaveReqVO createReqVO);

    /**
     * Update ai chat message
     *
     * @param updateReqVO ai chat message information
     */
    int updateAiChatMessage(AiChatMessageSaveReqVO updateReqVO);

    /**
     * Delete ai chat messages
     *
     * @param idList AI chat message IDs
     */
    int removeAiChatMessage(Collection<Long> idList);

    /**
     * Get ai chat message details
     *
     * @param id AI chat message ID
     * @return ai chat message
     */
    AiChatMessageDO getAiChatMessageById(Long id);

    /**
     * Get a list of all ai chat messages
     *
     * @return ai chat message list
     */
    List<AiChatMessageDO> getAiChatMessageList();

    /**
     * Get all ai chat messages Map
     *
     * @return ai chat message Map
     */
    Map<Long, AiChatMessageDO> getAiChatMessageMap();

    /**
     * Get the ai chat message list of the specified session
     *
     * @param conversationId conversation ID
     * @return ai chat message list
     */
    List<AiChatMessageDO> getChatMessageListByConversationId(Long conversationId);
}
