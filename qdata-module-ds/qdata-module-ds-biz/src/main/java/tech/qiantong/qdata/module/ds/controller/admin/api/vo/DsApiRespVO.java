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
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * API service Response VO DS_API
 *
 * @author lhs
 * @date 2025-02-12
 */
@Schema(description = "API服务 Response VO")
@Data
public class DsApiRespVO implements Serializable {

    private static final long serialVersionUID = 1L;


    @Excel(name = "ID")
    @Schema(description = "ID", example = "")
    private Long id;

    @Excel(name = "类目id")
    @Schema(description = "类目id")
    private Long catId;

    @Excel(name = "类目编码")
    @Schema(description = "类目编码")
    private String catCode;

    @Excel(name = "类目名称")
    @Schema(description = "类目名称", example = "")
    private String catName;

    @Excel(name = "API服务名称")
    @Schema(description = "API服务名称", example = "")
    private String name;

    @Excel(name = "API版本")
    @Schema(description = "API版本", example = "")
    private String apiVersion;

    @Excel(name = "API路径")
    @Schema(description = "API路径", example = "")
    private String apiUrl;

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

    @Excel(name = "请求方式")
    @Schema(description = "请求方式", example = "")
    private String reqMethod;

    @Excel(name = "服务提供类型")
    @Schema(description = "服务提供类型", example = "")
    private String apiServiceType;

    @Excel(name = "返回结果类型")
    @Schema(description = "返回结果类型", example = "")
    private String resDataType;

    @Excel(name = "IP黑名单多个，隔开")
    @Schema(description = "IP黑名单多个，隔开", example = "")
    private String denyIp;

    @Excel(name = "执行配置JSON")
    @Schema(description = "执行配置JSON", example = "")
    private String configJson;

    @Excel(name = "限流配置JSON")
    @Schema(description = "限流配置JSON", example = "")
    private String limitJson;

    @Excel(name = "请求参数")
    @Schema(description = "请求参数", example = "")
    private String reqParams;

    @Excel(name = "返回参数")
    @Schema(description = "返回参数", example = "")
    private String resParams;

    @Excel(name = "描述")
    @Schema(description = "描述", example = "")
    private String description;

    @Excel(name = "状态")
    @Schema(description = "状态", example = "")
    private String status;

    @Excel(name = "是否有效")
    @Schema(description = "是否有效", example = "")
    private Boolean validFlag;

    @Excel(name = "删除标志")
    @Schema(description = "删除标志", example = "")
    private Boolean delFlag;

    @Excel(name = "创建人")
    @Schema(description = "创建人", example = "")
    private String createBy;

    @Excel(name = "创建人id")
    @Schema(description = "创建人id", example = "")
    private Long creatorId;

    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "")
    private Date createTime;

    @Excel(name = "更新人")
    @Schema(description = "更新人", example = "")
    private String updateBy;

    @Excel(name = "更新人id")
    @Schema(description = "更新人id", example = "")
    private Long updaterId;

    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", example = "")
    private Date updateTime;

    @Excel(name = "备注")
    @Schema(description = "备注", example = "")
    private String remark;

}
