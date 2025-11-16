/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.module.dp.api.dataElem.dto;

import lombok.Data;

/**
 * 数据元数据规则关联信息 DTO 对象 DP_DATA_ELEM_RULE_REL
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
     * 状态;0下线1上线
     */
    private Integer status;

    /**
     * 数据元id
     */
    private Long dataElemId;

    /**
     * 规则类型 1:稽核规则 2:清洗规则
     */
    private String type;

    /**
     * 规则id
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
     * 规则配置
     */
    private String rule;

    /**
     * @see tech.qiantong.qdata.module.att.dal.dataobject.rule.AttAuditRuleDO#strategyKey
     */
    private String ruleType;

    /**
     * 是否有效
     */
    private Boolean validFlag;

    /**
     * 删除标志
     */
    private Boolean delFlag;

}
