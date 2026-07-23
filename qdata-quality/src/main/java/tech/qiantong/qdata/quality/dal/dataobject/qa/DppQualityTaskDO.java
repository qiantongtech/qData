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

package tech.qiantong.qdata.quality.dal.dataobject.qa;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * Data Quality Task DO Object DPP_QUALITY_TASK
 *
 * @author Chaos
 * @date 2025-07-21
 */
@Data
@TableName(value = "DPP_QUALITY_TASK")
// Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you do not need to write it.
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

    /** Contact person */
    private String contact;

    /** Contact ID */
    private String contactId;

    /** Contact number */
    private String contactNumber;

    /** Task status */
    private String status;

    /** Task description */
    private String description;

    /** Task priority */
    private String priority;

    /** Worker grouping */
    private String workerGroup;

    /** Number of failed retries */
    private Long retryTimes;

    /** Failure retry interval (seconds) */
    private Long retryInterval;

    /** Delayed execution time (seconds) */
    private Long delayTime;

    /** Execution strategy */
    private String strategy;

    /** Scheduling cycle */
    private String cycle;

    /** Is it valid */
    private Boolean validFlag;

    /** Delete flag */
    @TableLogic
    private Boolean delFlag;

    @TableField(exist = false)
    private Integer taskObjNum;

    @TableField(exist = false)
    private Integer taskEvaluateNum;



    @Schema(description = "是否是资产质量任务;0：否。1是")
    private String assetFlag;

    @Schema(description = "资产id")
    private Long assetId;
}
