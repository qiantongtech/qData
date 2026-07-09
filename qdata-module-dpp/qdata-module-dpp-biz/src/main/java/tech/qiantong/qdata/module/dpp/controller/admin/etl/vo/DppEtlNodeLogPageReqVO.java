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
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * Data Integration Node-Log Request VO Object DPP_ETL_NODE_LOG
 *
 * @author qdata
 * @date 2025-02-13
 */
@Schema(description = "数据集成节点-日志 Request VO")
@Data
public class DppEtlNodeLogPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "节点类型", example = "")
    private String type;

    @Schema(description = "节点名称", example = "")
    private String name;

    @Schema(description = "节点编码", example = "")
    private String code;

    @Schema(description = "节点版本", example = "")
    private Long version;

    @Schema(description = "项目id", example = "")
    private Long projectId;

    @Schema(description = "项目编码", example = "")
    private String projectCode;

    @Schema(description = "节点参数", example = "")
    private String parameters;

    @Schema(description = "任务优先级", example = "")
    private String priority;

    @Schema(description = "失败重试次数", example = "")
    private Long failRetryTimes;

    @Schema(description = "失败重试间隔", example = "")
    private Long failRetryInterval;

    @Schema(description = "超时时间", example = "")
    private Long timeout;

    @Schema(description = "延迟执行时间", example = "")
    private Long delayTime;

    @Schema(description = "CPU配额", example = "")
    private Long cpuQuota;

    @Schema(description = "最大内存", example = "")
    private Long memoryMax;

    @Schema(description = "描述", example = "")
    private String description;

    @Schema(description = "DolphinScheduler的id", example = "")
    private Long dsId;




}
