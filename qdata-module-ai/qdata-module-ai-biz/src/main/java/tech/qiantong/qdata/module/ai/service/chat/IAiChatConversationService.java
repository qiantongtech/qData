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
import tech.qiantong.qdata.module.ai.controller.admin.chat.vo.AiChatConversationRespVO;
import tech.qiantong.qdata.module.ai.controller.admin.chat.vo.AiChatConversationSaveReqVO;
import tech.qiantong.qdata.module.ai.controller.admin.chat.vo.AiChatConversationPageReqVO;
import tech.qiantong.qdata.module.ai.dal.dataobject.chat.AiChatConversationDO;

/**
 * ai chat conversation service interface
 *
 * @author FXB
 * @date 2026-04-01
 */
public interface IAiChatConversationService extends IService<AiChatConversationDO> {

    /**
     * Get a paginated list of ai chat conversations
     *
     * @param pageReqVO paging request
     * @return ai chat conversation paginated list
     */
    PageResult<AiChatConversationDO> getAiChatConversationPage(AiChatConversationPageReqVO pageReqVO);

    /**
     * Create ai chat conversation
     *
     * @param createReqVO ai chat conversation information
     * @return AI chat conversation ID
     */
    Long createAiChatConversation(AiChatConversationSaveReqVO createReqVO);

    /**
     * Update ai chat conversation
     *
     * @param updateReqVO ai chat conversation information
     */
    int updateAiChatConversation(AiChatConversationSaveReqVO updateReqVO);

    /**
     * Delete ai chat conversation
     *
     * @param idList AI chat conversation IDs
     */
    int removeAiChatConversation(Collection<Long> idList);

    /**
     * Get ai chat conversation details
     *
     * @param id AI chat conversation ID
     * @return ai chat conversation
     */
    AiChatConversationDO getAiChatConversationById(Long id);

    /**
     * Get a list of all ai chat conversations
     *
     * @return ai chat conversation list
     */
    List<AiChatConversationDO> getAiChatConversationList();

    /**
     * Get all ai chat conversation map
     *
     * @return ai chat conversation map
     */
    Map<Long, AiChatConversationDO> getAiChatConversationMap();

    /**
     * Get the AI chat conversation list by user ID
     *
     * @param userId
     * @return
     */
    List<AiChatConversationDO> getChatConversationListByUserId(Long userId);
}
