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

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.List;

@Schema(description = "Data Integration Task Response VO")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // Add this annotation, null fields will not be serialized and returned
public class DppEtlTaskTreeRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "Tree Label")
    private String label;


    @Schema(description = "Count")
    private int dppEtlTaskCount;

    @Schema(description = "Tree Children")
    private List<DppEtlTaskTreeRespVO> children;



    @Excel(name = "Task Type")
    @Schema(description = "Task Type", example = "")
    private String type;

    @Excel(name = "Task Name")
    @Schema(description = "Task Name", example = "")
    private String name;

    @Excel(name = "Task Code")
    @Schema(description = "Task Code", example = "")
    private String code;

    @Excel(name = "Extended Task Code")
    @Schema(description = "Extended Task Code", example = "")
    private String extCode;

    @Excel(name = "Task Version")
    @Schema(description = "Task Version", example = "")
    private Long version;

    @Excel(name = "Project ID")
    @Schema(description = "Project ID", example = "")
    private Long projectId;

    @Excel(name = "Project Code")
    @Schema(description = "Project Code", example = "")
    private String projectCode;

    @Excel(name = "Person in Charge")
    @Schema(description = "Person in Charge", example = "")
    private String personCharge;

    @Excel(name = "Contact Number")
    @Schema(description = "Contact Number", example = "")
    private String contactNumber;

    @Excel(name = "Node Coordinate Info")
    @Schema(description = "Node Coordinate Info", example = "")
    private String locations;

    @Excel(name = "Description")
    @Schema(description = "Description", example = "")
    private String description;

    @Schema(description = "Task Execution Strategy", example = "")
    private String executionType;

    @Excel(name = "Task Status")
    @Schema(description = "Task Status", example = "")
    private String status;

    @TableField(exist = false)
    private String releaseState = this.status;

    @Excel(name = "DolphinScheduler ID")
    @Schema(description = "DolphinScheduler ID", example = "")
    private Long dsId;

    /** Task ID */
    private Long treeId;

    @Schema(description = "Draft Task Config", example = "")
    private String draftJson;
}
