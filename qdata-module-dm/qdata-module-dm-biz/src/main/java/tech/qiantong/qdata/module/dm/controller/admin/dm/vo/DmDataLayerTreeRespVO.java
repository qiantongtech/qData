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

package tech.qiantong.qdata.module.dm.controller.admin.dm.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Data Warehouse Layer Tree Response VO - DM_DATA_LAYER
 *
 * @author FXB
 * @date 2026-03-24
 */
@Schema(description = "数仓分层管理 Response VO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DmDataLayerTreeRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    /** parentId */
    private Long parentId;

    @Excel(name = "名称")
    @Schema(description = "名称", example = "")
    private String name;

    @Excel(name = "英文缩写")
    @Schema(description = "英文缩写", example = "")
    private String engName;

    @Excel(name = "负责人ID")
    @Schema(description = "负责人ID", example = "")
    private Long ownerUserId;

    @Excel(name = "分类")
    @Schema(description = "分类", example = "")
    private String category;

    @Excel(name = "描述")
    @Schema(description = "描述", example = "")
    private String description;

    @Schema(description = "子列表", example = "")
    private List<DmDataLayerTreeRespVO> children;
}
