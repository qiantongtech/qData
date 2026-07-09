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

package tech.qiantong.qdata.module.dpp.dal.dataobject.etl;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * Data Integration Task-Extension Data DO - DPP_ETL_TASK_EXT
 *
 * @author qdata
 * @date 2025-04-16
 */
@Data
@TableName(value = "DPP_ETL_TASK_EXT")
// Used for auto-increment primary keys in Oracle, PostgreSQL, Kingbase, DB2, H2 databases. Can be omitted for MySQL and similar databases.
// @KeySequence("DPP_ETL_TASK_EXT_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DppEtlTaskExtDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** Data integration task ID */
    private Long taskId;

    /**
     * Data integration task code
     */
    private String etlTaskCode;
    /**
     * Data integration task version
     */
    private Integer etlTaskVersion;

    /** Data integration node ID */
    private Long etlNodeId;

    /** Data integration node name */
    private String etlNodeName;

    /** Data integration node code */
    private String etlNodeCode;

    /** Data integration node version */
    private Integer etlNodeVersion;

    /** Data integration node relation ID */
    private Long etlRelationId;

    /** Valid flag */
    private Boolean validFlag;

    /** Delete flag */
    @TableLogic
    private Boolean delFlag;


}
