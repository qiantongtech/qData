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

package tech.qiantong.qdata.module.da.controller.admin.daAssetApply.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

import java.util.Set;

/**
 * Data Asset Application Request VO DA_ASSET_APPLY
 *
 * @author shu
 * @date 2025-03-19
 */
@Schema(description = "数据资产申请 Request VO")
@Data
public class DaAssetApplyPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID", example = "")
    private Long id;

    @Schema(description = "资产id", example = "")
    private Long assetId;

    @Schema(description = "资产名称", example = "")
    private String assetName;

    @Schema(description = "资产英文名称", example = "")
    private String assetTableName;

    @Schema(description = "资产类目", example = "")
    private String catAssetName;

    @Schema(description = "资产类目编码", example = "")
    private String catAssetCode;

    @Schema(description = "项目id", example = "")
    private Long projectId;

    @Schema(description = "是申请过来的资产还是项目自己生成的资产0：申请，1：自创", example = "")
    private String sourceType;

    @Schema(description = "项目名称", example = "")
    private String projectName;

    @Schema(description = "主题名称", example = "")
    private String themeName;

    @Schema(description = "项目编码", example = "")
    private String projectCode;

    @Schema(description = "申请理由", example = "")
    private String applyReason;

    @Schema(description = "审批理由", example = "")
    private String approvalReason;

    @Schema(description = "状态", example = "")
    private String status;

    private Set<Long> assetIds;

}
