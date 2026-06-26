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
