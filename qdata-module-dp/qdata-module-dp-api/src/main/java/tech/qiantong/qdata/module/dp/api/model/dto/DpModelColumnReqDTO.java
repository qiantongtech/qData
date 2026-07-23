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

package tech.qiantong.qdata.module.dp.api.model.dto;

import lombok.Data;
import tech.qiantong.qdata.common.database.core.DbColumn;

/**
 * Logical Model Column DTO - DP_MODEL_COLUMN
 *
 * @author qdata
 * @date 2025-01-21
 */
@Data
public class DpModelColumnReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Logical Model Table ID */
    private Long modelId;

    /** English Name */
    private String engName;

    /** Chinese Name */
    private String cnName;

    /** Data Type */
    private String columnType;

    /** Column Length */
    private Long columnLength;

    /** Decimal Scale */
    private Long columnScale;

    /** Default Value */
    private String defaultValue;

    /** Primary Key Flag */
    private String pkFlag;

    /** Nullable Flag */
    private String nullableFlag;

    /** Sort Order */
    private Long sortOrder;

    /** Authority Department */
    private String authorityDept;

    /** Data Element ID */
    private Long dataElemId;

    /** Valid Flag */
    private Boolean validFlag;

    /** Delete Flag */
    private Boolean delFlag;

    // Constructor
    public DpModelColumnReqDTO(DbColumn column) {
        if (column != null) {
            this.columnLength = (column.getDataLength() != null) ? Long.valueOf(column.getDataLength()) : null;
            this.engName = column.getColName();
            this.cnName = column.getColComment();
            this.columnType = column.getDataType();
            this.columnScale = (column.getDataScale() != null) ? Long.valueOf(column.getDataScale()) : null;
            this.defaultValue = column.getDataDefault();
            this.pkFlag = column.getColKey() ? "1" : "0";
            this.nullableFlag = column.getNullable() ? "0" : "1";
            this.sortOrder = (column.getColPosition() != null) ? Long.valueOf(column.getColPosition()) : null;
        }
    }
}
