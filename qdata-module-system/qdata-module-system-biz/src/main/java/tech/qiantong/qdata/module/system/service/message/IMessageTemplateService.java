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

package tech.qiantong.qdata.module.system.service.message;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.system.controller.admin.system.message.vo.MessageTemplatePageReqVO;
import tech.qiantong.qdata.module.system.convert.message.MessageTemplateConvert;
import tech.qiantong.qdata.module.system.dal.dataobject.message.MessageTemplateDO;

import java.util.List;

/**
 * 消息模板Service接口
 *
 * @author qdata
 * @date 2024-10-31
 */
public interface IMessageTemplateService extends IService<MessageTemplateDO> {

    default PageResult<MessageTemplateDO> getMessageTemplatePage(MessageTemplatePageReqVO messageTemplate) {
        QueryWrapper<MessageTemplateDO> queryWrapper = new QueryWrapper<>(MessageTemplateConvert.INSTANCE.convertToDO(messageTemplate));
        List<MessageTemplateDO> list = list(queryWrapper);

        return new PageResult(list, new PageInfo(list).getTotal());
    }
}
