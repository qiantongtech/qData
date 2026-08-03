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
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import tech.qiantong.qdata.common.core.page.PageParam;

import java.util.Date;

/**
 * Data Integration Task Request VO Object DPP_ETL_TASK
 *
 * @author qdata
 * @date 2025-02-13
 */
@Schema(description = "Data Integration Task Request VO")
@Data
public class DppEtlTaskPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;

    @Schema(description = "Task Type", example = "1: Offline Task 2: Real-time Task 3: Data Development Task 4: Job Task")
    private String type;

    @Schema(description = "Process Type", example = "1: Stream Processing  2: Batch Processing")
    private String processType;

    @Schema(description = "Datasource Type", example = "")
    private String datasourceType;

    @Schema(description = "Task Name", example = "")
    private String name;

    @Schema(description = "Task Execution Strategy", example = "")
    private String executionType;

    @Schema(description = "Task Code", example = "")
    private String code;

    @Schema(description = "Task Version", example = "")
    private Long version;

    @Schema(description = "Project ID", example = "")
    private Long projectId;

    @Schema(description = "Project Code", example = "")
    private String projectCode;

    @Schema(description = "Person in Charge", example = "")
    private String personCharge;

    @Schema(description = "Node Coordinate Info", example = "")
    private String locations;

    @Schema(description = "Description", example = "")
    private String description;

    @Schema(description = "Timeout", example = "")
    private Long timeout;

    @Schema(description = "Extraction Count", example = "")
    private Long extractionCount;

    @Schema(description = "Write Count", example = "")
    private Long writeCount;

    @Schema(description = "Task Status", example = "")
    private String status;

    @Schema(description = "Current execution status: running, success, failed or idle", example = "running")
    private String currentStatus;

    @Schema(description = "Last execution start time", example = "2026-07-04 00:00:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @Schema(description = "Last execution end time", example = "2026-07-10 23:59:59")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @Schema(description = "DolphinScheduler ID", example = "")
    private Long dsId;

    /** Category Code */
    private String catCode;

    @Schema(description = "Draft Task Config", example = "")
    private String draftJson;
}
