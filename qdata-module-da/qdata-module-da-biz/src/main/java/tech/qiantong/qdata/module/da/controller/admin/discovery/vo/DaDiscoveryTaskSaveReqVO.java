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

package tech.qiantong.qdata.module.da.controller.admin.discovery.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Data Discovery Task Create/Update Request VO DA_DISCOVERY_TASK
 *
 * @author qdata
 * @date 2025-02-11
 */
@Schema(description = "数据发现任务 Response VO")
@Data
public class DaDiscoveryTaskSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "任务名称", example = "")
    @Size(max = 256, message = "任务名称长度不能超过256个字符")
    private String name;

    @Schema(description = "数据连接id", example = "")
    private Long datasourceId;

    @Schema(description = "任务状态", example = "")
    @Size(max = 256, message = "任务状态长度不能超过256个字符")
    private String status;

    @Schema(description = "cron执行表达式", example = "")
    @Size(max = 256, message = "cron执行表达式长度不能超过256个字符")
    private String cronExpression;

    @Schema(description = "联系人", example = "")
    @Size(max = 256, message = "联系人长度不能超过256个字符")
    private String contact;

    @Schema(description = "邮箱", example = "")
    @Size(max = 256, message = "邮箱长度不能超过256个字符")
    private String email;

    @Schema(description = "联系人ID", example = "")
    private Long contactId;

    /** Last changed table count */
    @Schema(description = "Last changed table count")
    private Long lastTableCount;

    @Schema(description = "联系电话", example = "")
    @Size(max = 256, message = "联系电话长度不能超过256个字符")
    private String contactNumber;

    @Schema(description = "类目编码", example = "")
    @Size(max = 256, message = "类目编码长度不能超过256个字符")
    private String catCode;

    @Schema(description = "描述", example = "")
    @Size(max = 256, message = "描述长度不能超过256个字符")
    private String description;

    @Schema(description = "定时任务调度表id", example = "")
    private Long systemJobId;

    @Schema(description = "最后执行时间", example = "")
    private Date lastExecuteTime;

    /** Node ID */
    @Schema(description = "Node ID", example = "")
    private Long nodeId;

    /** Node code */
    @Schema(description = "Node code", example = "")
    private String nodeCode;

    /** Task ID */
    @Schema(description = "Task ID", example = "")
    private Long taskId;

    /** Task code */
    @Schema(description = "Task code", example = "")
    private String taskCode;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;

    @TableField(exist = false)
    private String misfirePolicy;
    @TableField(exist = false)
    private String jobGroup;
    @TableField(exist = false)
    private String concurrent;
}
