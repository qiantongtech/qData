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
