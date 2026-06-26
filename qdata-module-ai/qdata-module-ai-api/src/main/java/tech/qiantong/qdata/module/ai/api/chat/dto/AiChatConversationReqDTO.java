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

package tech.qiantong.qdata.module.ai.api.chat.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

/**
 * ai聊天对话 DTO 对象 AI_CHAT_CONVERSATION
 *
 * @author FXB
 * @date 2026-04-01
 */
@Data
public class AiChatConversationReqDTO {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 对话标题
     */
    private String title;

    /**
     * 是否置顶;0：不置顶，1：置顶
     */
    private Boolean pinned;

    /**
     * 置顶时间
     */
    private Date pinnedTime;

    /**
     * 数据源id
     */
    private Long datasourceId;

    /**
     * 数据源类型
     */
    private String datasourceType;

    /**
     * 事实表名称
     */
    private String factTableName;

    /**
     * 事实表注释/事实表描述
     */
    private String factTableComment;

    /**
     * 维度表;格式 [{"tableName":"表名","tableComment":"表注释"}]
     */
    private String dimensionTable;

    /**
     * 关联信息,格式如下
     * [{
     * "dimensionTable": "维度表名",
     * "factColumnName": "事实表外键字段名",
     * "dimensionColumnName": "维度表主键字段名",
     * "matchReason": "匹配依据"
     * }]
     */
    private String associations;


    /**
     * 关联条件匹配状态;0：未匹配，1：已匹配
     */
    private Boolean joinConditionMatchFlag;

    /**
     * 关联条件匹配类型;1：自动匹配 2:手动匹配
     */
    private String joinConditionMatchType;

    /**
     * 是否有效;0：无效，1：有效
     */
    private Boolean validFlag;

    /**
     * 删除标志;1：已删除，0：未删除
     */
    private Boolean delFlag;


}
