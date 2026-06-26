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
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 应用管理 Request VO 对象 ATT_CLIENT
 *
 * @author qdata
 * @date 2025-02-18
 */
@Schema(description = "应用管理 Request VO")
@Data
public class AttClientPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID", example = "")
    private Long id;

    @Schema(description = "应用名称", example = "")
    private String name;

    @Schema(description = "应用类型", example = "")
    private String type;

    @Schema(description = "应用秘钥", example = "")
    private String secret;

    @Schema(description = "主页地址", example = "")
    private String homepageUrl;

    @Schema(description = "允许授权的url", example = "")
    private String allowUrl;

    @Schema(description = "同步地址", example = "")
    private String syncUrl;

    @Schema(description = "应用图标", example = "")
    private String logo;

    @Schema(description = "应用描述", example = "")
    private String description;

    @Schema(description = "是否公开", example = "")
    private String publicFlag;




}
