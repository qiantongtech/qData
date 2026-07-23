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

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * Data Element Rule Relationship DO - DP_DATA_ELEM_RULE_REL
 *
 * @author qdata
 * @date 2025-01-21
 */
@Data
@TableName(value = "DP_DATA_ELEM_RULE_REL")
// Used for auto-increment primary keys in Oracle, PostgreSQL, Kingbase, DB2, H2 databases. Can be omitted for MySQL and similar databases.
// @KeySequence("DP_DATA_ELEM_RULE_REL_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DpDataElemRuleRelDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    /**
     * Status; 0 offline, 1 online
     */
    private String status;

    /**
     * Data Element ID
     */
    private Long dataElemId;

    /**
     * Rule Type 1: Audit Rule 2: Cleaning Rule
     */
    private String type;

    /**
     * Rule ID
     *
     * @see tech.qiantong.qdata.module.att.dal.dataobject.rule.AttCleanRuleDO#id
     * @see tech.qiantong.qdata.module.att.dal.dataobject.rule.AttAuditRuleDO#id
     */
    private Long ruleId;

    /**
     * @see tech.qiantong.qdata.module.att.dal.dataobject.rule.AttCleanRuleDO#code
     * @see tech.qiantong.qdata.module.att.dal.dataobject.rule.AttAuditRuleDO#code
     */
    private String ruleCode;

    /**
     * @see tech.qiantong.qdata.module.att.dal.dataobject.rule.AttCleanRuleDO#name
     * @see tech.qiantong.qdata.module.att.dal.dataobject.rule.AttAuditRuleDO#name
     */
    private String ruleName;

    /**
     * @see tech.qiantong.qdata.module.att.dal.dataobject.rule.AttAuditRuleDO#qualityDim
     */
    private String dimensionType;

    private String ruleDescription;
    private String errDescription;
    private String suggestion;
    private String whereClause;

    /**
     * Rule Configuration
     */
    private String rule;

    /**
     * @see tech.qiantong.qdata.module.att.dal.dataobject.rule.AttAuditRuleDO#strategyKey
     *
     */
    private String ruleType;

    /**
     * Valid Flag
     */
    private Boolean validFlag;

    /**
     * Delete Flag
     */
    @TableLogic
    private Boolean delFlag;

}
