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
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 逻辑模型 Request VO 对象 DP_MODEL
 *
 * @author qdata
 * @date 2025-01-21
 */
@Schema(description = "逻辑模型 Request VO")
@Data
public class DpModelPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;

    /**
     * 表类型;1:明细表 2:汇总表 3:维度表 4:应用表
     */
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

    @Schema(description = "模型编码", example = "")
    private String modelName;

    @Schema(description = "模型名称", example = "")
    private String modelComment;

    @Schema(description = "类目编码", example = "")
    private String catCode;


    @Schema(description = "状态", example = "")
    private String status;


    private Long documentId;


}
