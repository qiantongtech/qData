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

package tech.qiantong.qdata.module.dm.controller.admin.dm.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 数仓分层-规范管理 Request VO 对象 DM_DATA_LAYER_SPECIFICATION
 *
 * @author FXB
 * @date 2026-03-24
 */
@Schema(description = "数仓分层-规范管理 Request VO")
@Data
public class DmDataLayerSpecificationPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "数仓分层ID", example = "")
    private Long dataLayerId;

    @Schema(description = "表前缀", example = "")
    private String prefixName;

    @Schema(description = "业务大类英文缩写", example = "")
    private String businessEngName;

    @Schema(description = "负责人ID", example = "")
    private Long ownerUserId;

    @Schema(description = "状态", example = "")
    private String status;

    @Schema(description = "描述", example = "")
    private String description;




}
