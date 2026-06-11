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
import tech.qiantong.qdata.module.ai.controller.admin.chat.vo.AiChatMessagePageReqVO;
import tech.qiantong.qdata.module.ai.controller.admin.chat.vo.AiChatMessageRespVO;
import tech.qiantong.qdata.module.ai.controller.admin.chat.vo.AiChatMessageSaveReqVO;
import tech.qiantong.qdata.module.ai.dal.dataobject.chat.AiChatMessageDO;

/**
 * ai聊天消息 Convert
 *
 * @author FXB
 * @date 2026-04-01
 */
@Mapper
public interface AiChatMessageConvert {
    AiChatMessageConvert INSTANCE = Mappers.getMapper(AiChatMessageConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param aiChatMessagePageReqVO 请求参数
     * @return AiChatMessageDO
     */
     AiChatMessageDO convertToDO(AiChatMessagePageReqVO aiChatMessagePageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param aiChatMessageSaveReqVO 保存请求参数
     * @return AiChatMessageDO
     */
     AiChatMessageDO convertToDO(AiChatMessageSaveReqVO aiChatMessageSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param aiChatMessageDO 实体对象
     * @return AiChatMessageRespVO
     */
     AiChatMessageRespVO convertToRespVO(AiChatMessageDO aiChatMessageDO);

    /**
     * DOList 转换为 RespVOList
     * @param aiChatMessageDOList 实体对象列表
     * @return List<AiChatMessageRespVO>
     */
     List<AiChatMessageRespVO> convertToRespVOList(List<AiChatMessageDO> aiChatMessageDOList);
}
