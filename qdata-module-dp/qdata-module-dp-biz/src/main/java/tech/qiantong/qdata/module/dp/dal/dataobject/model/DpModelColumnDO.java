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

package tech.qiantong.qdata.module.dp.dal.dataobject.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * Logical Model Column DO - DP_MODEL_COLUMN
 *
 * @author qdata
 * @date 2025-01-21
 */
@Data
@TableName(value = "DP_MODEL_COLUMN")
// Used for auto-increment primary keys in Oracle, PostgreSQL, Kingbase, DB2, H2 databases. Can be omitted for MySQL and similar databases.
// @KeySequence("DP_MODEL_COLUMN_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DpModelColumnDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** Logical model table ID */
    private Long modelId;

    /** English name */
    private String engName;

    /** Chinese name */
    private String cnName;

    /** Data type */
    private String columnType;

    /** Attribute length */
    private Long columnLength;

    /** Decimal length */
    private Long columnScale;

    /** Default value */
    private String defaultValue;

    /** Whether the column is a primary key */
    private String pkFlag;

    /** Whether the column is required */
    private String nullableFlag;

    /** Sort order */
    private Long sortOrder;

    /** Authoritative department */
    private Long authorityDept;

    /** Data element ID */
    private Long dataElemId;

    /** Data element name */
    @TableField(exist = false)
    private String dataElemName;

    /** Whether the record is active */
    private Boolean validFlag;

    /** Deletion flag */
    @TableLogic
    private Boolean delFlag;


}
