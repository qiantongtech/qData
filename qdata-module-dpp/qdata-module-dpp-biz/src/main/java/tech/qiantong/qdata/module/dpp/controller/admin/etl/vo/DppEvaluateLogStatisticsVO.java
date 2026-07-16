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

package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * Evaluate Rule Result Statistics VO DPP_EVALUATE_LOG
 *
 * @author qdata
 * @date 2025-07-21
 */
@Schema(description = "Evaluate Rule Result Response VO")
@Data
public class DppEvaluateLogStatisticsVO {


    @Schema(description = "Quality Dimension", example = "")
    @Size(max = 256, message = "Quality Dimension length cannot exceed 256 characters")
    private String dimensionType;


    @Schema(description = "Total", example = "")
    private Long total;

    @Schema(description = "Problem Total", example = "")
    private Long problemTotal;

    /**
     * Rule Count
     */
    private Long succesTotal;

    // Proportion
    private BigDecimal proportion;

    // Trend 0: Down, 1: Up
    private Long trendType;



}
