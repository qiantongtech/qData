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
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * Evaluate Rule Result Create/Update Request VO DPP_EVALUATE_LOG
 *
 * @author qdata
 * @date 2025-07-21
 */
@Schema(description = "Evaluate Rule Result Response VO")
@Data
public class DppEvaluateLogSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "Table Name", example = "")
    @Size(max = 256, message = "Table Name length cannot exceed 256 characters")
    private String tableName;

    @Schema(description = "Column Name", example = "")
    @Size(max = 256, message = "Column Name length cannot exceed 256 characters")
    private String columnName;

    @Schema(description = "Audit Rule Code", example = "")
    @Size(max = 256, message = "Audit Rule Code length cannot exceed 256 characters")
    private String ruleCode;

    @Schema(description = "Audit Rule Name", example = "")
    @Size(max = 256, message = "Audit Rule Name length cannot exceed 256 characters")
    private String ruleName;

    @Schema(description = "Quality Dimension", example = "")
    @Size(max = 256, message = "Quality Dimension length cannot exceed 256 characters")
    private String dimensionType;

    @Schema(description = "Rule Description", example = "")
    @Size(max = 256, message = "Rule Description length cannot exceed 256 characters")
    private String ruleDescription;

    @Schema(description = "Data Quality Record ID", example = "")
    @Size(max = 256, message = "Data Quality Record ID length cannot exceed 256 characters")
    private String taskLogId;

    @Schema(description = "Evaluate ID", example = "")
    @Size(max = 256, message = "Evaluate ID length cannot exceed 256 characters")
    private String evaluateId;

    @Schema(description = "Total", example = "")
    private Long total;

    @Schema(description = "Problem Total", example = "")
    private Long problemTotal;

    @Schema(description = "Check Time", example = "")
    private Date checkDate;

    @Schema(description = "Remark", example = "")
    @Size(max = 256, message = "Remark length cannot exceed 256 characters")
    private String remark;

    @Excel(name = "Rule Custom Config, JSON Format")
    @Schema(description = "Rule Custom Config, JSON Format", example = "")
    private String rule;


    List<DppEvaluateLogStatisticsVO> dppEvaluateLogStatisticsVOList;


}
