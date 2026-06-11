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

package tech.qiantong.qdata.module.att.controller.admin.sourceSystem.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * 来源系统 创建/修改 Request VO ATT_SOURCE_SYSTEM
 *
 * @author qdata
 * @date 2026-04-03
 */
@Schema(description = "来源系统 Response VO")
@Data
public class AttSourceSystemSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "系统名称", example = "")
    @Size(max = 256, message = "系统名称长度不能超过256个字符")
    private String name;

    @Schema(description = "系统类型", example = "")
    @Size(max = 256, message = "系统类型长度不能超过256个字符")
    private String type;

    @Schema(description = "排序", example = "")
    private Long sortOrder;

    @Schema(description = "描述", example = "")
    @Size(max = 256, message = "描述长度不能超过256个字符")
    private String description;

    @Schema(description = "是否有效;0：无效，1：有效", example = "")
    private Boolean validFlag;

    @Schema(description = "负责人", example = "")
    @Size(max = 256, message = "负责人长度不能超过256个字符")
    private String responsiblePerson;

    @Schema(description = "对接人", example = "")
    @Size(max = 256, message = "对接人长度不能超过256个字符")
    private String contactPerson;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
