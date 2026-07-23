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

package tech.qiantong.qdata.module.ds.controller.admin.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

/**
 * API service Request VO DS_API
 *
 * @author lhs
 * @date 2025-02-12
 */
@Schema(description = "API服务 Request VO")
@Data
public class DsApiReqVO{

    private static final long serialVersionUID = 1L;

    @Schema(description = "类目id", example = "")
    private Long catId;

    @Schema(description = "类目编码", example = "")
    private String catCode;

    @Schema(description = "类目名称", example = "")
    private String catName;

    @Schema(description = "API服务名称", example = "")
    private String name;

    @Schema(description = "状态", example = "")
    private String status;

    private String startDate;

    private String endDate;

    /**
     *Forwarding type (1: API, 2: geospatial data)
     */
    private String transmitType;

    /**
     *apiId
     */
    private String apiId;

    /**
     *Header configuration JSON
     */
    private String headerJson;

    Map<String, Object> queryParams;

}
