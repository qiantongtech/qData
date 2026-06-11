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
 * 数据集成任务-扩展数据 Request VO 对象 DPP_ETL_TASK_EXT
 *
 * @author qdata
 * @date 2025-04-16
 */
@Schema(description = "数据集成任务-扩展数据 Request VO")
@Data
public class DppEtlTaskExtPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "数据汇聚任务id", example = "")
    private Long taskId;

    @Schema(description = "数据汇聚节点id", example = "")
    private Long etlNodeId;

    @Schema(description = "数据汇聚节点名称", example = "")
    private String etlNodeName;

    @Schema(description = "数据汇聚节点编码", example = "")
    private String etlNodeCode;

    @Schema(description = "数据汇聚节点版本", example = "")
    private Long etlNodeVersion;

    @Schema(description = "数据汇聚节点关系id", example = "")
    private Long etlRelationId;




}
