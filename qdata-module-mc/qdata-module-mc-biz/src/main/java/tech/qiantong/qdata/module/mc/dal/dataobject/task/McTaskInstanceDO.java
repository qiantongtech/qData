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
 * Business License: https://docs.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.module.mc.dal.dataobject.task;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * Collection task instance DO object MC_TASK_INSTANCE
 *
 * @author qdata
 * @date 2025-12-16
 */
@Data
@TableName(value = "MC_TASK_INSTANCE")
// Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you do not need to write it.
// @KeySequence("MC_TASK_INSTANCE_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class McTaskInstanceDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** Source system ID */
    private Long sourceSystemId;

    /** Source system name */
    private String sourceSystemName;

    /** Collection task id */
    private Long taskId;

    /** Collection mode */
    private String collectionMode;

    /** Collection range */
    private String collectionScope;

    /** Total number of collection tables */
    private Long totalCount;

    /** Number of successful collection tables */
    private Long successCount;

    /** Number of failed collection tables */
    private Long failCount;

    /** Reason for failure */
    private String failCause;

    /** New quantity */
    private Long addCount;

    /** Delete quantity */
    private Long delCount;

    /** Change quantity */
    private Long updateCount;

    /** Start time */
    private Date startTime;

    /** End time */
    private Date endTime;

    /** Time consuming */
    private Long duration;

    /** status */
    private String status;

    /** Whether the record is valid. */
    private Boolean validFlag;

    /** Delete flag */
    @TableLogic
    private Boolean delFlag;

    /** Description */
    private String description;

    /**
     * Task name
     */
    @TableField(exist = false)
    private String name;
    /**
     * Task status
     */
    @TableField(exist = false)
    private String taskStatus;

    /**
     * Creator phone number
     */
    @TableField(exist = false)
    private String createPhoneNumber;

    /**
     * Data source name
     */
    @TableField(exist = false)
    private String datasourceName;


    /**
     * Data source type
     */
    @TableField(exist = false)
    private String datasourceType;
}
