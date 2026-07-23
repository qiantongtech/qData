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

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;
import tech.qiantong.qdata.common.utils.JSONUtils;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskDO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskInstanceDO;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Data Integration Task Response VO Object DPP_ETL_TASK
 *
 * @author qdata
 * @date 2025-02-13
 */
@Schema(description = "Data Integration Task Response VO")
@Data
public class DppEtlTaskUpdateQueryRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Schema(description = "Category ID", example = "")
    private Long catId;

    @Schema(description = "Category Code", example = "")
    @Size(max = 256, message = "Category Code length cannot exceed 256 characters")
    private String catCode;


    /**
     * @see DppEtlTaskDO#type
     */
    @Excel(name = "Task Type")
    @Schema(description = "Task Type", example = "")
    private String type;

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
    private List<Map<String, Object>> locations;

    @Excel(name = "Description")
    @Schema(description = "Description", example = "")
    private String description;

    @Schema(description = "Task Execution Strategy", example = "")
    private String executionType;

    @Schema(description = "调度器", example = "")
    private String scheduler;

    @Schema(description = "执行器", example = "")
    private String actuator;

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

    // Return the Quartz job ID in edit details so the UI can inspect the schedule binding without another query.
    @Excel(name = "Quartz调度任务id")
    @Schema(description = "Quartz调度任务id", example = "")
    private Long quartzId;

    @Excel(name = "Valid")
    @Schema(description = "Valid", example = "")
    private Boolean validFlag;

    @Excel(name = "Delete Flag")
    @Schema(description = "Delete Flag", example = "")
    private Boolean delFlag;

    @Excel(name = "Created By")
    @Schema(description = "Created By", example = "")
    private String createBy;

    @Excel(name = "Creator ID")
    @Schema(description = "Creator ID", example = "")
    private Long creatorId;

    @Excel(name = "Create Time", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Create Time", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Excel(name = "Updated By")
    @Schema(description = "Updated By", example = "")
    private String updateBy;

    @Excel(name = "Updater ID")
    @Schema(description = "Updater ID", example = "")
    private Long updaterId;

    @Excel(name = "Update Time", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Update Time", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @Excel(name = "Remark")
    @Schema(description = "Remark", example = "")
    private String remark;

    /**
     * Cron Expression
     */
    @TableField(exist = false)
    private String crontab;


    @TableField(exist = false)
    private Map<String, Object> taskConfig;

    @TableField(exist = false)
    List<Map<String, Object>> taskDefinitionList;

    @TableField(exist = false)
    List<Map<String, Object>> taskRelationJson;

    @Schema(description = "Draft Task Config", example = "")
    private String draftJson;

    /** Last Execution Time */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastExecuteTime;

    /** Last Execution Status */
    private String lastExecuteStatus;

    /** Scheduling Online/Offline Status */
    private String schedulerState;

    /**
     * Task Instance
     */
    private DppEtlTaskInstanceDO taskInstance;

    public DppEtlTaskUpdateQueryRespVO(DppEtlTaskDO dppEtlTaskDO) {
        this.id = dppEtlTaskDO.getId();
        this.catId = dppEtlTaskDO.getCatId();
        this.catCode = dppEtlTaskDO.getCatCode();
        this.type = dppEtlTaskDO.getType();
        this.name = dppEtlTaskDO.getName();
        this.code = dppEtlTaskDO.getCode();
        this.version = dppEtlTaskDO.getVersion();
        this.projectId = dppEtlTaskDO.getProjectId();
        this.projectCode = dppEtlTaskDO.getProjectCode();
        this.personCharge = dppEtlTaskDO.getPersonCharge();
        this.personChargeName = dppEtlTaskDO.getPersonChargeName();
        this.contactNumber = dppEtlTaskDO.getContactNumber();
        this.locations = parseList(dppEtlTaskDO.getLocations());
        this.description = dppEtlTaskDO.getDescription();
        this.executionType = dppEtlTaskDO.getExecutionType();
        // Copy the Quartz job ID stored on the task into the edit details.
        this.quartzId = dppEtlTaskDO.getQuartzId();
        this.scheduler = dppEtlTaskDO.getScheduler();
        this.actuator = dppEtlTaskDO.getActuator();
        this.timeout = dppEtlTaskDO.getTimeout();
        this.extractionCount = dppEtlTaskDO.getExtractionCount();
        this.writeCount = dppEtlTaskDO.getWriteCount();
        this.status = dppEtlTaskDO.getStatus();
        this.dsId = dppEtlTaskDO.getDsId();
        this.validFlag = dppEtlTaskDO.getValidFlag();
        this.delFlag = dppEtlTaskDO.getDelFlag();
        this.draftJson = dppEtlTaskDO.getDraftJson();
        this.createBy = dppEtlTaskDO.getCreateBy();
        this.creatorId = dppEtlTaskDO.getCreatorId();
        this.createTime = dppEtlTaskDO.getCreateTime();
        this.updateBy = dppEtlTaskDO.getUpdateBy();
        this.updaterId = dppEtlTaskDO.getUpdatorId();
        this.updateTime = dppEtlTaskDO.getUpdateTime();
    }

    private List<Map<String, Object>> parseList(String string) {
        List<Map<String, Object>> list = JSONUtils.convertTaskDefinitionJson(string);
        return list;
    }

    private Map<String, Object> parseMap(String string) {
        Map<String, Object> stringObjectMap = JSONUtils.convertTaskDefinitionJsonMap(string);
        return stringObjectMap;
    }

    public void setTaskRelationJsonFromNodeRelList(List<DppEtlTaskNodeRelRespVO> dppEtlTaskNodeRelRespVOList) {
        // Convert List<DppEtlTaskNodeRelRespVO> to List<Map<String, Object>>
        List<Map<String, Object>> taskRelationJsonList = new ArrayList<>();

        for (DppEtlTaskNodeRelRespVO nodeRel : dppEtlTaskNodeRelRespVOList) {
            Map<String, Object> nodeRelMap = new HashMap<>();
            nodeRelMap.put("id", nodeRel.getId());
            nodeRelMap.put("projectId", nodeRel.getProjectId());
            nodeRelMap.put("projectCode", nodeRel.getProjectCode());
            nodeRelMap.put("taskId", nodeRel.getTaskId());
            nodeRelMap.put("taskCode", nodeRel.getTaskCode());
            nodeRelMap.put("taskVersion", nodeRel.getTaskVersion());
            nodeRelMap.put("preNodeId", nodeRel.getPreNodeId());
            nodeRelMap.put("preNodeCode", nodeRel.getPreNodeCode());
            nodeRelMap.put("preNodeVersion", nodeRel.getPreNodeVersion());
            nodeRelMap.put("postNodeId", nodeRel.getPostNodeId());
            nodeRelMap.put("postNodeCode", nodeRel.getPostNodeCode());
            nodeRelMap.put("postNodeVersion", nodeRel.getPostNodeVersion());
            nodeRelMap.put("validFlag", nodeRel.getValidFlag());
            nodeRelMap.put("delFlag", nodeRel.getDelFlag());
            nodeRelMap.put("createBy", nodeRel.getCreateBy());
            nodeRelMap.put("creatorId", nodeRel.getCreatorId());
            nodeRelMap.put("createTime", nodeRel.getCreateTime());
            nodeRelMap.put("updateBy", nodeRel.getUpdateBy());
            nodeRelMap.put("updaterId", nodeRel.getUpdaterId());
            nodeRelMap.put("updateTime", nodeRel.getUpdateTime());
            nodeRelMap.put("remark", nodeRel.getRemark());

            taskRelationJsonList.add(nodeRelMap);
        }

        // Store in taskRelationJson field
        this.taskRelationJson = taskRelationJsonList;
    }


    public void setTaskDefinitionList(List<DppEtlNodeRespVO> dppEtlNodeRespVOList) {
        // Convert DppEtlNodeRespVO list to List<Map<String, Object>>
        List<Map<String, Object>> taskDefinitionList = dppEtlNodeRespVOList.stream()
                .map(node -> {
                    Map<String, Object> stringObjectMap = parseMap(node.getParameters());
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", node.getId());
                    map.put("taskType", node.getType());
                    map.put("name", node.getName());
                    map.put("code", node.getCode());
                    map.put("releaseState", node.getReleaseState());
                    map.put("version", node.getVersion());
                    map.put("projectId", node.getProjectId());
                    map.put("projectCode", node.getProjectCode());
                    map.put("taskParams", stringObjectMap);
                    map.put("priority", node.getPriority());
                    map.put("failRetryTimes", node.getFailRetryTimes());
                    map.put("failRetryInterval", node.getFailRetryInterval());
                    map.put("timeout", node.getTimeout());
                    map.put("delayTime", node.getDelayTime());
                    map.put("cpuQuota", node.getCpuQuota());
                    map.put("memoryMax", node.getMemoryMax());
                    map.put("description", node.getDescription());
                    map.put("componentType", node.getComponentType());
                    map.put("dsId", node.getDsId());
                    map.put("validFlag", node.getValidFlag());
                    map.put("delFlag", node.getDelFlag());
                    map.put("createBy", node.getCreateBy());
                    map.put("creatorId", node.getCreatorId());
                    map.put("createTime", DateUtil.format(node.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
                    map.put("updateBy", node.getUpdateBy());
                    map.put("updaterId", node.getUpdaterId());
                    map.put("updateTime", DateUtil.format(node.getUpdateTime(),"yyyy-MM-dd HH:mm:ss"));
                    map.put("remark", node.getRemark());
                    return map;
                })
                .collect(Collectors.toList());

        // Assign the converted list to taskDefinitionList
        this.taskDefinitionList = taskDefinitionList;
    }


    public void createTaskConfig() {
        // Create taskConfig Map
        Map<String, Object> taskConfig = new HashMap<>();
        taskConfig.put("type", this.type);
        taskConfig.put("releaseState", this.status);
        taskConfig.put("description", this.description); // Get description from itself
        taskConfig.put("name", this.name); // Get task name from itself
        taskConfig.put("executionType", this.executionType); // Get execution type from itself
        taskConfig.put("scheduler", this.scheduler);
        taskConfig.put("actuator", this.actuator);
        taskConfig.put("crontab", this.crontab); // Fixed crontab expression
        taskConfig.put("personCharge", this.personCharge); // Person in charge
        taskConfig.put("contactNumber", this.contactNumber); // Contact number
        taskConfig.put("catCode", this.catCode); // Person in charge

        // Set taskConfig
        this.setTaskConfig(taskConfig);
    }
}
