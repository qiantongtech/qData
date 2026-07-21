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

package tech.qiantong.qdata.module.ds.api.api.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class FieldParam implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * Column name
     */
    private String columnName;

    /**
     * Data type
     */
    private String dataType;

    /**
     * Data length
     */
    private Long dataLength;

    /**
     * Data precision
     */
    private Long dataPrecision;

    /**
     * Decimal places
     */
    private Long dataScale;

    /**
     * Whether the column is a primary key
     */
    private String columnKey;

    /**
     * Whether null values are allowed
     */
    private String columnNullable;

    /**
     * Column ordinal
     */
    private Long columnPosition;

    /**
     * Column default value
     */
    private String dataDefault;

    /**
     * Column comment
     */
    private String columnComment;

    /**
     * Used as a request parameter
     */
    private String reqable;

    /**
     * Used as a response parameter
     */
    private String resable;
}
