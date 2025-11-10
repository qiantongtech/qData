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

package tech.qiantong.qdata.module.da.dal.dataobject.assetColumn;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tech.qiantong.qdata.common.annotation.Excel;
import tech.qiantong.qdata.common.core.domain.BaseEntity;
import tech.qiantong.qdata.module.dp.api.dataElem.dto.DpDataElemRuleRelRespDTO;

import java.util.List;
import java.util.Set;

/**
 * 数据资产字段 DO 对象 DA_ASSET_COLUMN
 *
 * @author lhs
 * @date 2025-01-21
 */
@Data
@TableName(value = "DA_ASSET_COLUMN")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("DA_ASSET_COLUMN_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DaAssetColumnDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 资产id
     */
    private Long assetId;

    /**
     * 字段名称/英文名称
     */
    private String columnName;

    /**
     * 字段注释/中文名称
     */
    private String columnComment;

    /**
     * 数据类型
     */
    private String columnType;

    /**
     * 长度
     */
    private Long columnLength;

    /**
     * 小数位
     */
    private Long columnScale;

    /**
     * 是否必填
     */
    private String nullableFlag;

    /**
     * 是否主键
     */
    private String pkFlag;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 是否代码
     */
    private String dataElemCodeFlag;

    /**
     * 代码id
     */
    private Long dataElemCodeId;

    /**
     * 敏感等级id
     */
    private Long sensitiveLevelId;

    /**
     * 关联数据元
     */
    private String relDataElmeFlag;

    /**
     * 关联清洗规则
     */
    private String relCleanFlag;

    /**
     * 关联稽查规则
     */
    private String relAuditFlag;

    /**
     * 描述
     */
    private String description;

    /**
     * 是否有效
     */
    private Boolean validFlag;

    /**
     * 删除标志
     */
    @TableLogic
    private Boolean delFlag;

    @TableField(exist = false)
    private Set<Long> elementId;

    @Excel(name = "关联数据元名称，多个逗号隔开")
    @Schema(description = "关联数据元名称，多个逗号隔开", example = "")
    @TableField(exist = false)
    private String relDataElmeName;

    @Excel(name = "敏感等级名称")
    @Schema(description = "敏感等级名称", example = "")
    @TableField(exist = false)
    private String sensitiveLevelName;

    @Excel(name = "代码表名称")
    @Schema(description = "代码表名称", example = "")
    @TableField(exist = false)
    private String dataElemCodeName;

    /**
     * 清洗规则列表
     */
    @TableField(exist = false)
    private List<DpDataElemRuleRelRespDTO> cleanRuleList;
}
