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
