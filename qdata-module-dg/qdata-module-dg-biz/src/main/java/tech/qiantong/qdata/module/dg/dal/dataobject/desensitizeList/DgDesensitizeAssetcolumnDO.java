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
 * Desensitize List - Asset Column Relation DO entity DG_DESENSITIZE_ASSETCOLUMN
 *
 * @author qdata
 * @date 2026-04-12
 */
@Data
@TableName(value = "DG_DESENSITIZE_ASSETCOLUMN")
// Used for auto-increment primary keys in Oracle, PostgreSQL, Kingbase, DB2, H2 databases. Can be omitted for MySQL and similar databases.
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

    /** Asset ID */
    private Long assetId;

    /** Asset Name */
    @TableField(exist = false)
    private String assetName;
    /** Asset Description */
    @TableField(exist = false)
    private String assetDescription;
    /** Asset English Table Name */
    @TableField(exist = false)
    private String assetTableName;
    /** Asset Table Comment */
    @TableField(exist = false)
    private String assetTableComment;

    /** Asset Column ID */
    private Long assetcolumnId;

    /** Asset Column Name */
    @TableField(exist = false)
    private String assetcolumnName;
    /** Asset Column Description */
    @TableField(exist = false)
    private String assetcolumnComment;
    /** Data Category ID */
    private Long dataCategoryId;
    /** Data Category Name */
    @TableField(exist = false)
    private String dataCategoryName;
    /** Data Level Name */
    @TableField(exist = false)
    private String dataLevelName;
    /** Desensitize Rule Name */
    @TableField(exist = false)
    private String desensitizeRuleName;

    /** Sort Order */
    private Long sortOrder;

    /** Description */
    private String description;

    /** Valid Flag; 0: invalid, 1: valid */
    private Boolean validFlag;

    /** Delete Flag; 1: deleted, 0: not deleted */
    @TableLogic
    private Boolean delFlag;


}
