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
 * 数据发现字段 DO 对象 DA_DISCOVERY_COLUMN
 *
 * @author qdata
 * @date 2025-02-11
 */
@Data
@TableName(value = "DA_DISCOVERY_COLUMN")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
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

    /** 任务id */
    private Long taskId;

    /** 数据发现库表id */
    private Long tableId;

    /** 字段名称/英文名称 */
    private String columnName;

    /** 字段注释/中文名称 */
    private String columnComment;

    /** 数据类型 */
    private String columnType;

    /** 长度 */
    private Long columnLength;

    /** 小数位 */
    private Long columnScale;

    /** 是否必填 */
    private String nullableFlag;

    /** 是否主键 */
    private String pkFlag;

    /** 默认值 */
    private String defaultValue;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
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
