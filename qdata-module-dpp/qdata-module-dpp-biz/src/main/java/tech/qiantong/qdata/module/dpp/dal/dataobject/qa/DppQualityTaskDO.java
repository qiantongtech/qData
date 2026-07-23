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

package tech.qiantong.qdata.module.dpp.dal.dataobject.qa;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * Data Quality Task DO - DPP_QUALITY_TASK
 *
 * @author Chaos
 * @date 2025-07-21
 */
@Data
@TableName(value = "DPP_QUALITY_TASK")
// Used for auto-increment primary keys in Oracle, PostgreSQL, Kingbase, DB2, H2 databases. Can be omitted for MySQL and similar databases.
// @KeySequence("DPP_QUALITY_TASK_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DppQualityTaskDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** Task name */
    private String taskName;

    /** Category code */
    private String catCode;

    /** Contact */
    private String contact;

    /** Contact ID */
    private String contactId;

    /** Contact number */
    private String contactNumber;

    /** Task status 0: offline, 1: online */
    private String status;

    /** Task description */
    private String description;

    /** Priority */
    private String priority;

    /** Worker group */
    private String workerGroup;

    /** Fail retry times */
    private Long retryTimes;

    /** Retry interval (seconds) */
    private Long retryInterval;

    /** Delay time (seconds) */
    private Long delayTime;

    /** Strategy */
    private String strategy;

    /** Cycle */
    private String cycle;

    /** Valid flag */
    private Boolean validFlag;

    /** Delete flag */
    @TableLogic
    private Boolean delFlag;

    @TableField(exist = false)
    private Integer taskObjNum;

    @TableField(exist = false)
    private Integer taskEvaluateNum;

    @TableField(exist = false)
    private String catName;

   /** System job ID */
    private Long systemJobId;

    /** Node ID */
    private Long nodeId;

    /** Node code */
    private String nodeCode;

    /** Task ID */
    private Long taskId;

    /** Task code */
    private String taskCode;


    @Schema(description = "Whether it is an asset quality task; 0: No, 1: Yes")
    private String assetFlag;

    @Schema(description = "Asset ID")
    private Long assetId;
}
