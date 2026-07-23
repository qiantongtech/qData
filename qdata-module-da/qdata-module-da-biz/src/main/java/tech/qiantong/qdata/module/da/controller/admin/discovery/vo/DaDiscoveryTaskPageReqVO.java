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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

import java.util.Date;

/**
 * Data Discovery Task Request VO object DA_DISCOVERY_TASK
 *
 * @author qdata
 * @date 2025-02-11
 */
@Schema(description = "数据发现任务 Request VO")
@Data
public class DaDiscoveryTaskPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "任务名称", example = "")
    private String name;

    @Schema(description = "数据连接id", example = "")
    private Long datasourceId;

    @Schema(description = "任务状态", example = "")
    private String status;

    @Schema(description = "cron执行表达式", example = "")
    private String cronExpression;

    @Schema(description = "联系人", example = "")
    private String contact;

    @Schema(description = "邮箱", example = "")
    private String email;

    @Schema(description = "联系人ID", example = "")
    private Long contactId;

    /** Last changed table count */
    @Schema(description = "Last changed table count", example = "")
    private Long lastTableCount;

    @Schema(description = "联系电话", example = "")
    private String contactNumber;

    @Schema(description = "类目编码", example = "")
    private String catCode;

    @Schema(description = "描述", example = "")
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
}
