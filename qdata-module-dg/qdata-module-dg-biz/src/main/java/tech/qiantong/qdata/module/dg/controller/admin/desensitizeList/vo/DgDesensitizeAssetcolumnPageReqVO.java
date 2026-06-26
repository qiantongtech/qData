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

package tech.qiantong.qdata.module.dg.controller.admin.desensitizeList.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 脱敏清单关联关系 Request VO 对象 DG_DESENSITIZE_ASSETCOLUMN
 *
 * @author qdata
 * @date 2026-04-12
 */
@Schema(description = "脱敏清单关联关系 Request VO")
@Data
public class DgDesensitizeAssetcolumnPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "资产ID", example = "")
    private Long assetId;

    @Schema(description = "资产字段ID", example = "")
    private Long assetcolumnId;

    @Schema(description = "资产名称", example = "")
    private String assetName;

    @Schema(description = "数据分类ID", example = "")
    private Long dataCategoryId;

    @Schema(description = "排序", example = "")
    private Long sortOrder;

    @Schema(description = "描述", example = "")
    private String description;

    @Schema(description = "是否有效;0：无效，1：有效", example = "")
    private Boolean validFlag;

    @Schema(description = "脱敏规则ID", example = "")
    private Long ruleId;

}
