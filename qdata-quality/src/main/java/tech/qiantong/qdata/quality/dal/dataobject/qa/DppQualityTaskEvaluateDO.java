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

package tech.qiantong.qdata.quality.dal.dataobject.qa;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * Data Quality Task-Evaluation Rule DO Object DPP_QUALITY_TASK_EVALUATE
 *
 * @author Chaos
 * @date 2025-07-21
 */
@Data
@TableName(value = "DPP_QUALITY_TASK_EVALUATE")
// Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you do not need to write it.
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

    /** Evaluation name */
    private String name;

    /** Audit rule number */
    private String ruleCode;

    /** Audit rule name */
    private String ruleName;

    /** Alarm level */
    private String warningLevel;

    /** status */
    private String status;

    /** Quality Dimension*/
    private String dimensionType;

    /** Rule description */
    private String ruleDescription;

    /** Error description */
    private String errDescription;

    /** Repair suggestions */
    private String suggestion;

    private String ruleType;

    /** where conditions */
    private String whereClause;

    /** Evaluation object ID */
    private Long objId;

    /** Audit object name */
    private String objName;

    /** table name */
    private String tableName;

    /** Check fields, separate them with commas */
    private String evaColumn;

    /** Customization of different rules, JSON format */
    private String rule;

    /** Is it valid */
    private Boolean validFlag;

    /** Delete flag */
    @TableLogic
    private Boolean delFlag;


}
