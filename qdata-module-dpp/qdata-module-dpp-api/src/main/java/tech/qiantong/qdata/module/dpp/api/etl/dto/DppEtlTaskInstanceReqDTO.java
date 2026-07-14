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

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * Handle task-related data and operations.
 *
 * @author qdata
 * @date 2025-02-13
 */
@Data
public class DppEtlTaskInstanceReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Handle task-related data and operations. */
    private String name;

    /** Task ID */
    private Long taskId;

    /** Task code */
    private String taskCode;

    /** Task version */
    private Long taskVersion;

    /** Implementation details. */
    private String statusHistory;

    /** Implementation details. */
    private String personCharge;

    /** Implementation details. */
    private Long projectId;

    /** Implementation details. */
    private String projectCode;

    /** Implementation details. */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** Implementation details. */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** Implementation details. */
    private String commandType;

    /** Implementation details. */
    private Long maxTryTimes;

    /** Implementation details. */
    private String failureStrategy;

    /** Handle task-related data and operations. */
    private String subTaskFlag;

    /** Implementation details. */
    private String status;

    /** Handle DolphinScheduler operations. */
    private Long dsId;

    /** Scheduling engine */
    private String taskScheduler;

    /** Execution engine */
    private String taskActuator;

    /** Handle Quartz scheduling operations. */
    private Long quartzId;

    /** Whether the record is valid. */
    private Boolean validFlag;

    /** Delete the related record. */
    private Boolean delFlag;


}
