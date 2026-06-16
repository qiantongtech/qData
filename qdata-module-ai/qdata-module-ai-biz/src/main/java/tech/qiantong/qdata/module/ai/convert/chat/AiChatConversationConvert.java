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
