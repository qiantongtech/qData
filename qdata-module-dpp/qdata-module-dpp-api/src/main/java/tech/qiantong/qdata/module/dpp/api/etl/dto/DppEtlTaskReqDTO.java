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

package tech.qiantong.qdata.module.dpp.api.etl.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Data Integration Task DTO - DPP_ETL_TASK
 *
 * @author qdata
 * @date 2025-02-13
 */
@Data
public class DppEtlTaskReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Task Type */
    private String type;

    /** Task Name */
    private String name;

    /** Task Code */
    private String code;

    /** Task Version */
    private Long version;

    /** Project ID */
    private Long projectId;

    /** Project Code */
    private String projectCode;

    @Schema(description = "Task Execution Strategy", example = "")
    private String executionType;

    /** Person in Charge */
    private String personCharge;

    /** Node Coordinate Information */
    private String locations;

    /** Description */
    private String description;

    /** Timeout */
    private Long timeout;

    /** Extraction Count */
    private Long extractionCount;

    /** Write Count */
    private Long writeCount;

    /** Task Status */
    private String status;

    /** DolphinScheduler ID */
    private Long dsId;

    /** Valid Flag */
    private Boolean validFlag;

    /** Delete Flag */
    private Boolean delFlag;


}
