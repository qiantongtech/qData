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

package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * 数据集成任务-扩展数据 创建/修改 Request VO DPP_ETL_TASK_EXT
 *
 * @author qdata
 * @date 2025-04-16
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "数据集成任务-扩展数据 Response VO")
@Data
public class DppEtlTaskExtSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "数据汇聚任务id", example = "")
    private Long taskId;

    @Schema(description = "数据汇聚任务编码", example = "")
    private String etlTaskCode;

    @Schema(description = "数据汇聚任务版本", example = "")
    private Integer etlTaskVersion;

    @Schema(description = "数据汇聚节点id", example = "")
    private Long etlNodeId;

    @Schema(description = "数据汇聚节点名称", example = "")
    @Size(max = 256, message = "数据汇聚节点名称长度不能超过256个字符")
    private String etlNodeName;

    @Schema(description = "数据汇聚节点编码", example = "")
    @Size(max = 256, message = "数据汇聚节点编码长度不能超过256个字符")
    private String etlNodeCode;

    @Schema(description = "数据汇聚节点版本", example = "")
    private Integer etlNodeVersion;

    @Schema(description = "数据汇聚节点关系id", example = "")
    private Long etlRelationId;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
