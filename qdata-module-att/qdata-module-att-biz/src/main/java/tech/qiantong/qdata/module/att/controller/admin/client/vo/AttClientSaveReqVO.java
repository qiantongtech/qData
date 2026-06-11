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

package tech.qiantong.qdata.module.att.controller.admin.client.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 应用管理 创建/修改 Request VO ATT_CLIENT
 *
 * @author qdata
 * @date 2025-02-18
 */
@Schema(description = "应用管理 Response VO")
@Data
public class AttClientSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "应用名称", example = "")
    @NotBlank(message = "应用名称不能为空")
    @Size(max = 128, message = "应用名称长度不能超过128个字符")
    private String name;

    @Schema(description = "应用类型", example = "")
    @NotBlank(message = "应用类型不能为空")
    private String type;

    @Schema(description = "主页地址", example = "")
    @Size(max = 1024, message = "主页地址长度不能超过1024个字符")
    private String homepageUrl;

    @Schema(description = "允许授权的url", example = "")
    @Size(max = 1024, message = "允许授权的url长度不能超过1024个字符")
    private String allowUrl;

    @Schema(description = "同步地址", example = "")
    @Size(max = 1024, message = "同步地址长度不能超过1024个字符")
    private String syncUrl;

    @Schema(description = "应用图标", example = "")
    private String logo;

    @Schema(description = "应用描述", example = "")
    @Size(max = 256, message = "应用描述长度不能超过256个字符")
    private String description;

    @Schema(description = "是否公开", example = "")
    private String publicFlag;

    @Schema(description = "备注", example = "")
    @Size(max = 1024, message = "备注长度不能超过1024个字符")
    private String remark;


}
