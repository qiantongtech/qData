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

package tech.qiantong.qdata.module.dpp.dal.dataobject.qa;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * Data Quality Task-Evaluate Rule DO - DPP_QUALITY_TASK_EVALUATE
 *
 * @author Chaos
 * @date 2025-07-21
 */
@Data
@TableName(value = "DPP_QUALITY_TASK_EVALUATE")
// Used for auto-increment primary keys in Oracle, PostgreSQL, Kingbase, DB2, H2 databases. Can be omitted for MySQL and similar databases.
// @KeySequence("DPP_QUALITY_TASK_EVALUATE_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DppQualityTaskEvaluateDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** Data quality task ID */
    private Long taskId;

    /** Evaluate name */
    private String name;

    /** Audit rule code */
    private String ruleCode;

    /** Audit rule name */
    private String ruleName;

    /** Warning level */
    private String warningLevel;

    /** Status */
    private String status;

    /** Quality dimension */
    private String dimensionType;

     /** Rule type */
    private String ruleType;

    /** Rule description */
    private String ruleDescription;

    /** Error description */
    private String errDescription;

    /** Suggestion */
    private String suggestion;

    /** Where clause */
    private String whereClause;

    /** Evaluate object ID */
    private Long objId;

    /** Audit object name */
    private String objName;

    /** Table name */
    private String tableName;

    /** Check columns, comma separated for multiple */
    private String evaColumn;

    /** Custom per rule, in JSON format */
    private String rule;

    /** Valid flag */
    private Boolean validFlag;

    /** Delete flag */
    @TableLogic
    private Boolean delFlag;


}
