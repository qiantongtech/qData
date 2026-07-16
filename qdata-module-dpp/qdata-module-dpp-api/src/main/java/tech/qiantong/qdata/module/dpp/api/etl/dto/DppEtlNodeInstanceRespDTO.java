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
 * Data Integration Node Instance DTO - DPP_ETL_NODE_INSTANCE
 *
 * @author qdata
 * @date 2025-02-13
 */
@Data
public class DppEtlNodeInstanceRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Node Instance Name */
    private String name;

    /** Node Type */
    private String nodeType;

    /** Node ID */
    private Long nodeId;

    /** Node Code */
    private String nodeCode;

    /** Node Version */
    private Long nodeVersion;

    /** Task Instance ID */
    private Long taskInstanceId;

    /** Task Instance Name */
    private String taskInstanceName;

    /** Project ID */
    private Long projectId;

    /** Project Code */
    private String projectCode;

    /** Submit Time */
    private Date submitTime;

    /** Start Time */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** End Time */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** Execute Path */
    private String executePath;

    /** Log Path */
    private String logPath;

    /** Node Parameters */
    private String parameters;

    /** Node Priority */
    private String priority;

    /** Retry Times */
    private Long retryTimes;

    /** Retry Interval (Minutes) */
    private Long fretryInterval;

    /** Delay Execution Time (Minutes) */
    private Long delayTime;

    /** CPU Quota */
    private Long cpuQuota;

    /** Max Memory */
    private Long memoryMax;

    /** Status */
    private String status;

    /** DolphinScheduler ID */
    private Long dsId;

    /** DolphinScheduler Task Instance ID */
    private Long dsTaskInstanceId;

    /** Valid Flag */
    private Boolean validFlag;

    /** Delete Flag */
    private Boolean delFlag;


}
