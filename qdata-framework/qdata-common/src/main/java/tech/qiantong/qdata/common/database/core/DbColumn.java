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

package tech.qiantong.qdata.common.database.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DbColumn {

    /**
     * Table name
     */
    private String tableName;

    /**
     * List
     */
    private String colName;

    /**
     * Data type
     */
    private String dataType;

    /**
     * Data length
     */
    private String dataLength;

    /**
     * Data accuracy
     */
    private String dataPrecision;

    /**
     * Data decimal places
     */
    private String dataScale;

    /**
     * Whether the primary key is true is the primary key false is not the primary key
     */
    private Boolean colKey;

    /**
     * Whether empty is allowed true, empty is allowed, false is not allowed to be empty
     */
    private Boolean nullable;

    /**
     * Column number
     */
    private Integer colPosition;

    /**
     * Column default value
     */
    private String dataDefault;

    /**
     * Column comments
     */
    private String colComment;
}
