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

package tech.qiantong.qdata.module.da.api.datasource.dto;

import lombok.Data;
import tech.qiantong.qdata.common.database.core.DbColumn;

import java.util.List;

@Data
public class DatasourceCreaTeTableReqDTO {

    /** Datasource Type */
    private String datasourceType;

    /** Datasource Config (JSON String) */
    private String datasourceConfig;

    /** IP */
    private String ip;

    /** Port */
    private Long port;

    /**
     * Database Name
     */
    private String dbname;

    /**
     * Table Name
     */
    private String tableName;

    /**
     * Table Comment
     */
    private String tableComment;

    /**
     * Table Columns
     */
    private List<DbColumn> columnsList;
}
