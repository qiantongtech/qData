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

package tech.qiantong.qdata.module.ai.convert.chat;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.ai.controller.admin.chat.vo.AiChatConversationPageReqVO;
import tech.qiantong.qdata.module.ai.controller.admin.chat.vo.AiChatConversationRespVO;
import tech.qiantong.qdata.module.ai.controller.admin.chat.vo.AiChatConversationSaveReqVO;
import tech.qiantong.qdata.module.ai.dal.dataobject.chat.AiChatConversationDO;

/**
 * ai聊天对话 Convert
 *
 * @author FXB
 * @date 2026-04-01
 */
@Mapper
public interface AiChatConversationConvert {
    AiChatConversationConvert INSTANCE = Mappers.getMapper(AiChatConversationConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param aiChatConversationPageReqVO 请求参数
     * @return AiChatConversationDO
     */
     AiChatConversationDO convertToDO(AiChatConversationPageReqVO aiChatConversationPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param aiChatConversationSaveReqVO 保存请求参数
     * @return AiChatConversationDO
     */
     AiChatConversationDO convertToDO(AiChatConversationSaveReqVO aiChatConversationSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param aiChatConversationDO 实体对象
     * @return AiChatConversationRespVO
     */
     AiChatConversationRespVO convertToRespVO(AiChatConversationDO aiChatConversationDO);

    /**
     * DOList 转换为 RespVOList
     * @param aiChatConversationDOList 实体对象列表
     * @return List<AiChatConversationRespVO>
     */
     List<AiChatConversationRespVO> convertToRespVOList(List<AiChatConversationDO> aiChatConversationDOList);
}
