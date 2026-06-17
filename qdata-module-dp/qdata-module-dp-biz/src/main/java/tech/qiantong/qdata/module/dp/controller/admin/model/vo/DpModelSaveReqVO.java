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
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.module.dp.controller.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 逻辑模型 创建/修改 Request VO DP_MODEL
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
     * 表类型;1:明细表 2:汇总表 3:维度表 4:应用表
     */
    @NotBlank(message = "表类型不能为空")
    @Schema(description = "表类型 1:明细表 2:汇总表 3:维度表 4:应用表")
    private String tableType;
    /**
     * 数仓分层id
     */
    @Schema(description = "数仓分层id ")
    private Long dataLayerId;
    /**
     * 业务分类id;只有表类型为非应用表是才有值
     */
    @Schema(description = "业务分类id 只有表类型为非应用表是才有值")
    private Long businessCategoryId;
    /**
     * 业务分类层级编码
     */
    @Schema(description = "业务分类层级编码 ")
    private String businessCategoryCode;
    /**
     * 数据分域id;只有表类型为非应用表是才有值
     */
    @Schema(description = "数据分域id 只有表类型为非应用表是才有值")
    private Long dataDomainId;
    /**
     * 所属主题id（主题规划）;只有表类型为应用表是才有值
     */
    @Schema(description = "所属主题id（主题规划） 只有表类型为应用表是才有值")
    private Long themeDomainId;
    /**
     * 所属主题层级编码
     */
    @Schema(description = "所属主题层级编码 ")
    private String themeDomainCode;
    /**
     * 表名大小写;1：大写 2：小写
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
