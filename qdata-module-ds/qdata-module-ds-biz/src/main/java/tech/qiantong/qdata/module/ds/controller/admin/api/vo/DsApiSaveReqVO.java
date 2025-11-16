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
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.module.ds.controller.admin.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * API服务 创建/修改 Request VO DS_API
 *
 * @author lhs
 * @date 2025-02-12
 */
@Schema(description = "API服务 Response VO")
@Data
public class DsApiSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "类目id")
    private Long catId;

    @Schema(description = "类目编码")
    private String catCode;

    @Schema(description = "API服务名称", example = "")
    @Size(max = 256, message = "API服务名称长度不能超过256个字符")
    private String name;

    @Schema(description = "类目名称", example = "")
    private String catName;

    /**
     *转发类型;1:API 2:地理空间数据'
     */
    private String transmitType;

    /**
     *apiId
     */
    private String apiId;

    /**
     *Header配置json
     */
    private String headerJson;

    @Schema(description = "API版本", example = "")
    @Size(max = 256, message = "API版本长度不能超过256个字符")
    private String apiVersion;

    @Schema(description = "API路径", example = "")
    @Size(max = 256, message = "API路径长度不能超过256个字符")
    private String apiUrl;

    @Schema(description = "请求方式", example = "")
    @Size(max = 256, message = "请求方式长度不能超过256个字符")
    private String reqMethod;

    @Schema(description = "服务提供类型", example = "")
    @Size(max = 256, message = "服务提供类型长度不能超过256个字符")
    private String apiServiceType;

    @Schema(description = "返回结果类型", example = "")
    @Size(max = 256, message = "返回结果类型长度不能超过256个字符")
    private String resDataType;

    @Schema(description = "IP黑名单多个，隔开", example = "")
    @Size(max = 256, message = "IP黑名单多个，隔开长度不能超过256个字符")
    private String denyIp;

    @Schema(description = "执行配置JSON", example = "")
    @Size(max = 256, message = "执行配置JSON长度不能超过256个字符")
    private String configJson;

    @Schema(description = "限流配置JSON", example = "")
    @Size(max = 256, message = "限流配置JSON长度不能超过256个字符")
    private String limitJson;

    @Schema(description = "请求参数", example = "")
    @Size(max = 256, message = "请求参数长度不能超过256个字符")
    private String reqParams;

    @Schema(description = "返回参数", example = "")
    @Size(max = 256, message = "返回参数长度不能超过256个字符")
    private String resParams;

    @Schema(description = "描述", example = "")
    @Size(max = 256, message = "描述长度不能超过256个字符")
    private String description;

    @Schema(description = "状态", example = "")
    @Size(max = 256, message = "状态长度不能超过256个字符")
    private String status;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
