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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * ai聊天消息 创建/修改 Request VO AI_CHAT_MESSAGE
 *
 * @author FXB
 * @date 2026-04-01
 */
@Schema(description = "ai聊天消息 Response VO")
@Data
public class AiChatMessageSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "对话id", example = "")
    private Long conversationId;

    @Schema(description = "回复id", example = "")
    private Long replyId;

    /**
     * 回复类型;1:知识问答 2: 知识图表
     */
    @Schema(description = "回复类型;1:知识问答 2: 知识图表", example = "")
    private String replyType;

//    @Schema(description = "统计任务id", example = "")
//    private Long statisticsTaskId;

    @Schema(description = "用户id", example = "")
    private Long userId;

    @Schema(description = "消息类型;1:用户 2：机器人", example = "")
    @Size(max = 256, message = "消息类型;1:用户 2：机器人长度不能超过256个字符")
    private String type;

    @Schema(description = "消息内容", example = "")
    @Size(max = 256, message = "消息内容长度不能超过256个字符")
    private String content;

    @Schema(description = "是否携带上下文;0：否，1：是", example = "")
    @Size(max = 256, message = "是否携带上下文;0：否，1：是长度不能超过256个字符")
    private String contextFlag;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
