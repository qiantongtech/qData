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

package tech.qiantong.qdata.module.dpp.dal.dataobject.etl;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * Data Integration Node Instance DO - DPP_ETL_NODE_INSTANCE
 *
 * @author qdata
 * @date 2025-02-13
 */
@Data
@TableName(value = "DPP_ETL_NODE_INSTANCE")
// Used for auto-increment primary keys in Oracle, PostgreSQL, Kingbase, DB2, H2 databases. Can be omitted for MySQL and similar databases.
// @KeySequence("DPP_ETL_NODE_INSTANCE_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DppEtlNodeInstanceDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** Task type; 1: Offline task 2: Real-time task 3: Data development task 4: Job task */
    private String taskType;

    /** Node instance name */
    private String name;

    /** Node type */
    private String nodeType;

    /** Node ID */
    private Long nodeId;

    /** Node code */
    private String nodeCode;

    /** Node version */
    private Integer nodeVersion;

    /** Task instance ID */
    private Long taskInstanceId;

    /** Task instance name */
    private String taskInstanceName;

    /** Project ID */
    private Long projectId;

    /** Project code */
    private String projectCode;

    /** Submit time */
    private Date submitTime;

    /** Start time */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** End time */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** Execute path */
    private String executePath;

    /** Log path */
    private String logPath;

    /** Node parameters */
    private String parameters;

    /** Node priority */
    private String priority;

    /** Retry times */
    private Integer retryTimes;

    /** Delay execution time (minutes) */
    private Integer delayTime;

    /** CPU quota */
    private Integer cpuQuota;

    /** Max memory */
    private Integer memoryMax;

    /** Status */
    private String status;

    /** Component type */
    private String componentType;

    /** DolphinScheduler ID */
    private Long dsId;

    /** DolphinScheduler task instance ID */
    private Long dsTaskInstanceId;

    /** Valid flag */
    private Boolean validFlag;

    /** Delete flag */
    @TableLogic
    private Boolean delFlag;

    /**
     * Person in charge name
     */
    @TableField(exist = false)
    private String personChargeName;

    /**
     * Run type
     */
    @TableField(exist = false)
    private String commandType;
}
