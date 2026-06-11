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

package tech.qiantong.qdata.module.da.controller.admin.assetchild.files.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 数据资产-文件服务 Request VO 对象 DA_ASSET_FILES
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
