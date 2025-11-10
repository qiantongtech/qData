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
 * For brand customization, please contact the official team for authorization.
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
 * 如需定制品牌，请通过官方渠道申请品牌授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;

/**
 * 在线单设计器 Response VO 对象 DPP_ONL_DESFORM
 *
 * @author qdata
 * @date 2025-04-09
 */
@Schema(description = "在线单设计器 Response VO")
@Data
public class DppOnlDesformAndFormDataRespVO implements Serializable {

    private static final long serialVersionUID = 1L;


    @Excel(name = "ID")
    @Schema(description = "ID", example = "")
    private Long id;

    @Excel(name = "表单名称")
    @Schema(description = "表单名称", example = "")
    private String desformName;

    @Excel(name = "表单编码")
    @Schema(description = "表单编码", example = "")
    private String desformCode;

    @Excel(name = "表单JSON")
    @Schema(description = "表单JSON", example = "")
    private String desformJson;

    @Excel(name = "表单数据")
    @Schema(description = "表单数据", example = "")
    private String desformData;

    @Excel(name = "是否存储到指定表中")
    @Schema(description = "是否存储到指定表中", example = "")
    private String saveTableFlag;

    @Excel(name = "关联数据源id")
    @Schema(description = "关联数据源id", example = "")
    private Long datasourceId;

    @Excel(name = "数据库名")
    @Schema(description = "数据库名", example = "")
    private String databaseName;

    @Excel(name = "表名称")
    @Schema(description = "表名称", example = "")
    private String tableName;

    @Excel(name = "字段")
    @Schema(description = "字段", example = "")
    private String columnName;

    @Excel(name = "主键字段")
    @Schema(description = "主键字段", example = "")
    private String pkColumnName;


}
