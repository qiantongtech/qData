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
 * Materialized Model Record DO - DP_MODEL_MATERIALIZED
 *
 * @author qdata
 * @date 2025-01-21
 */
@Data
@TableName(value = "DP_MODEL_MATERIALIZED")
// Used for auto-increment primary keys in Oracle, PostgreSQL, Kingbase, DB2, H2 databases. Can be omitted for MySQL and similar databases.
// @KeySequence("DP_MODEL_MATERIALIZED_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DpModelMaterializedDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** Model Code */
    private String modelName;

    /** Model Name */
    private String modelAlias;

    /** Model Table ID */
    private Long modelId;

    /** Status
     * 1 Not Created, 2 Creating, 3 Success, 4 Failed, 5 Already Exists.
     *
     * */
    private String status;

    /** Execution Log Message */
    private String message;

    /** Execution SQL Backup */
    private String sqlCommand;

    /** Datasource ID */
    private String datasourceId;

    /** Datasource Type */
    private String datasourceType;

    /** Datasource Name */
    private String datasourceName;

    /** Asset Table ID */
    private Long assetId;

    /** Release Mode 1: Drop and Recreate  2: Incremental Publish */
    private String releaseMode;

    /** Valid Flag */
    private Boolean validFlag;

    /** Delete Flag */
    @TableLogic
    private Boolean delFlag;

    @TableField(exist = false)
    /** Field Count */
    private Long fieldCount;
}
