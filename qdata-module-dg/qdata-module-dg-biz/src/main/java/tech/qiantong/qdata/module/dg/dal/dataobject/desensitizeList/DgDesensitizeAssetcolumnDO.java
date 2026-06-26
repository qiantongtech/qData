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

package tech.qiantong.qdata.module.dg.dal.dataobject.desensitizeList;

import lombok.*;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * 脱敏清单关联关系 DO 对象 DG_DESENSITIZE_ASSETCOLUMN
 *
 * @author qdata
 * @date 2026-04-12
 */
@Data
@TableName(value = "DG_DESENSITIZE_ASSETCOLUMN")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("DG_DESENSITIZE_ASSETCOLUMN_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DgDesensitizeAssetcolumnDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 资产ID */
    private Long assetId;

    /** 资产名称 */
    @TableField(exist = false)
    private String assetName;
    /** 资产描述 */
    @TableField(exist = false)
    private String assetDescription;
    /** 资产英文表名 */
    @TableField(exist = false)
    private String assetTableName;
    /** 资产表名 */
    @TableField(exist = false)
    private String assetTableComment;

    /** 资产字段ID */
    private Long assetcolumnId;

    /** 资产字段名称*/
    @TableField(exist = false)
    private String assetcolumnName;
    /** 资产字段描述 */
    @TableField(exist = false)
    private String assetcolumnComment;
    /** 数据分类ID */
    private Long dataCategoryId;
    /** 数据分类名称 */
    @TableField(exist = false)
    private String dataCategoryName;
    /** 数据分级名称 */
    @TableField(exist = false)
    private String dataLevelName;
    /** 脱敏规则名称 */
    @TableField(exist = false)
    private String desensitizeRuleName;

    /** 排序 */
    private Long sortOrder;

    /** 描述 */
    private String description;

    /** 是否有效;0：无效，1：有效 */
    private Boolean validFlag;

    /** 删除标志;1：已删除，0：未删除 */
    @TableLogic
    private Boolean delFlag;


}
