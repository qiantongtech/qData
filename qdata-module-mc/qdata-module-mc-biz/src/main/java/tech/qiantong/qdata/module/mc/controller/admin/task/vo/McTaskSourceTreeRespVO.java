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
 * Business License: https://docs.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.module.mc.controller.admin.task.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Source system tree structure Response VO
 *
 * @author qdata
 * @date 2026-04-27
 */
@Schema(description = "来源系统树形结构 Response VO")
@Data
public class McTaskSourceTreeRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Node ID
     * - SOURCE node: source system ID
     * - DATASOURCE node: data source ID
     * - DATABASE node: task ID (use task ID uniformly to facilitate subsequent queries)
     */
    @Schema(description = "节点ID", example = "1")
    private Long id;

    /**
     * Node name
     */
    @Schema(description = "节点名称", example = "名称")
    private String name;

    /**
     * Node type: SOURCE-source system, DATASOURCE-data source, DATABASE-database
     */
    @Schema(description = "节点类型: SOURCE-来源系统, DATASOURCE-数据源, DATABASE-数据库", example = "SOURCE")
    private String type;

    @Schema(description = "数据源类型")
    private String datasourceType;


    @Schema(description = "采集任务ID")
    private Long taskId;

    /**
     * List of child nodes
     */
    @Schema(description = "子节点列表")
    private List<McTaskSourceTreeRespVO> children;
}
