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
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import tech.qiantong.qdata.common.annotation.Excel;

import java.util.Date;
import java.io.Serializable;

/**
 * ai chat conversation Response VO object AI_CHAT_CONVERSATION
 *
 * @author FXB
 * @date 2026-04-01
 */
@Schema(description = "ai聊天对话 Response VO")
@Data
public class AiChatConversationRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "用户id")
    @Schema(description = "用户id", example = "")
    private Long userId;

    @Excel(name = "对话标题")
    @Schema(description = "对话标题", example = "")
    private String title;

    @Excel(name = "是否置顶;0：不置顶，1：置顶")
    @Schema(description = "是否置顶;0：不置顶，1：置顶", example = "")
    private Boolean pinned;

    @Excel(name = "置顶时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "置顶时间", example = "")
    private Date pinnedTime;

    @Excel(name = "数据源id")
    @Schema(description = "数据源id", example = "")
    private Long datasourceId;

    /**
     * Data source type
     */
    @Schema(description = "数据源类型", example = "")
    private String datasourceType;

    @Excel(name = "事实表名称")
    @Schema(description = "事实表名称", example = "")
    private String factTableName;

    @Excel(name = "事实表注释/事实表描述")
    @Schema(description = "事实表注释/事实表描述", example = "")
    private String factTableComment;

    @Excel(name = "关联维度表;")
    @Schema(description = "维度表;格式 [{\"tableName\":\"表名\",\"tableComment\":\"表注释\"}]", example = "")
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

    @Excel(name = "是否有效;0：无效，1：有效")
    @Schema(description = "是否有效;0：无效，1：有效", example = "")
    private Boolean validFlag;

    @Excel(name = "删除标志;1：已删除，0：未删除")
    @Schema(description = "删除标志;1：已删除，0：未删除", example = "")
    private Boolean delFlag;

    @Excel(name = "创建人")
    @Schema(description = "创建人", example = "")
    private String createBy;

    @Excel(name = "创建人id")
    @Schema(description = "创建人id", example = "")
    private Long creatorId;

    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "")
    private Date createTime;

    @Excel(name = "更新人")
    @Schema(description = "更新人", example = "")
    private String updateBy;

    @Excel(name = "更新人id")
    @Schema(description = "更新人id", example = "")
    private Long updaterId;

    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", example = "")
    private Date updateTime;

    @Excel(name = "备注")
    @Schema(description = "备注", example = "")
    private String remark;

}
