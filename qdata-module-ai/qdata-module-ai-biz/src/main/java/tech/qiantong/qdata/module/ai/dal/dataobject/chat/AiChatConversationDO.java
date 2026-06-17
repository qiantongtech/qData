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
 * ai聊天对话 DO 对象 AI_CHAT_CONVERSATION
 *
 * @author FXB
 * @date 2026-04-01
 */
@Data
@TableName(value = "AI_CHAT_CONVERSATION")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
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
    @Schema(description = "数据源类型", example = "")
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
     * 维度表;格式 [{"tableName":"表名","tableComment":"表注释","columnName":"关联字段"}]
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

    /**
     * 是否有效;0：无效，1：有效
     */
    private Boolean validFlag;

    /**
     * 删除标志;1：已删除，0：未删除
     */
    @TableLogic
    private Boolean delFlag;


}
