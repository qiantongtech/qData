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

package tech.qiantong.qdata.module.da.controller.admin.assetchild.files.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * Data Asset - File Service Request VO Object DA_ASSET_FILES
 *
 * @author qdata
 * @date 2025-06-26
 */
@Schema(description = "数据资产-文件服务 Request VO")
@Data
public class DaAssetFilesPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "资产id", example = "")
    private Long assetId;

    @Schema(description = "文件名称", example = "")
    private String name;
    @Schema(description = "起始列", example = "")
    private Integer startColumn;
    @Schema(description = "起始行", example = "")
    private Integer startData;
    @Schema(description = "文件地址", example = "")
    private String url;

    @Schema(description = "文件类型", example = "")
    private String type;




}
