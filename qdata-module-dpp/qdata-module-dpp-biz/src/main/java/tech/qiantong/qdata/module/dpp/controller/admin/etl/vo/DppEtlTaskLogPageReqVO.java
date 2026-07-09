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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * Data Integration Task-Log Request VO Object DPP_ETL_TASK_LOG
 *
 * @author qdata
 * @date 2025-02-13
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data Integration Task-Log Request VO")
@Data
public class DppEtlTaskLogPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;
    @Schema(description = "任务类型", example = "")
    private String type;

    @Schema(description = "任务名称", example = "")
    private String name;

    @Schema(description = "任务编码", example = "")
    private String code;

    @Schema(description = "任务版本", example = "")
    private Integer version;

    @Schema(description = "项目id", example = "")
    private Long projectId;

    @Schema(description = "项目编码", example = "")
    private String projectCode;

    @Schema(description = "责任人", example = "")
    private String personCharge;

    @Schema(description = "节点坐标信息", example = "")
    private String locations;

    @Schema(description = "描述", example = "")
    private String description;

    @Schema(description = "超时时间", example = "")
    private Long timeout;

    @Schema(description = "抽取量", example = "")
    private Long extractionCount;

    @Schema(description = "写入量", example = "")
    private Long writeCount;

    @Schema(description = "任务状态", example = "")
    private String status;

    @Schema(description = "DolphinScheduler的id", example = "")
    private Long dsId;




}
