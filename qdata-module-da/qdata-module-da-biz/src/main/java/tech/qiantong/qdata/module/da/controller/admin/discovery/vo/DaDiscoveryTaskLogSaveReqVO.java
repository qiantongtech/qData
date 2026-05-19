/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.module.da.controller.admin.discovery.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 数据发现任务日志 创建/修改 Request VO DA_DISCOVERY_TASK_LOG
 *
 * @author qdata
 * @date 2025-02-17
 */
@Schema(description = "数据发现任务日志 Response VO")
@Data
public class DaDiscoveryTaskLogSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "实例名称", example = "")
    @Size(max = 256, message = "实例名称长度不能超过256个字符")
    private String name;

    @Schema(description = "节点id", example = "")
    private Long nodeId;

    @Schema(description = "节点编码", example = "")
    @Size(max = 256, message = "节点编码长度不能超过256个字符")
    private String nodeCode;

    @Schema(description = "任务名称", example = "")
    @Size(max = 256, message = "任务名称长度不能超过256个字符")
    private String taskName;

    @Schema(description = "任务id", example = "")
    private Long taskId;

    @Schema(description = "任务编码", example = "")
    @Size(max = 256, message = "任务编码长度不能超过256个字符")
    private String taskCode;

    @Schema(description = "开始时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @Schema(description = "结束时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @Schema(description = "状态", example = "")
    @Size(max = 256, message = "状态长度不能超过256个字符")
    private String status;

    @Schema(description = "新增表数", example = "")
    private Long newTableCount;

    @Schema(description = "修改表数", example = "")
    private Long modifiedTableCount;

    @Schema(description = "删除表数", example = "")
    private Long deletedTableCount;

    @Schema(description = "联系人", example = "")
    @Size(max = 256, message = "联系人长度不能超过256个字符")
    private String contact;

    @Schema(description = "联系人ID", example = "")
    private Long contactId;

    @Schema(description = "联系电话", example = "")
    @Size(max = 256, message = "联系电话长度不能超过256个字符")
    private String contactNumber;

    @Schema(description = "邮箱", example = "")
    @Size(max = 256, message = "邮箱长度不能超过256个字符")
    private String email;

    @Schema(description = "DolphinScheduler的id", example = "")
    private Long dsId;

    @Schema(description = "DolphinScheduler的任务实例id", example = "")
    private Long dsTaskInstanceId;

    @Schema(description = "日志路径", example = "")
    @Size(max = 256, message = "日志路径长度不能超过256个字符")
    private String path;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;

    public void populateFromTask(DaDiscoveryTaskRespVO daDiscoveryTask) {
        if (daDiscoveryTask == null) {
            return;
        }
        //TODO 对接海豚调度器会改
        this.name = daDiscoveryTask.getName();
        this.taskName = daDiscoveryTask.getName();
        this.remark = daDiscoveryTask.getRemark();
        this.contact = daDiscoveryTask.getContact();
        this.contactId = daDiscoveryTask.getContactId();
        this.contactNumber = daDiscoveryTask.getContactNumber();
        this.email = daDiscoveryTask.getEmail();
        this.nodeId = daDiscoveryTask.getNodeId();
        this.nodeCode = daDiscoveryTask.getNodeCode();
        this.taskId = daDiscoveryTask.getTaskId();
        this.taskCode = daDiscoveryTask.getTaskCode();
        this.dsId = 0L;
        this.dsTaskInstanceId = 0L;
        this.status = "1";
    }

}
