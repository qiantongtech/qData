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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * Data Warehouse Layer Specification Create/Update Request VO - DM_DATA_LAYER_SPECIFICATION
 *
 * @author FXB
 * @date 2026-03-24
 */
@Schema(description = "数仓分层-规范管理 Response VO")
@Data
public class DmDataLayerSpecificationSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "数仓分层ID", example = "")
    private Long dataLayerId;

    @Schema(description = "表前缀", example = "")
    @Size(max = 256, message = "表前缀长度不能超过256个字符")
    private String prefixName;

    @Schema(description = "业务大类英文缩写", example = "")
    @Size(max = 256, message = "业务大类英文缩写长度不能超过256个字符")
    private String businessEngName;

    @Schema(description = "负责人ID", example = "")
    private Long ownerUserId;

    @Schema(description = "状态", example = "")
    @Size(max = 256, message = "状态长度不能超过256个字符")
    private String status;

    @Schema(description = "描述", example = "")
    @Size(max = 256, message = "描述长度不能超过256个字符")
    private String description;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
