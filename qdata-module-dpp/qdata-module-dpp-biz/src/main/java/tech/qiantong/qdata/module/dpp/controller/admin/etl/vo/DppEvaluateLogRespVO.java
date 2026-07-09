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

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Evaluate Rule Result Response VO Object DPP_EVALUATE_LOG
 *
 * @author qdata
 * @date 2025-07-21
 */
@Schema(description = "Evaluate Rule Result Response VO")
@Data
public class DppEvaluateLogRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "Table Name")
    @Schema(description = "Table Name", example = "")
    private String tableName;
    private Long datasourceId;

    @Excel(name = "Column Name")
    @Schema(description = "Column Name", example = "")
    private String columnName;

    @Excel(name = "Audit Rule Code")
    @Schema(description = "Audit Rule Code", example = "")
    private String ruleCode;

    @Excel(name = "Audit Rule Name")
    @Schema(description = "Audit Rule Name", example = "")
    private String ruleName;

    @Excel(name = "Quality Dimension")
    @Schema(description = "Quality Dimension", example = "")
    private String dimensionType;

    @Excel(name = "Rule Description")
    @Schema(description = "Rule Description", example = "")
    private String ruleDescription;

    @Excel(name = "Data Quality Record ID")
    @Schema(description = "Data Quality Record ID", example = "")
    private String taskLogId;

    @Excel(name = "Evaluate ID")
    @Schema(description = "Evaluate ID", example = "")
    private String evaluateId;

    @Excel(name = "Total")
    @Schema(description = "Total", example = "")
    private Long total;

    @Excel(name = "Problem Total")
    @Schema(description = "Problem Total", example = "")
    private Long problemTotal;

    @Excel(name = "Check Time", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Check Time", example = "")
    private Date checkDate;

    @Excel(name = "Valid")
    @Schema(description = "Valid", example = "")
    private Boolean validFlag;

    @Excel(name = "Delete Flag")
    @Schema(description = "Delete Flag", example = "")
    private Boolean delFlag;

    @Excel(name = "Created By")
    @Schema(description = "Created By", example = "")
    private String createBy;

    @Excel(name = "Creator ID")
    @Schema(description = "Creator ID", example = "")
    private Long creatorId;

    @Excel(name = "Create Time", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Create Time", example = "")
    private Date createTime;

    @Excel(name = "Updated By")
    @Schema(description = "Updated By", example = "")
    private String updateBy;

    @Excel(name = "Updater ID")
    @Schema(description = "Updater ID", example = "")
    private Long updaterId;

    @Excel(name = "Update Time", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Update Time", example = "")
    private Date updateTime;

    @Excel(name = "Remark")
    @Schema(description = "Remark", example = "")
    private String remark;

    @Excel(name = "Rule Custom Config, JSON Format")
    @Schema(description = "Rule Custom Config, JSON Format", example = "")
    private String rule;
    // Proportion
    private BigDecimal proportion;

    /** Data Source Name */
    private String datasourceType;
    private String datasourceName;


}
