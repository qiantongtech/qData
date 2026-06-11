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

package tech.qiantong.qdata.module.att.controller.admin.theme.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * 主题 创建/修改 Request VO ATT_THEME
 *
 * @author qdata
 * @date 2025-01-20
 */
@Schema(description = "主题 Response VO")
@Data
public class AttThemeSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "主题名称", example = "")
    @Size(max = 256, message = "主题名称长度不能超过256个字符")
    private String name;

    @Schema(description = "图标url", example = "")
    @Size(max = 256, message = "图标url长度不能超过256个字符")
    private String icon;

    @Schema(description = "排序", example = "")
    private Long sortOrder;
    @Schema(description = "有效状态", example = "")
    private Boolean validFlag;

    @Schema(description = "描述", example = "")
    @Size(max = 256, message = "描述长度不能超过256个字符")
    private String description;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
