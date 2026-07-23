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

package tech.qiantong.qdata.module.dp.controller.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Logical Model Save Request VO - DP_MODEL
 *
 * @author qdata
 * @date 2025-01-21
 */
@Schema(description = "逻辑模型 Response VO")
@Data
public class DpModelSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "模型编码", example = "")
    @NotBlank(message = "模型编码不能为空")
    @Size(max = 256, message = "模型编码长度不能超过256个字符")
    private String modelName;

    @Schema(description = "模型名称", example = "")
    @NotBlank(message = "模型名称不能为空")
    @Size(max = 256, message = "模型名称长度不能超过256个字符")
    private String modelComment;

    @Schema(description = "数据源id", example = "")
    private String datasourceId;

    private Long documentId;

    @Schema(description = "类目编码", example = "")
    @Size(max = 256, message = "类目编码长度不能超过256个字符")
    private String catCode;

    /**
     * Table Type; 1: Detail Table 2: Summary Table 3: Dimension Table 4: Application Table
     */
    @NotBlank(message = "表类型不能为空")
    @Schema(description = "表类型 1:明细表 2:汇总表 3:维度表 4:应用表")
    private String tableType;
    /**
     * Data Warehouse Layer ID
     */
    @Schema(description = "数仓分层id ")
    private Long dataLayerId;
    /**
     * Business Category ID; Only has value when table type is not Application Table
     */
    @Schema(description = "业务分类id 只有表类型为非应用表是才有值")
    private Long businessCategoryId;
    /**
     * Business Category Hierarchy Code
     */
    @Schema(description = "业务分类层级编码 ")
    private String businessCategoryCode;
    /**
     * Data Domain ID; Only has value when table type is not Application Table
     */
    @Schema(description = "数据分域id 只有表类型为非应用表是才有值")
    private Long dataDomainId;
    /**
     * Theme Domain ID (Theme Planning); Only has value when table type is Application Table
     */
    @Schema(description = "所属主题id（主题规划） 只有表类型为应用表是才有值")
    private Long themeDomainId;
    /**
     * Theme Domain Hierarchy Code
     */
    @Schema(description = "所属主题层级编码 ")
    private String themeDomainCode;
    /**
     * Table Name Case; 1: Uppercase 2: Lowercase
     */
    @Schema(description = "表名大小写 1：大写 2：小写")
    private String tableCase;

    @Schema(description = "状态", example = "")
    @NotBlank(message = "状态不能为空")
    @Size(max = 256, message = "状态长度不能超过256个字符")
    private String status;

    @Schema(description = "创建方式", example = "")
    @NotBlank(message = "创建方式不能为空")
    @Size(max = 256, message = "创建方式长度不能超过256个字符")
    private String createType;

    @Schema(description = "联系人", example = "")
    @Size(max = 256, message = "联系人长度不能超过256个字符")
    private String contact;

    @Schema(description = "联系电话", example = "")
    @Size(max = 256, message = "联系电话长度不能超过256个字符")
    private String contactNumber;

    @Schema(description = "描述", example = "")
    @Size(max = 256, message = "描述长度不能超过256个字符")
    private String description;


}
