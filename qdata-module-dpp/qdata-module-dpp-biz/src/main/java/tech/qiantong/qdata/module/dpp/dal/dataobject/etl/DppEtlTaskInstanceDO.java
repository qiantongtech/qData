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
 * Handle task-related data and operations.
 *
 * @author qdata
 * @date 2025-02-13
 */
@Data
@TableName(value = "DPP_ETL_TASK_INSTANCE")
// Handle JDBC SQL execution.
// @KeySequence("DPP_ETL_TASK_INSTANCE_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DppEtlTaskInstanceDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** Implementation details. */
    private Long catId;

    /** Implementation details. */
    private String catCode;

    /**
     * Handle task-related data and operations.
     */
    private String taskType;

    /**
     * Handle task-related data and operations.
     */
    private String name;

    /**
     * Task ID
     */
    private Long taskId;

    /**
     * Task code
     */
    private String taskCode;

    /**
     * Task version
     */
    private Integer taskVersion;

    /**
     * Implementation details.
     */
    private String statusHistory;

    /**
     * Implementation details.
     */
    private String personCharge;

    /**
     * Implementation details.
     */
    @TableField(exist = false)
    private String personChargeName;


    /**
     * Implementation details.
     */
    private String contactNumber;
    /**
     * Implementation details.
     */
    private Long projectId;

    /**
     * Implementation details.
     */
    private String projectCode;

    /**
     * Handle scheduling configuration and operations.
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date scheduleTime;

    /**
     * Implementation details.
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * Implementation details.
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * Implementation details.
     */
    private Integer runTimes;

    /**
     * Implementation details.
     */
    private String commandType;

    /**
     * Implementation details.
     */
    private Integer maxTryTimes;

    /**
     * Implementation details.
     */
    private String failureStrategy;

    /**
     * Handle task-related data and operations.
     */
    private String subTaskFlag;

    /**
     * Implementation details.
     */
    private String status;

    /**
     * Handle task-related data and operations.
     */
    private Long parentTaskInstanceId;

    /**
     * Handle task-related data and operations.
     */
    private Long parentNodeInstanceId;

    /**
     * Handle DolphinScheduler operations.
     */
    private Long dsId;

    /**
     * Scheduling engine
     */
    private String taskScheduler;

    /**
     * Execution engine
     */
    private String taskActuator;

    /**
     * Handle Quartz scheduling operations.
     */
    private Long quartzId;

    /**
     * Executor job ID.
     */
    private String executorJobId;

    /**
     * Executor configuration file path.
     */
    private String executorConfigPath;

    /**
     * Execution process ID.
     */
    private Long pid;

    /**
     * Execution log file path.
     */
    private String logPath;

    /**
     * Whether the record is valid.
     */
    private Boolean validFlag;

    /**
     * Delete the related record.
     */
    @TableLogic
    private Boolean delFlag;
}
