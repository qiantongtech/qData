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
 * ai聊天消息Service接口
 *
 * @author FXB
 * @date 2026-04-01
 */
public interface IAiChatMessageService extends IService<AiChatMessageDO> {

    /**
     * 获得ai聊天消息分页列表
     *
     * @param pageReqVO 分页请求
     * @return ai聊天消息分页列表
     */
    PageResult<AiChatMessageDO> getAiChatMessagePage(AiChatMessagePageReqVO pageReqVO);

    /**
     * 创建ai聊天消息
     *
     * @param createReqVO ai聊天消息信息
     * @return ai聊天消息编号
     */
    Long createAiChatMessage(AiChatMessageSaveReqVO createReqVO);

    /**
     * 更新ai聊天消息
     *
     * @param updateReqVO ai聊天消息信息
     */
    int updateAiChatMessage(AiChatMessageSaveReqVO updateReqVO);

    /**
     * 删除ai聊天消息
     *
     * @param idList ai聊天消息编号
     */
    int removeAiChatMessage(Collection<Long> idList);

    /**
     * 获得ai聊天消息详情
     *
     * @param id ai聊天消息编号
     * @return ai聊天消息
     */
    AiChatMessageDO getAiChatMessageById(Long id);

    /**
     * 获得全部ai聊天消息列表
     *
     * @return ai聊天消息列表
     */
    List<AiChatMessageDO> getAiChatMessageList();

    /**
     * 获得全部ai聊天消息 Map
     *
     * @return ai聊天消息 Map
     */
    Map<Long, AiChatMessageDO> getAiChatMessageMap();

    /**
     * 获得指定会话的ai聊天消息列表
     *
     * @param conversationId 会话编号
     * @return ai聊天消息列表
     */
    List<AiChatMessageDO> getChatMessageListByConversationId(Long conversationId);
}
