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
 * Business License: https://docs.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.module.dg.dal.dataobject.standard;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import java.util.Set;

/**
 * Data Element DO entity DG_DATA_ELEM
 *
 * @author qdata
 * @date 2025-01-21
 */
@Data
@TableName(value = "DG_DATA_ELEM")
// Used for auto-increment primary keys in Oracle, PostgreSQL, Kingbase, DB2, H2 databases. Can be omitted for MySQL and similar databases.
// @KeySequence("DG_DATA_ELEM_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DgDataElemDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * Code
     */
    private String code;

    /**
     * Name
     */
    private String name;

    /**
     * English Name
     */
    private String engName;

    /**
     * Category Code
     */
    private String catCode;

    @TableField(exist = false)
    private String catName;

    /**
     * Type; 1: Data Element 2: Code Table
     */
    private String type;

    /**
     * Person in Charge
     */
    private String personCharge;

    /**
     * Person in Charge Name
     */
    @TableField(exist = false)
    private String personChargeName;

    /**
     * Contact Number
     */
    private String contactNumber;

    /**
     * Column Type
     */
    private String columnType;

    /**
     * Status
     */
    private String status;

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
    private Set<Long> columnId;

    private Long documentId;

    /**
     * Name
     */
    @TableField(exist = false)
    private String documentName;

    /**
     * Name
     */
    @TableField(exist = false)
    private String documentCode;

    /**
     * File Standard Type Field
     */
    @TableField(exist = false)
    private String documentType;
}
