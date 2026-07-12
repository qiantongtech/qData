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

package tech.qiantong.qdata.module.system.convert.message;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.system.controller.admin.system.message.vo.MessagePageReqVO;
import tech.qiantong.qdata.module.system.controller.admin.system.message.vo.MessageRespVO;
import tech.qiantong.qdata.module.system.controller.admin.system.message.vo.MessageSaveReqVO;
import tech.qiantong.qdata.module.system.dal.dataobject.message.MessageDO;

import java.util.List;

/**
 * Message Convert
 *
 * @author qdata
 * @date 2024-10-31
 */
@Mapper
public interface MessageConvert {
    MessageConvert INSTANCE = Mappers.getMapper(MessageConvert.class);

    /**
     * PageReqVO Convert to DO
     * @param messagePageReqVO request parameters
     * @return MessageDO
     */
     MessageDO convertToDO(MessagePageReqVO messagePageReqVO);

    /**
     * SaveReqVO Convert to DO
     * @param messageSaveReqVO save request parameters
     * @return MessageDO
     */
     MessageDO convertToDO(MessageSaveReqVO messageSaveReqVO);

    /**
     * DO Convert to RespVO
     * @param messageDO entity object
     * @return MessageRespVO
     */
     MessageRespVO convertToRespVO(MessageDO messageDO);

    /**
     * DOList Convert to RespVOList
     * @param messageDOList entity object list
     * @return List<MessageRespVO>
     */
     List<MessageRespVO> convertToRespVOList(List<MessageDO> messageDOList);
}
