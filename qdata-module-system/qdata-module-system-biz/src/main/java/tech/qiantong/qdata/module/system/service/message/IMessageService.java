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
import tech.qiantong.qdata.module.system.controller.admin.system.message.vo.MessagePageReqVO;
import tech.qiantong.qdata.module.system.controller.admin.system.message.vo.MessageSaveReqVO;
import tech.qiantong.qdata.module.system.convert.message.MessageConvert;
import tech.qiantong.qdata.module.system.dal.dataobject.message.MessageDO;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Message Service interface
 *
 * @author qdata
 * @date 2024-10-31
 */
public interface IMessageService extends IService<MessageDO> {

    default PageResult<MessageDO> getMessagePage(MessagePageReqVO message) {
        QueryWrapper<MessageDO> qw = new QueryWrapper<>(MessageConvert.INSTANCE.convertToDO(message));

        Date startTime = message.getStartTime();
        Date endTime   = message.getEndTime();

        if (startTime != null || endTime != null) {
            if (startTime != null) {
                // >= current day 00:00:00
                Calendar cal = Calendar.getInstance();
                cal.setTime(startTime);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                qw.ge("create_time", cal.getTime());
            }
            if (endTime != null) {
                // <= current day 23:59:59.999
                Calendar cal = Calendar.getInstance();
                cal.setTime(endTime);
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                cal.set(Calendar.MILLISECOND, 999);
                qw.le("create_time", cal.getTime());
            }
        }

        List<MessageDO> list = list(qw);
        return new PageResult<>(list, new PageInfo<>(list).getTotal());
    }

    /**
     * Send a message to a specific user via template
     * @param templateId template id
     * @param messageSaveReqVO message creation request
     * @param entity entity object
     * @return whether send succeeded
     */
    public Boolean send(Long templateId, MessageSaveReqVO messageSaveReqVO, Object entity);

    /**
     * Query message count
     * @param message query criteria
     * @return count
     */
    public Long getNum(MessagePageReqVO message);

    /**
     * Mark as read
     * @param id message id
     * @return whether succeeded
     */
    public Boolean read(Long id);

    /**
     * Mark all as read
     * @param receiverId receiver id
     * @param category message type
     * @param module message module
     * @return whether succeeded
     */
    public Boolean readAll(Long receiverId, Integer category, Integer module);

    /**
     * Update receiver's unread message count
     *
     * @param receiverId receiver id
     */
    public void getReceiverWDNum(Long receiverId);

}
