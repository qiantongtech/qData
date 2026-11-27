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
 * Business License: https://docs.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "数据集成任务实例日志行 Response VO")
public class DppEtlTaskInstanceLogLineRespVO {

    @Schema(description = "行号", example = "1")
    private Integer lineNo;

    @Schema(description = "日志时间", example = "09:30:46.123")
    private String logTime;

    @Schema(description = "错误等级：INFO、WARN、ERROR", example = "INFO")
    private String level;

    @Schema(description = "日志处理阶段", example = "表输入组件")
    private String stage;

    @Schema(description = "日志内容", example = "任务启动，节点初始化完成，准备开始数据处理")
    private String content;

    @Schema(description = "日志详情内容，错误日志保留完整堆栈信息", example = "任务启动，节点初始化完成，准备开始数据处理")
    private String detailContent;
}

