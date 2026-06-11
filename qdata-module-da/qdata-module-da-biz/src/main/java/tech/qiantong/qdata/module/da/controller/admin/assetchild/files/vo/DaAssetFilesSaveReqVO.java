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
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * 数据资产-文件服务 创建/修改 Request VO DA_ASSET_FILES
 *
 * @author qdata
 * @date 2025-06-26
 */
@Schema(description = "数据资产-文件服务 Response VO")
@Data
public class DaAssetFilesSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "资产id", example = "")
    private Long assetId;
    @Schema(description = "起始列", example = "")
    private Integer startColumn;
    @Schema(description = "起始行", example = "")
    private Integer startData;

    @Schema(description = "文件名称", example = "")
    @Size(max = 256, message = "文件名称长度不能超过256个字符")
    private String name;

    @Schema(description = "文件地址", example = "")
    @Size(max = 256, message = "文件地址长度不能超过256个字符")
    private String url;

    @Schema(description = "文件类型", example = "")
    @Size(max = 256, message = "文件类型长度不能超过256个字符")
    private String type;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
