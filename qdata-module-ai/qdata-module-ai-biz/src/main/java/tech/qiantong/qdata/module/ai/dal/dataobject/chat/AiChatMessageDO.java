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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * ai chat message DO object AI_CHAT_MESSAGE
 *
 * @author FXB
 * @date 2026-04-01
 */
@Data
@TableName(value = "AI_CHAT_MESSAGE")
// Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you do not need to write it.
// @KeySequence("AI_CHAT_MESSAGE_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AiChatMessageDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** Conversation id */
    private Long conversationId;

    /** reply id */
    private Long replyId;

    /**
     * Reply type; 1: Knowledge Q&A 2: Knowledge graph
     */
    @Schema(description = "回复类型;1:知识问答 2: 知识图表", example = "")
    private String replyType;

// /** Statistics task id */
//    private Long statisticsTaskId;

    /** User ID */
    private Long userId;

    /** Message type; 1: User 2: Robot */
    private String type;

    /** Message content */
    private String content;

    /** Whether to carry context; 0: no, 1: yes */
    private String contextFlag;

    /** Whether it is valid; 0: invalid, 1: valid */
    private Boolean validFlag;

    /** Deletion flag; 1: deleted, 0: not deleted */
    @TableLogic
    private Boolean delFlag;


}
