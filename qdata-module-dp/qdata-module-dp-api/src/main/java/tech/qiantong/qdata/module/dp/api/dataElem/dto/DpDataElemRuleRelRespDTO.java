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

package tech.qiantong.qdata.module.dp.api.dataElem.dto;

import lombok.Data;

/**
 * Data Element Rule Relation DTO - DP_DATA_ELEM_RULE_REL
 *
 * @author qdata
 * @date 2025-01-21
 */
@Data
public class DpDataElemRuleRelRespDTO {

    private static final long serialVersionUID = 1L;


    /**
     * ID
     */
    private Long id;

    private String name;

    /**
     * Status; 0: Offline, 1: Online
     */
    private Integer status;

    /**
     * Data Element ID
     */
    private Long dataElemId;

    /**
     * Rule Type 1: Audit Rule 2: Clean Rule
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
     * Rule Config
     */
    private String rule;

    /**
     * @see tech.qiantong.qdata.module.att.dal.dataobject.rule.AttAuditRuleDO#strategyKey
     */
    private String ruleType;

    /**
     * Valid Flag
     */
    private Boolean validFlag;

    /**
     * Delete Flag
     */
    private Boolean delFlag;

}
