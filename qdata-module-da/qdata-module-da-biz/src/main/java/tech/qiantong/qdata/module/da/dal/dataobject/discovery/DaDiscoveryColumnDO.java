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

package tech.qiantong.qdata.module.da.dal.dataobject.discovery;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.utils.ConversionUtils;

/**
 * Data Discovery Column DO - DA_DISCOVERY_COLUMN
 *
 * @author qdata
 * @date 2025-02-11
 */
@Data
@TableName(value = "DA_DISCOVERY_COLUMN")
// Used for auto-increment primary keys in Oracle, PostgreSQL, Kingbase, DB2, H2 databases. Not needed for MySQL and similar databases.
// @KeySequence("DA_DISCOVERY_COLUMN_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DaDiscoveryColumnDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** Task ID */
    private Long taskId;

    /** Discovery Table ID */
    private Long tableId;

    /** Column Name / English Name */
    private String columnName;

    /** Column Comment / Chinese Name */
    private String columnComment;

    /** Data Type */
    private String columnType;

    /** Length */
    private Long columnLength;

    /** Decimal Places */
    private Long columnScale;

    /** Required Flag */
    private String nullableFlag;

    /** Primary Key Flag */
    private String pkFlag;

    /** Default Value */
    private String defaultValue;

    /** Valid Flag */
    private Boolean validFlag;

    /** Delete Flag */
    @TableLogic
    private Boolean delFlag;


    public DaDiscoveryColumnDO(Long taskId, Long discoveryTableId, DbColumn column) {
        this.setTaskId(taskId);
        this.setTableId(discoveryTableId);
        this.setColumnName(column.getColName());
        this.setColumnComment(column.getColComment());
        this.setColumnType(column.getDataType());
        this.setColumnLength(ConversionUtils.getStringToLong(column.getDataLength()));
        this.setColumnScale(ConversionUtils.getStringToLong(column.getDataScale()));
        this.setPkFlag(column.getColKey() ? "1" : "0");
        this.setNullableFlag(column.getNullable() ? "1" : "0");
        this.setDefaultValue(column.getDataDefault());
    }

    public boolean isEqual(DaDiscoveryColumnDO other) {
        if (other == null) {
            return false;
        }

        return (this.columnName != null && this.columnName.equals(other.columnName) || (this.columnName == null && other.columnName == null)) &&
                (this.columnComment != null && this.columnComment.equals(other.columnComment) || (this.columnComment == null && other.columnComment == null)) &&
                (this.columnType != null && this.columnType.equals(other.columnType) || (this.columnType == null && other.columnType == null)) &&
                (this.columnLength != null && this.columnLength.equals(other.columnLength) || (this.columnLength == null && other.columnLength == null)) &&
                (this.columnScale != null && this.columnScale.equals(other.columnScale) || (this.columnScale == null && other.columnScale == null)) &&
                (this.nullableFlag != null && this.nullableFlag.equals(other.nullableFlag) || (this.nullableFlag == null && other.nullableFlag == null)) &&
                (this.pkFlag != null && this.pkFlag.equals(other.pkFlag) || (this.pkFlag == null && other.pkFlag == null)) &&
                (this.defaultValue != null && this.defaultValue.equals(other.defaultValue) || (this.defaultValue == null && other.defaultValue == null));
    }
}
