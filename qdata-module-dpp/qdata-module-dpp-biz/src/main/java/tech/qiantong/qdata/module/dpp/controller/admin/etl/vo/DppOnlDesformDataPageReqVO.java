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
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 在线单数据 Request VO 对象 DPP_ONL_DESFORM_DATA
 *
 * @author qdata
 * @date 2025-04-09
 */
@Schema(description = "在线单数据 Request VO")
@Data
public class DppOnlDesformDataPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;

    @Schema(description = "表单编码", example = "")
    private String desformCode;

    @Schema(description = "表单名称", example = "")
    private String desformName;

    @Schema(description = "表单ID", example = "")
    private String desformId;

    @Schema(description = "表单数据", example = "")
    private String desformData;




}
