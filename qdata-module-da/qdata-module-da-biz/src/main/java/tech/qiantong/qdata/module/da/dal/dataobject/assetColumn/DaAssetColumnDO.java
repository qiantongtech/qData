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

package tech.qiantong.qdata.module.da.dal.dataobject.assetColumn;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tech.qiantong.qdata.common.annotation.Excel;
import tech.qiantong.qdata.common.core.domain.BaseEntity;
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.module.dp.api.dataElem.dto.DpDataElemRuleRelRespDTO;
import tech.qiantong.qdata.module.mc.api.column.dto.McColumnRespDTO;

import java.util.List;
import java.util.Set;

/**
 * Data Asset Column DO - DA_ASSET_COLUMN
 *
 * @author lhs
 * @date 2025-01-21
 */
@Data
@TableName(value = "DA_ASSET_COLUMN")
// Used for auto-increment primary keys in Oracle, PostgreSQL, Kingbase, DB2, H2 databases. Not needed for MySQL and similar databases.
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
     * Asset ID
     */
    private Long assetId;

    /**
     * Column Name / English Name
     */
    private String columnName;

    /**
     * Column Comment / Chinese Name
     */
    private String columnComment;

    /**
     * Data Type
     */
    private String columnType;

    /**
     * Length
     */
    private Long columnLength;

    /**
     * Decimal Places
     */
    private Long columnScale;

    /**
     * Required Flag
     */
    private String nullableFlag;

    /**
     * Primary Key Flag
     */
    private String pkFlag;

    /**
     * Default Value
     */
    private String defaultValue;

    /**
     * Data Element Code Flag
     */
    private String dataElemCodeFlag;

    /**
     * Data Element Code ID
     */
    private Long dataElemCodeId;

    /**
     * Sensitive Level ID
     */
    private Long sensitiveLevelId;

    /**
     * Associated Data Element
     */
    private String relDataElmeFlag;

    /**
     * Associated Cleansing Rule
     */
    private String relCleanFlag;

    /**
     * Associated Audit Rule
     */
    private String relAuditFlag;

    /**
     * Description
     */
    private String description;

    /**
     * Valid Flag
     */
    private Boolean validFlag;

    /**
     * Delete Flag
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
     * Cleansing Rule List
     */
    @TableField(exist = false)
    private List<DpDataElemRuleRelRespDTO> cleanRuleList;

    public DaAssetColumnDO(McColumnRespDTO column) {
        if (column != null) {
            this.columnLength = (column.getColumnLength() != null) ? Long.valueOf(column.getColumnLength()) : null;
            this.columnName = column.getColumnName();
            this.columnComment = column.getColumnComment();
            this.columnType = column.getColumnType();
            this.columnScale = (column.getColumnScale() != null) ? Long.valueOf(column.getColumnScale()) : null;
            this.defaultValue = column.getDefaultValue();
            this.pkFlag = column.getPkFlag();
            this.nullableFlag = column.getNullableFlag();
        }
    }

    public DbColumn toDbColumn() {
        return DbColumn.builder()
                .colName(this.columnName)
                .colComment(this.columnComment)
                .dataType(this.columnType)
                .dataLength(String.valueOf(this.columnLength))
                .dataPrecision(String.valueOf(this.columnLength))
                .dataScale(String.valueOf(this.columnScale))
                .colKey(this.pkFlag.equals("1"))
                .nullable(this.nullableFlag.equals("0"))
                .build();
    }
}
