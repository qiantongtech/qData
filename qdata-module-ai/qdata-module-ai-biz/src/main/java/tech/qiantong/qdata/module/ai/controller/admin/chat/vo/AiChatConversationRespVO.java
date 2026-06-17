/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
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
 * ai聊天对话 Response VO 对象 AI_CHAT_CONVERSATION
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
     * 数据源类型
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
     * 关联信息,格式如下
     * [{
     * "dimensionTable": "维度表名",
     * "factColumnName": "事实表外键字段名",
     * "dimensionColumnName": "维度表主键字段名",
     * "matchReason": "匹配依据"
     * }]
     */
    @Schema(description = "[{\"dimensionTable\": \"维度表名\",\"factColumnName\": \"事实表外键字段名\",\"dimensionColumnName\": \"维度表主键字段名\",\"matchReason\": \"匹配依据\"}")
    private String associations;

    /**
     * 关联条件匹配状态;0：未匹配，1：已匹配
     */
    @Schema(description = "关联条件匹配状态;0：未匹配，1：已匹配", example = "")
    private Boolean joinConditionMatchFlag;

    /**
     * 关联条件匹配类型;1：自动匹配 2:手动匹配
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
