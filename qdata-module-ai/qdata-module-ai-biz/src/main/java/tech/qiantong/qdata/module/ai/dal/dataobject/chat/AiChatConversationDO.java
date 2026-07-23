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

package tech.qiantong.qdata.module.ai.dal.dataobject.chat;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * ai chat conversation DO object AI_CHAT_CONVERSATION
 *
 * @author FXB
 * @date 2026-04-01
 */
@Data
@TableName(value = "AI_CHAT_CONVERSATION")
// Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you do not need to write it.
// @KeySequence("AI_CHAT_CONVERSATION_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AiChatConversationDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
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
    @Schema(description = "数据源类型", example = "")
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
     * Dimension table; format [{"tableName":"table name","tableComment":"table comment","columnName":"associated field"}]
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
    @Schema(description = "[{\"dimensionTable\": \"维度表名\",\"factColumnName\": \"事实表外键字段名\",\"dimensionColumnName\": \"维度表主键字段名\",\"matchReason\": \"匹配依据\"}")
    private String associations;

    /**
     * Association condition matching status; 0: not matched, 1: matched
     */
    @Schema(description = "关联条件匹配状态;0：未匹配，1：已匹配", example = "")
    private Boolean joinConditionMatchFlag;

    /**
     * Association condition matching type; 1: automatic matching 2: manual matching
     */
    @Schema(description = "关联条件匹配类型;1：自动匹配 2:手动匹配,字典：ai_chat_coversation_jcm_type", example = "")
    private String joinConditionMatchType;

    /**
     * Whether it is valid; 0: invalid, 1: valid
     */
    private Boolean validFlag;

    /**
     * Deletion flag; 1: deleted, 0: not deleted
     */
    @TableLogic
    private Boolean delFlag;


}
