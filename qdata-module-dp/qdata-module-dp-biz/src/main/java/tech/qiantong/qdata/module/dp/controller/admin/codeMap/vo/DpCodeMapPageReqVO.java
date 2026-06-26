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

package tech.qiantong.qdata.module.dp.controller.admin.codeMap.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 数据元代码映射 Request VO 对象 DP_CODE_MAP
 *
 * @author qdata
 * @date 2025-01-21
 */
@Schema(description = "数据元代码映射 Request VO")
@Data
public class DpCodeMapPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "数据元id", example = "")
    private String dataElemId;

    @Schema(description = "原始值", example = "")
    private String originalValue;

    @Schema(description = "代码名", example = "")
    private String codeName;

    @Schema(description = "代码值", example = "")
    private String codeValue;




}
