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

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.qiantong.qdata.common.core.domain.BaseEntity;
import tech.qiantong.qdata.common.utils.JSONUtils;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

/**
 * Implementation details.
 *
 * @author qdata
 * @date 2025-02-19
 */
@Schema(description = "新数据集成请求 VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DppEtlNewNodeSaveReqVO extends BaseEntity {

    /** Implementation details. */
    @Parameter(name = "catCode", description = "类目编码")
    private String catCode;
    private Long catId;

    /** Implementation details. */
    @Parameter(name = "catCode", description = "责任人")
    private String personCharge;

    /** Implementation details. */
    @Parameter(name = "catCode", description = "联系电话")
    private String contactNumber;

    @Parameter(name = "projectCode", description = "项目编码", required = true)
    @NotNull(message = "项目编码不能为空")
    private Long projectCode;

    @Schema(description = "任务类型", example = "")
    @Size(max = 256, message = "任务类型长度不能超过256个字符")
    private String type;

    @Schema(description = "项目id", example = "")
    private Long projectId;

    @Parameter(name = "name", description = "名称", required = true)
    private String name;

    @Parameter(name = "description", description = "描述", required = true)
    @Size(max = 255, message = "描述长度不能超过256个字符")
    private String description;

    @Parameter(name = "globalParams", description = "全局参数", required = false)
    private String globalParams = "[]";  // Implementation details.

    @Parameter(name = "locations", description = "位置参数", required = false)
    private List<Map<String,Object>> locations;

    @Parameter(name = "timeout", description = "超时时间", required = false)
    private Long timeout = 0L;  // Implementation details.

    @Parameter(name = "taskRelationJson", description = "任务关系的 JSON", required = true)
    private String taskRelationJson;

    @Parameter(name = "taskDefinitionList", description = "任务定义的 JSON", required = true)
    private String taskDefinitionList;

    @Parameter(name = "otherParamsJson", description = "其他参数的 JSON", required = false)
    private String otherParamsJson;

    @Parameter(name = "executionType", description = "执行类型", required = false)
    private String executionType;

    // Implementation details.
    private String releaseState;
    // Implementation details.
    private String schedulerState;
    private String status;
    private String code;
    private String crontab;
    private String id;
    private Integer version;

    @Schema(description = "调度器", example = "DOLPHINSCHEDULER")
    private String scheduler;

    @Schema(description = "执行器", example = "SPARK")
    private String actuator;

    /**
     * taskType（SPARK、FINK）
     * Handle task-related data and operations.
     * Implementation details.
     * Implementation details.
     * Implementation details.
     * Implementation details.
     *
     *
     * Implementation details.
     * Implementation details.
     * Implementation details.
     * Implementation details.
     * Implementation details.
     * Implementation details.
     * Implementation details.
     *
     *
     * Implementation details.
     * Implementation details.
     * Implementation details.
     * Implementation details.
     * Implementation details.
     * Implementation details.
     * Implementation details.
     */
    @Schema(description = "草稿任务配置信息", example = "")
    private String draftJson;

    public DppEtlNewNodeSaveReqVO(DppEtlTaskUpdateQueryRespVO src) {
        if (src == null) {
            return;
        }

        this.catCode = src.getCatCode();
        this.catId = src.getCatId();
        this.personCharge = src.getPersonCharge();
        this.contactNumber = src.getContactNumber();
        this.projectCode = JSONUtils.convertToLong(src.getProjectCode());
        this.projectId = src.getProjectId();
        this.type = src.getType();
        this.name = src.getName();
        this.description = src.getDescription();
        this.locations = src.getLocations();
        this.timeout = src.getTimeout();
        this.executionType = src.getExecutionType();
        this.crontab = src.getCrontab();
        this.draftJson = src.getDraftJson();

        // Handle JSON data for this operation.
        if (src.getTaskRelationJson() != null) {
            this.taskRelationJson = JSONUtils.toJson(src.getTaskRelationJson());
        }
        if (src.getTaskDefinitionList() != null) {
            this.taskDefinitionList = JSONUtils.toJson(src.getTaskDefinitionList());
        }

        // Implementation details.
        this.releaseState = "0";
        this.schedulerState = "0";
        this.status = "0";
    }
}
