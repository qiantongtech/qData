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

package tech.qiantong.qdata.module.system.service.message.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.module.system.dal.dataobject.message.MessageTemplateDO;
import tech.qiantong.qdata.module.system.dal.mapper.message.MessageTemplateMapper;
import tech.qiantong.qdata.module.system.service.message.IMessageTemplateService;

import javax.annotation.Resource;

/**
 * 消息模板Service业务层处理
 *
 * @author qdata
 * @date 2024-10-31
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class MessageTemplateServiceImpl  extends ServiceImpl<MessageTemplateMapper,MessageTemplateDO> implements IMessageTemplateService {
    @Resource
    private MessageTemplateMapper messageTemplateMapper;

}
