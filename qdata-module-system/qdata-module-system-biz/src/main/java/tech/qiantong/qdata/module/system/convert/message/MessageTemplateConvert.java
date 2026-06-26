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
import tech.qiantong.qdata.module.system.controller.admin.system.message.vo.MessageTemplatePageReqVO;
import tech.qiantong.qdata.module.system.controller.admin.system.message.vo.MessageTemplateRespVO;
import tech.qiantong.qdata.module.system.controller.admin.system.message.vo.MessageTemplateSaveReqVO;
import tech.qiantong.qdata.module.system.dal.dataobject.message.MessageTemplateDO;

import java.util.List;

/**
 * 消息模板 Convert
 *
 * @author qdata
 * @date 2024-10-31
 */
@Mapper
public interface MessageTemplateConvert {
    MessageTemplateConvert INSTANCE = Mappers.getMapper(MessageTemplateConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param messageTemplatePageReqVO 请求参数
     * @return MessageTemplateDO
     */
     MessageTemplateDO convertToDO(MessageTemplatePageReqVO messageTemplatePageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param messageTemplateSaveReqVO 保存请求参数
     * @return MessageTemplateDO
     */
     MessageTemplateDO convertToDO(MessageTemplateSaveReqVO messageTemplateSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param messageTemplateDO 实体对象
     * @return MessageTemplateRespVO
     */
     MessageTemplateRespVO convertToRespVO(MessageTemplateDO messageTemplateDO);

    /**
     * DOList 转换为 RespVOList
     * @param messageTemplateDOList 实体对象列表
     * @return List<MessageTemplateRespVO>
     */
     List<MessageTemplateRespVO> convertToRespVOList(List<MessageTemplateDO> messageTemplateDOList);
}
