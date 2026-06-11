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
 */

package tech.qiantong.qdata.ai.core.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <P>
 * 用途:会话创建
 * </p>
 *
 * @author: FXB
 * @create: 2026-04-11 11:16
 **/
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Schema(description = "会话创建 Response VO")
public class AiChatConversationSaveRespVO {

    @Schema(description = "会话id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "创建会话状态码；200：成功 10001：匹配字段关联信息识别失败", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long code;
}
