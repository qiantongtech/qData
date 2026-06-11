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
 */

package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 数据集成节点实例-日志 Request VO 对象 DPP_ETL_NODE_INSTANCE_LOG
 *
 * @author qdata
 * @date 2025-08-05
 */
@Schema(description = "数据集成节点实例-日志 Request VO")
@Data
public class DppEtlNodeInstanceLogPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;


    @Schema(description = "任务类型", example = "")
    private String taskType;

    @Schema(description = "节点id", example = "")
    private Long nodeId;

    @Schema(description = "节点编码", example = "")
    private String nodeCode;

    @Schema(description = "任务实例id", example = "")
    private Long taskInstanceId;

    @Schema(description = "日志内容", example = "")
    private String logContent;




}
