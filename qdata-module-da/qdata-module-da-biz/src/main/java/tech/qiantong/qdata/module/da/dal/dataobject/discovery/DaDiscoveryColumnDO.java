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
