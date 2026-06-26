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

package tech.qiantong.qdata.module.dp.controller.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 物化模型记录 Request VO 对象 DP_MODEL_MATERIALIZED
 *
 * @author qdata
 * @date 2025-01-21
 */
@Schema(description = "物化模型记录 Request VO")
@Data
public class DpModelMaterializedPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "模型编码", example = "")
    private String modelName;

    @Schema(description = "模型名称", example = "")
    private String modelAlias;

    @Schema(description = "模型表id", example = "")
    private Long modelId;

    @Schema(description = "状态", example = "")
    private String status;

    @Schema(description = "执行日志信息", example = "")
    private String message;

    @Schema(description = "执行sql备份", example = "")
    private String sqlCommand;

    @Schema(description = "数据源id", example = "")
    private String datasourceId;

    @Schema(description = "数据源类型", example = "")
    private String datasourceType;

    @Schema(description = "数据源名称", example = "")
    private String datasourceName;

    @Schema(description = "资产表id", example = "")
    private String assetId;

    /**  发布模式 1：删除重建  2：增量发布 */
    private String releaseMode;


}
