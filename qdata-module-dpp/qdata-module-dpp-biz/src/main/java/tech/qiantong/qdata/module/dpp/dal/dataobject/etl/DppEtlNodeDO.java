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
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * Data Integration Node DO - DPP_ETL_NODE
 *
 * @author qdata
 * @date 2025-02-13
 */
@Data
@TableName(value = "DPP_ETL_NODE")
// Used for auto-increment primary keys in Oracle, PostgreSQL, Kingbase, DB2, H2 databases. Can be omitted for MySQL and similar databases.
// @KeySequence("DPP_ETL_NODE_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DppEtlNodeDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** Task type; 1: Offline task 2: Real-time task 3: Data development task 4: Job task */
    private String taskType;

    /** Node type */
    private String type;

    /** Node name */
    private String name;

    /** Node code */
    private String code;

    /** Node version */
    private Long version;

    /** Project ID */
    private Long projectId;

    /** Project code */
    private String projectCode;

    /** Node parameters */
    private String parameters;

    /** Task priority */
    private String priority;

    /** Fail retry times */
    private Long failRetryTimes;

    /** Fail retry interval (minutes) */
    private Long failRetryInterval;

    /** Timeout */
    private Long timeout;

    /** Delay execution time (minutes) */
    private Long delayTime;

    /** CPU quota */
    private Long cpuQuota;

    /** Max memory */
    private Long memoryMax;

    /** Description */
    private String description;

    /** Component type */
    private String componentType;

    /** DolphinScheduler ID */
    private Long dsId;

    /** Valid flag */
    private Boolean validFlag;

    /** Delete flag */
    @TableLogic
    private Boolean delFlag;


}
