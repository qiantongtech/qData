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

package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * 在线单设计器 创建/修改 Request VO DPP_ONL_DESFORM
 *
 * @author qdata
 * @date 2025-04-09
 */
@Schema(description = "在线单设计器 Response VO")
@Data
public class DppOnlDesformSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "表单名称", example = "")
    @Size(max = 256, message = "表单名称长度不能超过256个字符")
    private String desformName;

    @Schema(description = "表单编码", example = "")
    @Size(max = 256, message = "表单编码长度不能超过256个字符")
    private String desformCode;

    @Schema(description = "表单JSON", example = "")
//    @Size(max = 256, message = "表单JSON长度不能超过256个字符")
    private String desformJson;

    @Schema(description = "是否存储到指定表中", example = "")
    @Size(max = 256, message = "是否存储到指定表中长度不能超过256个字符")
    private String saveTableFlag;

    @Schema(description = "关联数据源id", example = "")
    private Long datasourceId;

    @Schema(description = "数据库名", example = "")
    @Size(max = 256, message = "数据库名长度不能超过256个字符")
    private String databaseName;

    @Schema(description = "表名称", example = "")
    @Size(max = 256, message = "表名称长度不能超过256个字符")
    private String tableName;

    @Schema(description = "字段", example = "")
    @Size(max = 256, message = "字段长度不能超过256个字符")
    private String columnName;

    @Schema(description = "主键字段", example = "")
    @Size(max = 256, message = "主键字段长度不能超过256个字符")
    private String pkColumnName;

    @Schema(description = "是否程序生成主键的值", example = "")
    @Size(max = 256, message = "是否程序生成主键的值长度不能超过256个字符")
    private String createPkDataFlag;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
