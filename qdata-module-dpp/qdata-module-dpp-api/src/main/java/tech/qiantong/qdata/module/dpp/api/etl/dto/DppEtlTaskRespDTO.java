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
 * Handle task-related data and operations.
 *
 * @author qdata
 * @date 2025-02-13
 */
@Data
public class DppEtlTaskRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Implementation details. */
    private Long catId;

    /** Implementation details. */
    private String catCode;

    /** Handle task-related data and operations. */
    private String type;

    /** Handle task-related data and operations. */
    private String name;

    /** Task code */
    private String code;

    /** Task version */
    private Long version;

    /** Implementation details. */
    private Long projectId;

    /** Implementation details. */
    private String projectCode;

    /** Implementation details. */
    private String personCharge;

    /**
     * Implementation details.
     */
    private String contactNumber;

    /** Handle node-related data and operations. */
    private String locations;

    /** Implementation details. */
    private String description;

    /** Implementation details. */
    private Long timeout;

    /** Implementation details. */
    private Long extractionCount;

    @Schema(description = "任务的执行策略", example = "")
    private String executionType;

    /** Implementation details. */
    private Long writeCount;

    /** Handle task-related data and operations. */
    private String status;

    /** Handle DolphinScheduler operations. */
    private Long dsId;

    /** Handle Quartz scheduling operations. */
    private Long quartzId;

    /** Scheduler */
    private String scheduler;

    /** Executor */
    private String actuator;

    /** Whether the record is valid. */
    private Boolean validFlag;

    /** Delete the related record. */
    private Boolean delFlag;


}
