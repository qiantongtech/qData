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
import tech.qiantong.qdata.common.annotation.Excel;
import tech.qiantong.qdata.common.core.page.PageParam;

import java.util.Date;

/**
 * Evaluate Rule Result Request VO Object DPP_EVALUATE_LOG
 *
 * @author qdata
 * @date 2025-07-21
 */
@Schema(description = "Evaluate Rule Result Request VO")
@Data
public class DppEvaluateLogPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "Table Name", example = "")
    private String tableName;

    @Schema(description = "Column Name", example = "")
    private String columnName;

    @Schema(description = "Audit Rule Code", example = "")
    private String ruleCode;

    @Schema(description = "Audit Rule Name", example = "")
    private String ruleName;

    @Schema(description = "Quality Dimension", example = "")
    private String dimensionType;

    @Schema(description = "Rule Description", example = "")
    private String ruleDescription;

    @Schema(description = "Data Quality Record ID", example = "")
    private String taskLogId;

    @Schema(description = "Evaluate ID", example = "")
    private String evaluateId;

    @Schema(description = "Total", example = "")
    private Long total;

    @Schema(description = "Problem Total", example = "")
    private Long problemTotal;

    @Schema(description = "Check Time", example = "")
    private Date checkDate;

    @Excel(name = "Rule Custom Config, JSON Format")
    private String rule;


}
