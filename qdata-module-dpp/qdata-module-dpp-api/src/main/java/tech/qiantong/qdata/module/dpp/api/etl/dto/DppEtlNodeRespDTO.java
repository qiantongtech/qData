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

import lombok.Data;

/**
 * Data Integration Node DTO - DPP_ETL_NODE
 *
 * @author qdata
 * @date 2025-02-13
 */
@Data
public class DppEtlNodeRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Task Type; 1: Offline Task 2: Real-time Task 3: Data Development Task 4: Job Task */
    private String taskType;

    /** Node Type */
    private String type;

    /** Node Name */
    private String name;

    /** Node Code */
    private String code;

    /** Node Version */
    private Long version;

    /** Project ID */
    private Long projectId;

    /** Project Code */
    private String projectCode;

    /** Node Parameters */
    private String parameters;

    /** Task Priority */
    private String priority;

    /** Fail Retry Times */
    private Long failRetryTimes;

    /** Fail Retry Interval (Minutes) */
    private Long failRetryInterval;

    /** Timeout */
    private Long timeout;

    /** Delay Execution Time (Minutes) */
    private Long delayTime;

    /** CPU Quota */
    private Long cpuQuota;

    /** Max Memory */
    private Long memoryMax;

    /** Description */
    private String description;

    /** Component Type */
    private String componentType;

    /** DolphinScheduler ID */
    private Long dsId;

    /** Valid Flag */
    private Boolean validFlag;

    /** Delete Flag */
    private Boolean delFlag;


}
