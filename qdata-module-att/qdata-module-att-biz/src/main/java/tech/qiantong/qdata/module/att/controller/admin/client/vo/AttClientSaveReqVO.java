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
