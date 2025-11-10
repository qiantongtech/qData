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

package tech.qiantong.qdata.module.da.controller.admin.assetColumn.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;
import java.util.Set;

/**
 * 数据资产字段 创建/修改 Request VO DA_ASSET_COLUMN
 *
 * @author lhs
 * @date 2025-01-21
 */
@Schema(description = "数据资产字段 Response VO")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DaAssetColumnSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "资产id", example = "")
    @Size(max = 256, message = "资产id长度不能超过256个字符")
    private String assetId;

    @Schema(description = "字段名称/英文名称", example = "")
    @Size(max = 256, message = "字段名称/英文名称长度不能超过256个字符")
    private String columnName;

    @Schema(description = "字段注释/中文名称", example = "")
    @Size(max = 256, message = "字段注释/中文名称长度不能超过256个字符")
    private String columnComment;

    @Schema(description = "数据类型", example = "")
    @Size(max = 256, message = "数据类型长度不能超过256个字符")
    private String columnType;

    @Schema(description = "长度", example = "")
    private Long columnLength;

    @Schema(description = "小数位", example = "")
    private Long columnScale;

    @Schema(description = "是否必填", example = "")
    @Size(max = 256, message = "是否必填长度不能超过256个字符")
    private String nullableFlag;

    @Schema(description = "是否主键", example = "")
    @Size(max = 256, message = "是否主键长度不能超过256个字符")
    private String pkFlag;

    @Schema(description = "默认值", example = "")
    @Size(max = 256, message = "默认值长度不能超过256个字符")
    private String defaultValue;

    @Schema(description = "是否代码", example = "")
    @Size(max = 256, message = "是否代码长度不能超过256个字符")
    private String dataElemCodeFlag;

    @Schema(description = "代码id", example = "")
    @Size(max = 256, message = "代码id长度不能超过256个字符")
    private String dataElemCodeId;

    @Schema(description = "敏感等级id", example = "")
    @Size(max = 256, message = "敏感等级id长度不能超过256个字符")
    private String sensitiveLevelId;

    @Schema(description = "关联数据元", example = "")
    @Size(max = 256, message = "关联数据元长度不能超过256个字符")
    private String relDataElmeFlag;

    @Schema(description = "关联清洗规则", example = "")
    @Size(max = 256, message = "关联清洗规则长度不能超过256个字符")
    private String relCleanFlag;

    @Schema(description = "关联稽查规则", example = "")
    @Size(max = 256, message = "关联稽查规则长度不能超过256个字符")
    private String relAuditFlag;

    @Schema(description = "描述", example = "")
    @Size(max = 256, message = "描述长度不能超过256个字符")
    private String description;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;

    /**
     * 数据元id
     */
    @TableField(exist = false)
    private Set<Long> elementId;
}
