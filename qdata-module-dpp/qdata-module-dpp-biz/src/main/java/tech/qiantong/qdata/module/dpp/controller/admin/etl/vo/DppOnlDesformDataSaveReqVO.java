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

package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * 在线单数据 创建/修改 Request VO DPP_ONL_DESFORM_DATA
 *
 * @author qdata
 * @date 2025-04-09
 */
@Schema(description = "在线单数据 Response VO")
@Data
public class DppOnlDesformDataSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "表单编码", example = "")
    @Size(max = 256, message = "表单编码长度不能超过256个字符")
    private String desformCode;

    @Schema(description = "表单名称", example = "")
    @Size(max = 256, message = "表单名称长度不能超过256个字符")
    private String desformName;

    @Schema(description = "表单ID", example = "")
    @Size(max = 256, message = "表单ID长度不能超过256个字符")
    private String desformId;

    @Schema(description = "表单数据", example = "")
//    @Size(max = 256, message = "表单数据长度不能超过256个字符")
    private String desformData;

    @Schema(description = "备注", example = "")
//    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
