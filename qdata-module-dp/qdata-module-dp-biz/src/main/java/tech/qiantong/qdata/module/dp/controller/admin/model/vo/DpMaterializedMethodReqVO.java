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

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 物化方法 创建/修改 Request VO
 *
 * 用于 createMaterializedTable
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
     * 后端流转，非前端传递字段
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

    /**  发布模式 1：删除重建  2：增量发布 */
    private String releaseMode;
}
