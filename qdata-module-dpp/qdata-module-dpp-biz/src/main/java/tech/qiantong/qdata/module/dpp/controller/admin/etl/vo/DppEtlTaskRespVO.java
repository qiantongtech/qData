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

package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import tech.qiantong.qdata.common.annotation.Excel;

import java.util.Date;
import java.io.Serializable;
import java.util.List;

/**
 * Data Integration Task Response VO Object DPP_ETL_TASK
 *
 * @author qdata
 * @date 2025-02-13
 */
@Schema(description = "Data Integration Task Response VO")
@Data
public class DppEtlTaskRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Schema(description = "Category Code", example = "")
    private String catCode;

    @Schema(description = "Category Name", example = "")
    private String catName;

    @Excel(name = "Task Type")
    @Schema(description = "Task Type", example = "")
    private String type;

    @Excel(name = "Datasource Type")
    @Schema(description = "Datasource Type", example = "")
    private String datasourceType;

    @Excel(name = "Task Name")
    @Schema(description = "Task Name", example = "")
    private String name;

    @Excel(name = "Task Code")
    @Schema(description = "Task Code", example = "")
    private String code;

    @Excel(name = "Task Version")
    @Schema(description = "Task Version", example = "")
    private Long version;

    @Excel(name = "Project ID")
    @Schema(description = "Project ID", example = "")
    private Long projectId;

    @Excel(name = "Project Code")
    @Schema(description = "Project Code", example = "")
    private String projectCode;

    @Excel(name = "Person in Charge")
    @Schema(description = "Person in Charge", example = "")
    private String personCharge;

    @Excel(name = "Person in Charge Name")
    @Schema(description = "Person in Charge Name", example = "")
    private String personChargeName;

    @Excel(name = "Contact Number")
    @Schema(description = "Contact Number", example = "")
    private String contactNumber;

    @Excel(name = "Node Coordinate Info")
    @Schema(description = "Node Coordinate Info", example = "")
    private String locations;

    @Excel(name = "Description")
    @Schema(description = "Description", example = "")
    private String description;

    @Schema(description = "Task Execution Strategy", example = "")
    private String executionType;

    @Excel(name = "Timeout")
    @Schema(description = "Timeout", example = "")
    private Long timeout;

    @Excel(name = "Extraction Count")
    @Schema(description = "Extraction Count", example = "")
    private Long extractionCount;

    @Excel(name = "Write Count")
    @Schema(description = "Write Count", example = "")
    private Long writeCount;

    @Excel(name = "Task Status")
    @Schema(description = "Task Status", example = "")
    private String status;

    @Excel(name = "DolphinScheduler ID")
    @Schema(description = "DolphinScheduler ID", example = "")
    private Long dsId;

    // Return the Quartz job ID so the UI and API can identify the schedule bound to the task during troubleshooting.
    @Excel(name = "Quartz调度任务id")
    @Schema(description = "Quartz调度任务id", example = "")
    private Long quartzId;

    @Excel(name = "调度器")
    @Schema(description = "调度器", example = "DOLPHINSCHEDULER")
    private String scheduler;

    @Excel(name = "执行器")
    @Schema(description = "执行器", example = "SPARK")
    private String actuator;

    @Excel(name = "Valid")
    @Schema(description = "Valid", example = "")
    private Boolean validFlag;

    @Excel(name = "Delete Flag")
    @Schema(description = "Delete Flag", example = "")
    private Boolean delFlag;

    @Excel(name = "Created By")
    @Schema(description = "Created By", example = "")
    private String createBy;

    @Schema(description = "Creator Contact Number", example = "")
    private String createUserContactNumber;

    @Excel(name = "Creator ID")
    @Schema(description = "Creator ID", example = "")
    private Long creatorId;

    @Excel(name = "Create Time", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Create Time", example = "")
    private Date createTime;

    @Excel(name = "Updated By")
    @Schema(description = "Updated By", example = "")
    private String updateBy;

    @Excel(name = "Updater ID")
    @Schema(description = "Updater ID", example = "")
    private Long updaterId;

    @Excel(name = "Update Time", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Update Time", example = "")
    private Date updateTime;

    @Excel(name = "Remark")
    @Schema(description = "Remark", example = "")
    private String remark;


    @Excel(name = "Last Execution Time", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Last Execution Time", example = "")
    @TableField(exist = false)
    private Date lastExecuteTime;


    @TableField(exist = false)
    private String lastExecuteStatus;

    @Excel(name = "Last Execution End Time", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Last Execution End Time", example = "")
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastExecuteEndTime;

    @Schema(description = "Current status: running, idle, success or failed")
    @TableField(exist = false)
    private String currentStatus;

    @Schema(description = "Current status name")
    @TableField(exist = false)
    private String currentStatusName;

    @Excel(name = "Duration")
    @Schema(description = "Execution duration", example = "")
    @TableField(exist = false)
    private String duration;


    @Schema(description = "Draft Task Config", example = "")
    private String draftJson;

    /** Cron Expression */
    @TableField(exist = false)
    private String cronExpression;

    /** Scheduling Upper/Lower Limit */
    @TableField(exist = false)
    private String schedulerState;


    @TableField(exist = false)
    List<DppEtlNodeRespVO> taskDefinitionList;

    @TableField(exist = false)
    List<DppEtlTaskNodeRelRespVO> taskRelationJson;

    @Schema(description = "Latest task instance ID used to view execution logs")
    @TableField(exist = false)
    private Long taskInstanceId;

    @JsonProperty("label")
    public String getLabel() {
        return name; // label field dynamically takes value
    }
}
