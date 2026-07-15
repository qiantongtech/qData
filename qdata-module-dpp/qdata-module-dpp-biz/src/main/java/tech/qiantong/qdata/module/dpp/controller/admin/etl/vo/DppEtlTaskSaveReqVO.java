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

import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * Data Integration Task Create/Update Request VO DPP_ETL_TASK
 *
 * @author qdata
 * @date 2025-02-13
 */
@Schema(description = "Data Integration Task Response VO")
@Data
public class DppEtlTaskSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    /** Category ID */
    @Schema(description = "Category ID", example = "")
    private Long catId;

    /** Category Code */
    @Schema(description = "Category Code", example = "")
    private String catCode;

    @Schema(description = "Task Type", example = "")
    @Size(max = 256, message = "Task Type length cannot exceed 256 characters")
    private String type;

    @Schema(description = "Task Name", example = "")
    @Size(max = 256, message = "Task Name length cannot exceed 256 characters")
    private String name;

    @Schema(description = "Task Code", example = "")
    @Size(max = 256, message = "Task Code length cannot exceed 256 characters")
    private String code;

    @Schema(description = "Task Version", example = "")
    private Integer version;

    @Schema(description = "Project ID", example = "")
    private Long projectId;

    @Schema(description = "Project Code", example = "")
    @Size(max = 256, message = "Project Code length cannot exceed 256 characters")
    private String projectCode;

    @Schema(description = "Person in Charge", example = "")
    @Size(max = 256, message = "Person in Charge length cannot exceed 256 characters")
    private String personCharge;

    /** Contact Number */
    @Schema(description = "Contact Number", example = "")
    @Size(max = 256, message = "Contact Number length cannot exceed 256 characters")
    private String contactNumber;

    @Schema(description = "Node Coordinate Info", example = "")
    private String locations;

    @Schema(description = "Description", example = "")
    @Size(max = 256, message = "Description length cannot exceed 256 characters")
    private String description;

    @Schema(description = "Timeout", example = "")
    private Long timeout;

    @Schema(description = "Extraction Count", example = "")
    private Long extractionCount;

    @Schema(description = "Write Count", example = "")
    private Long writeCount;

    @Schema(description = "Task Status", example = "")
    @Size(max = 256, message = "Task Status length cannot exceed 256 characters")
    private String status;

    @Schema(description = "Execution Strategy", example = "")
    private String executionType;

    @Schema(description = "DolphinScheduler ID", example = "")
    private Long dsId;

    // 任务表里保存 Quartz Job id，后端后续可以按任务直接操作 Quartz 调度。
    @Schema(description = "Quartz调度任务id", example = "")
    private Long quartzId;

    @Schema(description = "调度器", example = "DOLPHINSCHEDULER")
    private String scheduler;

    @Schema(description = "执行器", example = "SPARK")
    private String actuator;

    @Schema(description = "Remark", example = "")
    @Size(max = 256, message = "Remark length cannot exceed 256 characters")
    private String remark;

    @Schema(description = "Draft Task Config", example = "")
    private String draftJson;
}
