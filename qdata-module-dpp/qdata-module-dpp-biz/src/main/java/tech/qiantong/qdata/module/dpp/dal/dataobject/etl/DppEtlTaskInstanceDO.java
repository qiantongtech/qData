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
 * Data Integration Task Instance DO - DPP_ETL_TASK_INSTANCE
 *
 * @author qdata
 * @date 2025-02-13
 */
@Data
@TableName(value = "DPP_ETL_TASK_INSTANCE")
// Used for auto-increment primary keys in Oracle, PostgreSQL, Kingbase, DB2, H2 databases. Can be omitted for MySQL and similar databases.
// @KeySequence("DPP_ETL_TASK_INSTANCE_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DppEtlTaskInstanceDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * Category ID
     */
    private Long catId;

    /**
     * Category code
     */
    private String catCode;

    /**
     * Task type; 1: Offline task 2: Real-time task 3: Data development task 4: Job task
     */
    private String taskType;

    /**
     * Task instance name
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
     * Status history (json list)
     */
    private String statusHistory;

    /**
     * Person in charge
     */
    private String personCharge;

    /**
     * Person in charge name
     */
    @TableField(exist = false)
    private String personChargeName;


    /**
     * Contact number
     */
    private String contactNumber;
    /**
     * Project ID
     */
    private Long projectId;

    /**
     * Project code
     */
    private String projectCode;

    /**
     * Schedule time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date scheduleTime;

    /**
     * Start time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * End time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * Run times
     */
    private Integer runTimes;

    /**
     * Run type
     */
    private String commandType;

    /**
     * Max retry times
     */
    private Integer maxTryTimes;

    /**
     * Failure strategy
     */
    private String failureStrategy;

    /**
     * Sub task flag
     */
    private String subTaskFlag;

    /**
     * Status
     */
    private String status;

    /**
     * Parent task instance ID; only has value when it's a sub-task
     */
    private Long parentTaskInstanceId;

    /**
     * Parent node instance ID; only has value when it's a sub-task
     */
    private Long parentNodeInstanceId;

    /**
     * DolphinScheduler ID
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
     * Quartz scheduled task ID
     */
    private Long quartzId;

    /**
     * Executor task ID
     */
    private String executorJobId;

    /**
     * Executor configuration file path
     */
    private String executorConfigPath;

    /**
     * Execution process ID
     */
    private Long pid;

    /**
     * Execution log file path
     */
    private String logPath;

    /**
     * Valid flag
     */
    private Boolean validFlag;

    /**
     * Delete flag
     */
    @TableLogic
    private Boolean delFlag;
}
