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

package tech.qiantong.qdata.module.ai.controller.admin.chat.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * ai chat conversation Request VO object AI_CHAT_CONVERSATION
 *
 * @author FXB
 * @date 2026-04-01
 */
@Schema(description = "ai聊天对话 Request VO")
@Data
public class AiChatConversationPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;
    @Schema(description = "用户id", example = "")
    private Long userId;

    @Schema(description = "对话标题", example = "")
    private String title;

    @Schema(description = "是否置顶;0：不置顶，1：置顶", example = "")
    private Boolean pinned;

    @Schema(description = "置顶时间", example = "")
    private Date pinnedTime;

    @Schema(description = "数据源id", example = "")
    private Long datasourceId;

    /**
     * Data source type
     */
    @Schema(description = "数据源类型", example = "")
    private String datasourceType;

    @Schema(description = "事实表名称", example = "")
    private String factTableName;

    @Schema(description = "事实表注释/事实表描述", example = "")
    private String factTableComment;

    @Schema(description = "维度表;格式 [{\"tableName\":\"表名\",\"tableComment\":\"表注释\"}]", example = "")
    private String dimensionTable;

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

    @Schema(description = "是否有效;0：无效，1：有效", example = "")
    private Boolean validFlag;


}
