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

package tech.qiantong.qdata.module.system.api.message.dto;

import lombok.Data;

/**
 * 消息 DTO 对象 message
 *
 * @author qdata
 * @date 2024-10-31
 */
@Data
public class MessageRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 发送人 */
    private Long senderId;

    /** 接收人 */
    private Long receiverId;

    /** 消息标题 */
    private String title;

    /** 消息模板内容 */
    private String content;

    /** 消息类别 */
    private Integer category;

    /** 消息等级 */
    private Integer msgLevel;

    /** 消息模块 */
    private Integer module;

    /** 实体类型 */
    private Integer entityType;

    /** 实体id */
    private Long entityId;

    /** 消息链接 */
    private String entityUrl;

    /** 是否已读 */
    private Integer hasRead;

    /** 是否撤回 */
    private Integer hasRetraction;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
