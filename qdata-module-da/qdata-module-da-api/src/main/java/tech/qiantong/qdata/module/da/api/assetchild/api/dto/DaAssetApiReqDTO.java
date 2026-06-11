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

package tech.qiantong.qdata.module.da.api.assetchild.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 数据资产-外部API DTO 对象 DA_ASSET_API
 *
 * @author qdata
 * @date 2025-04-14
 */
@Data
public class DaAssetApiReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 资产id */
    private Long assetId;

    /** API路径 */
    private String url;

    @Schema(description = "开发者", example = "")
    private String developerName;

    @Schema(description = "应用名称", example = "")
    private String appName;

    /** 请求方式 */
    private String httpMethod;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
