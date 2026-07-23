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
 * ai chat conversation DTO object AI_CHAT_CONVERSATION
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
     * User id
     */
    private Long userId;

    /**
     * Conversation title
     */
    private String title;

    /**
     * Whether the conversation is pinned; 0: not pinned, 1: pinned
     */
    private Boolean pinned;

    /**
     * Pin time
     */
    private Date pinnedTime;

    /**
     * Data source id
     */
    private Long datasourceId;

    /**
     * Data source type
     */
    private String datasourceType;

    /**
     * Fact table name
     */
    private String factTableName;

    /**
     * Fact table annotation/fact table description
     */
    private String factTableComment;

    /**
     * Dimension table; format [{"tableName":"table name","tableComment":"table comment"}]
     */
    private String dimensionTable;

    /**
     * Related information, the format is as follows
     * [{
     * "dimensionTable": "Dimension table name",
     * "factColumnName": "Fact table foreign key field name",
     * "dimensionColumnName": "Dimension table primary key field name",
     * "matchReason": "matching basis"
     * }]
     */
    private String associations;


    /**
     * Association condition matching status; 0: not matched, 1: matched
     */
    private Boolean joinConditionMatchFlag;

    /**
     * Association condition matching type; 1: automatic matching 2: manual matching
     */
    private String joinConditionMatchType;

    /**
     * Whether it is valid; 0: invalid, 1: valid
     */
    private Boolean validFlag;

    /**
     * Deletion flag; 1: deleted, 0: not deleted
     */
    private Boolean delFlag;


}
