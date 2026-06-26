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

package tech.qiantong.qdata.module.dg.controller.admin.dataCategory.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

import tech.qiantong.qdata.common.annotation.Excel;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * 数据分类 创建/修改 Request VO DG_DATA_CATEGORY
 *
 * @author qdata
 * @date 2026-04-07
 */
@Schema(description = "数据分类 Response VO")
@Data
public class DgDataCategorySaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "类目id", example = "")
    private Long catId;

    @Schema(description = "类目编码", example = "")
    @Size(max = 256, message = "类目编码长度不能超过256个字符")
    private String catCode;

    @Schema(description = "分类名称", example = "")
    @Size(max = 256, message = "分类名称长度不能超过256个字符")
    private String name;

    /** 分类名称缩写名 */
    @Schema(description = "分类名称缩写名", example = "")
    private String shortName;

    @Schema(description = "数据分级", example = "")
    private Long dataLevelId;


    @Excel(name = "是否有效;0：无效，1：有效")
    @Schema(description = "是否有效;0：无效，1：有效", example = "")
    private Boolean validFlag;

    @Schema(description = "任务优先级;HIGHEST,HIGH,MEDIUM,LOW,LOWEST", example = "")
    @Size(max = 256, message = "任务优先级;HIGHEST,HIGH,MEDIUM,LOW,LOWEST长度不能超过256个字符")
    private String priority;

    @Schema(description = "描述", example = "")
    @Size(max = 256, message = "描述长度不能超过256个字符")
    private String description;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
