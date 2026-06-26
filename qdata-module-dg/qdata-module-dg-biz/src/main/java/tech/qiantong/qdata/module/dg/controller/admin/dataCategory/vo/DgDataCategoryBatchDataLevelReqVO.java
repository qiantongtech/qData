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

package tech.qiantong.qdata.module.dg.controller.admin.dataCategory.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * 数据分类 批量設置数据分級 Request VO DG_DATA_CATEGORY
 *
 * @author qdata
 * @date 2026-04-07
 */
@Schema(description = "批量設置数据分級 Response VO")
@Data
public class DgDataCategoryBatchDataLevelReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ids")
    private List<Long> ids;


    @Schema(description = "数据分级", example = "")
    private Long dataLevelId;
}
