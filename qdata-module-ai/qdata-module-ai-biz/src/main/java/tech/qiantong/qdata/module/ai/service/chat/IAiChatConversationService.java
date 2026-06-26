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
 * ai聊天对话Service接口
 *
 * @author FXB
 * @date 2026-04-01
 */
public interface IAiChatConversationService extends IService<AiChatConversationDO> {

    /**
     * 获得ai聊天对话分页列表
     *
     * @param pageReqVO 分页请求
     * @return ai聊天对话分页列表
     */
    PageResult<AiChatConversationDO> getAiChatConversationPage(AiChatConversationPageReqVO pageReqVO);

    /**
     * 创建ai聊天对话
     *
     * @param createReqVO ai聊天对话信息
     * @return ai聊天对话编号
     */
    Long createAiChatConversation(AiChatConversationSaveReqVO createReqVO);

    /**
     * 更新ai聊天对话
     *
     * @param updateReqVO ai聊天对话信息
     */
    int updateAiChatConversation(AiChatConversationSaveReqVO updateReqVO);

    /**
     * 删除ai聊天对话
     *
     * @param idList ai聊天对话编号
     */
    int removeAiChatConversation(Collection<Long> idList);

    /**
     * 获得ai聊天对话详情
     *
     * @param id ai聊天对话编号
     * @return ai聊天对话
     */
    AiChatConversationDO getAiChatConversationById(Long id);

    /**
     * 获得全部ai聊天对话列表
     *
     * @return ai聊天对话列表
     */
    List<AiChatConversationDO> getAiChatConversationList();

    /**
     * 获得全部ai聊天对话 Map
     *
     * @return ai聊天对话 Map
     */
    Map<Long, AiChatConversationDO> getAiChatConversationMap();

    /**
     * 通过用户id获取ai聊天对话列表
     *
     * @param userId
     * @return
     */
    List<AiChatConversationDO> getChatConversationListByUserId(Long userId);
}
