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

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Schema(description = "数据集成任务统计 Response VO")
public class DppEtlTaskStatisticsRespVO {

    @Schema(description = "运行中的任务数")
    private Long runningCount;

    @Schema(description = "最近一次执行结果为失败的任务数")
    private Long todayErrorCount;

    @Schema(description = "今日执行次数")
    private Long todayExecuteCount;

    @Schema(description = "今日成功率")
    private BigDecimal todaySuccessRate;

    @Schema(description = "数据统计截止时间，返回当前服务器时间", example = "2026-07-08 09:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date statisticsTime;
}

