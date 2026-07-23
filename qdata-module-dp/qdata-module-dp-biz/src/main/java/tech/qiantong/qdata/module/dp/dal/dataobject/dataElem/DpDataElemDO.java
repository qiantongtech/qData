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

package tech.qiantong.qdata.module.dp.dal.dataobject.dataElem;

import java.util.List;
import java.util.Set;

import lombok.*;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * Data Element DO - DP_DATA_ELEM
 *
 * @author qdata
 * @date 2025-01-21
 */
@Data
@TableName(value = "DP_DATA_ELEM")
// Used for auto-increment primary keys in Oracle, PostgreSQL, Kingbase, DB2, H2 databases. Can be omitted for MySQL and similar databases.
// @KeySequence("DP_DATA_ELEM_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DpDataElemDO extends BaseEntity {
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
     * Type
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

    /** Name */
    @TableField(exist = false)
    private String documentName;

    /** Name */
    @TableField(exist = false)
    private String documentCode;

    /** Document standard type field */
    @TableField(exist = false)
    private String documentType;
}
