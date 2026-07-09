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

package tech.qiantong.qdata.module.da.controller.admin.assetchild.gis.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * Data Asset - Geospatial Service Create/Update Request VO DA_ASSET_GIS
 *
 * @author qdata
 * @date 2025-04-14
 */
@Schema(description = "数据资产-地理空间服务 Response VO")
@Data
public class DaAssetGisSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "资产id", example = "")
    private Long assetId;

    @Schema(description = "服务地址", example = "")
    @Size(max = 256, message = "服务地址长度不能超过256个字符")
    private String url;

    @Schema(description = "服务类型", example = "")
    @Size(max = 256, message = "服务类型长度不能超过256个字符")
    private String type;

    @Schema(description = "请求方式", example = "")
    @Size(max = 256, message = "请求方式长度不能超过256个字符")
    private String httpMethod;

    @Schema(description = "坐标系", example = "")
    @Size(max = 256, message = "坐标系长度不能超过256个字符")
    private String coordinateSystem;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
