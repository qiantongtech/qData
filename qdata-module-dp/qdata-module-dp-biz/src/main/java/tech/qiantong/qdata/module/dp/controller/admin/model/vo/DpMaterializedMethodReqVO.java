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
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Materialized Method Save Request VO
 *
 * Used for createMaterializedTable
 *
 * @author qdata
 * @date 2025-01-21
 */
@Schema(description = "物化方法 Request VO")
@Data
public class DpMaterializedMethodReqVO extends BaseEntity {

    @Schema(description = "模型ID列表", example = "[1, 2, 3]", required = true)
    @NotNull(message = "模型ID列表不能为空")
    private List<Long> modelId;

    @Schema(description = "数据源ID", example = "1", required = true)
    @NotNull(message = "数据源ID不能为空")
    private Long datasourceId;

    @Schema(description = "数据源名称", example = "DataSource1")
    private String datasourceName;

    /**
     * Backend processing, not a front-end input field
     */
    private String dbName;

    @Schema(description = "数据源类型", example = "MySQL")
    private String datasourceType;

    @Schema(description = "数据源配置(json字符串)", example = "{\"username\":\"root\",\"password\":\"123456\"}")
    private String datasourceConfig;

    @Schema(description = "IP地址", example = "192.168.1.1")
    private String ip;

    @Schema(description = "端口号", example = "3306")
    private Long port;

    /** Release mode 1: Drop and recreate 2: Incremental publish */
    private String releaseMode;
}
