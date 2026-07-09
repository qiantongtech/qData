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
 * Data Integration Task Node Relation DO - DPP_ETL_TASK_NODE_REL
 *
 * @author qdata
 * @date 2025-02-13
 */
@Data
@TableName(value = "DPP_ETL_TASK_NODE_REL")
// Used for auto-increment primary keys in Oracle, PostgreSQL, Kingbase, DB2, H2 databases. Can be omitted for MySQL and similar databases.
// @KeySequence("DPP_ETL_TASK_NODE_REL_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DppEtlTaskNodeRelDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** Project ID */
    private Long projectId;

    /** Project code */
    private String projectCode;

    /** Task ID */
    private Long taskId;

    /** Task code */
    private String taskCode;

    /** Task version */
    private Long taskVersion;

    /** Pre-node ID */
    private Long preNodeId;

    /** Pre-node code */
    private String preNodeCode;

    /** Pre-node version */
    private Long preNodeVersion;

    /** Post-node ID */
    private Long postNodeId;

    /** Post-node code */
    private String postNodeCode;

    /** Post-node version */
    private Long postNodeVersion;

    /** Valid flag */
    private Boolean validFlag;

    /** Delete flag */
    @TableLogic
    private Boolean delFlag;


}
