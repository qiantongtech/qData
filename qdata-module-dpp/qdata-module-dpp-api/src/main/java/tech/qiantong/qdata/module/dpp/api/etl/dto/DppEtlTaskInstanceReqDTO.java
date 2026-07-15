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
 * Data Integration Task Instance DTO - DPP_ETL_TASK_INSTANCE
 *
 * @author qdata
 * @date 2025-02-13
 */
@Data
public class DppEtlTaskInstanceReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Task Instance Name */
    private String name;

    /** Task ID */
    private Long taskId;

    /** Task Code */
    private String taskCode;

    /** Task Version */
    private Long taskVersion;

    /** Status History (JSON List) */
    private String statusHistory;

    /** Person in Charge */
    private String personCharge;

    /** Project ID */
    private Long projectId;

    /** Project Code */
    private String projectCode;

    /** Start Time */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** End Time */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** Command Type */
    private String commandType;

    /** Max Retry Times */
    private Long maxTryTimes;

    /** Failure Strategy */
    private String failureStrategy;

    /** Sub Task Flag */
    private String subTaskFlag;

    /** Status */
    private String status;

    /** DolphinScheduler ID */
    private Long dsId;

    /** 调度引擎 */
    private String taskScheduler;

    /** 执行引擎 */
    private String taskActuator;

    /** Quartz调度任务id */
    private Long quartzId;

    /** 是否有效 */
    private Boolean validFlag;

    /** Delete Flag */
    private Boolean delFlag;


}
