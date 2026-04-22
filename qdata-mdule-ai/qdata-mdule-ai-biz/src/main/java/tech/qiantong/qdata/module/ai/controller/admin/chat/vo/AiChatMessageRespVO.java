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

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import tech.qiantong.qdata.common.annotation.Excel;
import java.util.Date;
import java.io.Serializable;

/**
 * ai聊天消息 Response VO 对象 AI_CHAT_MESSAGE
 *
 * @author FXB
 * @date 2026-04-01
 */
@Schema(description = "ai聊天消息 Response VO")
@Data
public class AiChatMessageRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "对话id")
    @Schema(description = "对话id", example = "")
    private Long conversationId;

    @Excel(name = "回复id")
    @Schema(description = "回复id", example = "")
    private Long replyId;

    /**
     * 回复类型;1:知识问答 2: 知识图表
     */
    @Schema(description = "回复类型;1:知识问答 2: 知识图表", example = "")
    private String replyType;

//    @Excel(name = "统计任务id")
//    @Schema(description = "统计任务id", example = "")
//    private Long statisticsTaskId;

    @Excel(name = "用户id")
    @Schema(description = "用户id", example = "")
    private Long userId;

    @Excel(name = "消息类型;1:用户 2：机器人")
    @Schema(description = "消息类型;1:用户 2：机器人", example = "")
    private String type;

    @Excel(name = "消息内容")
    @Schema(description = "消息内容", example = "")
    private String content;

    @Excel(name = "是否携带上下文;0：否，1：是")
    @Schema(description = "是否携带上下文;0：否，1：是", example = "")
    private String contextFlag;

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
