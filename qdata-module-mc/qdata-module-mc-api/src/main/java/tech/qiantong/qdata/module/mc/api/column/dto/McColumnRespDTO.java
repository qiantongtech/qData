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

package tech.qiantong.qdata.module.mc.api.column.dto;

import lombok.Data;

/**
 * <P>
 * Purpose: metadata field information - log DTO object MC_COLUMN
 * </p>
 *
 * @author: FXB
 * @create: 2026-04-28 14:05
 **/
@Data
public class McColumnRespDTO {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * Field ID
     */
    private Long columnId;

    /**
     * Version
     */
    private Integer version;

    /**
     * Table information ID
     */
    private Long tableId;

    /**
     * Field name
     */
    private String columnName;

    /**
     * Field comment
     */
    private String columnComment;

    /**
     * Field type
     */
    private String columnType;

    /**
     * Data length
     */
    private Integer columnLength;

    /**
     * Data precision
     */
    private Integer columnPrecision;

    /**
     * Data scale
     */
    private Integer columnScale;

    /**
     * Data default value
     */
    private String defaultValue;

    /**
     * Whether it is a primary key; 0: no, 1: yes
     */
    private String pkFlag;

    /**
     * Whether it is a foreign key; 0: no, 1: yes
     */
    private String fkFlag;

    /**
     * Whether it is nullable; 0: no, 1: yes
     */
    private String nullableFlag;
}
