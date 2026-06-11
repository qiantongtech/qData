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

package tech.qiantong.qdata.module.dp.controller.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * 物化模型记录 创建/修改 Request VO DP_MODEL_MATERIALIZED
 *
 * @author qdata
 * @date 2025-01-21
 */
@Schema(description = "物化模型记录 Response VO")
@Data
public class DpModelMaterializedSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "模型编码", example = "")
    @Size(max = 256, message = "模型编码长度不能超过256个字符")
    private String modelName;

    @Schema(description = "模型名称", example = "")
    @Size(max = 256, message = "模型名称长度不能超过256个字符")
    private String modelAlias;

    @Schema(description = "模型表id", example = "")
    private Long modelId;

    @Schema(description = "状态", example = "")
    @Size(max = 256, message = "状态长度不能超过256个字符")
    private String status;

    @Schema(description = "执行日志信息", example = "")
    @Size(max = 256, message = "执行日志信息长度不能超过256个字符")
    private String message;

    @Schema(description = "执行sql备份", example = "")
    @Size(max = 256, message = "执行sql备份长度不能超过256个字符")
    private String sqlCommand;

    @Schema(description = "数据源id", example = "")
    @Size(max = 256, message = "数据源id长度不能超过256个字符")
    private String datasourceId;

    @Schema(description = "数据源类型", example = "")
    @Size(max = 256, message = "数据源类型长度不能超过256个字符")
    private String datasourceType;

    @Schema(description = "数据源名称", example = "")
    @Size(max = 256, message = "数据源名称长度不能超过256个字符")
    private String datasourceName;

    @Schema(description = "资产表id", example = "")
    @Size(max = 256, message = "资产表id长度不能超过256个字符")
    private String assetId;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;

    /**  发布模式 1：删除重建  2：增量发布 */
    @Schema(description = " 发布模式 1：删除重建  2：增量发布", example = "")
    private String releaseMode;
}
