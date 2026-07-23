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
 * ai chat message Convert
 *
 * @author FXB
 * @date 2026-04-01
 */
@Mapper
public interface AiChatMessageConvert {
    AiChatMessageConvert INSTANCE = Mappers.getMapper(AiChatMessageConvert.class);

    /**
     * PageReqVO converted to DO
     * @param aiChatMessagePageReqVO request parameters
     * @return AiChatMessageDO
     */
     AiChatMessageDO convertToDO(AiChatMessagePageReqVO aiChatMessagePageReqVO);

    /**
     * SaveReqVO converted to DO
     * @param aiChatMessageSaveReqVO save request parameters
     * @return AiChatMessageDO
     */
     AiChatMessageDO convertToDO(AiChatMessageSaveReqVO aiChatMessageSaveReqVO);

    /**
     * DO to RespVO
     * @param aiChatMessageDO entity object
     * @return AiChatMessageRespVO
     */
     AiChatMessageRespVO convertToRespVO(AiChatMessageDO aiChatMessageDO);

    /**
     * DOList to RespVOList
     * @param aiChatMessageDOList entity object list
     * @return List<AiChatMessageRespVO>
     */
     List<AiChatMessageRespVO> convertToRespVOList(List<AiChatMessageDO> aiChatMessageDOList);
}
