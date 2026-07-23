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
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.util.Date;
import java.util.List;

/**
 * <P>
 * Purpose:
 * </p>
 *
 * @author: FXB
 * @create: 2025-03-26 15:30
 **/
@Data
public class DppEtlTaskInstanceTreeListRespVO {

    private String dataId;

    private Long id;

    @Excel(name = "Instance Name")
    @Schema(description = "Instance Name", example = "")
    private String name;

    @Excel(name = "Task Name")
    @Schema(description = "Task Name, only for instance nodes", example = "")
    private String taskName;

    @Excel(name = "Code")
    @Schema(description = "Task Code", example = "")
    private String code;

    @Excel(name = "Schedule Time", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Schedule Time", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date scheduleTime;

    @Excel(name = "Start Time", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Start Time", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @Excel(name = "End Time", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "End Time", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @Excel(name = "Duration", width = 30)
    @Schema(description = "Duration", example = "")
    private String duration;

    @Schema(description = "Run Times", example = "")
    private Integer runTimes;

    @Excel(name = "Command Type")
    @Schema(description = "Command Type", example = "")
    private String commandType;

    @Excel(name = "Sub-task Flag")
    @Schema(description = "Sub-task Flag", example = "")
    private String subTaskFlag;

    @Excel(name = "Status")
    @Schema(description = "Status", example = "")
    private String status;

    @Schema(description = "数据类型 1:Job Task Instance 2:Job Task Node Instance 3:Sub-task Node Instance", example = "")
    private String dataType;

    @Schema(description = "Node Type", example = "")
    private String nodeType;

    @Schema(description = "Log", example = "")
    private String logPath;

    private boolean hasChildren;

    private Long taskInstanceId;

    private Long nodeInstanceId;

    private List<DppEtlTaskInstanceTreeListRespVO> children;
}
