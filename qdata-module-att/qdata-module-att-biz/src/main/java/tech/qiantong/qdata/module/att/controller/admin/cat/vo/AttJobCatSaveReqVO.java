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

package tech.qiantong.qdata.module.att.controller.admin.cat.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Job Category Management Create/Update Request VO ATT_JOB_CAT
 *
 * @author qdata
 * @date 2025-03-11
 */
@Schema(description = "作业类目管理 Response VO")
@Data
public class AttJobCatSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "项目id")
    private Long projectId;

    @Schema(description = "项目code")
    private String projectCode;

    @Schema(description = "类别名称", example = "")
    @NotBlank(message = "类目名称不能为空")
    @Size(max = 50, message = "类目名称长度不能超过50个字符")
    @Pattern(regexp = "^(?=.*[A-Za-z0-9\\u4e00-\\u9fa5])\\S+$", message = "类目名称不能包含空白字符或仅由符号组成")
    private String name;

    @Schema(description = "关联上级ID", example = "")
    private Long parentId;

    @Schema(description = "类别排序", example = "")
    @Min(value = 0, message = "排序值不合法，请输入非负整数")
    private Long sortOrder;

    @Schema(description = "描述", example = "")
    @Size(max = 256, message = "描述长度不能超过256个字符")
    private String description;
    @Schema(description = "有效状态", example = "")
    private Boolean validFlag;
    @Schema(description = "层级编码", example = "")
    @Size(max = 256, message = "层级编码长度不能超过256个字符")
    private String code;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
