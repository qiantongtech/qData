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

package tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

/**
 * 数据资产-外部API Request VO 对象 DA_ASSET_API
 *
 * @author qdata
 * @date 2025-04-14
 */
@Schema(description = "数据资产-外部API Request VO")
@Data
public class DaAssetApiReqVO {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "资产id", example = "")
    private Long assetId;

    @Schema(description = "API路径", example = "")
    private String url;

    @Schema(description = "请求方式", example = "")
    private String httpMethod;

    @Schema(description = "开发者", example = "")
    private String developerName;

    @Schema(description = "应用名称", example = "")
    private String appName;

    Map<String, Object> queryParams;
}
