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

package tech.qiantong.qdata.module.dpp.controller.admin.qa.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * Data Quality Task Request VO DPP_QUALITY_TASK
 *
 * @author Chaos
 * @date 2025-07-21
 */
@Schema(description = "Data Quality Task Page Request VO")
@Data
public class DppQualityTaskPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "任务名称", example = "")
    private String taskName;

    @Schema(description = "类目编码", example = "")
    private String catCode;

    @Schema(description = "联系人", example = "")
    private String contact;

    @Schema(description = "联系人ID", example = "")
    private String contactId;

    @Schema(description = "联系电话", example = "")
    private String contactNumber;

    @Schema(description = "任务状态; 0:下线，1:上线", example = "")
    private String status;

    @Schema(description = "任务描述", example = "")
    private String description;

    @Schema(description = "任务优先级", example = "")
    private String priority;

    @Schema(description = "Worker分组", example = "")
    private String workerGroup;

    @Schema(description = "失败重试次数", example = "")
    private Long retryTimes;

    @Schema(description = "失败重试间隔(秒)", example = "")
    private Long retryInterval;

    @Schema(description = "延时执行时间(秒)", example = "")
    private Long delayTime;

    @Schema(description = "执行策略", example = "")
    private String strategy;

    @Schema(description = "调度周期", example = "")
    private String cycle;

    @Schema(description = "是否是资产质量任务;0：否。1是")
    private String assetFlag;

    @Schema(description = "资产id")
    private Long assetId;


    /** Node ID */
    @Schema(description = "节点id", example = "")
    private Long nodeId;

    /** Node Code */
    @Schema(description = "节点编码", example = "")
    private String nodeCode;

    /** Task ID */
    @Schema(description = "任务id", example = "")
    private Long taskId;

    /** Task Code */
    @Schema(description = "任务编码", example = "")
    private String taskCode;

    @Schema(description = "定时任务调度表id", example = "")
    private Long systemJobId;




}
