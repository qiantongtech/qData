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
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
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
