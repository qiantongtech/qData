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

package tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.NotNull;

/**
 * 数据元数据规则关联信息 创建/修改 Request VO DP_DATA_ELEM_RULE_REL
 *
 * @author qdata
 * @date 2025-01-21
 */
@Schema(description = "数据元数据规则关联信息 Response VO")
@Data
public class DpDataElemRuleRelSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @NotNull(message = "name null")
    private String name;

    /**
     * 状态;0下线1上线
     */
    private String status;

    @Schema(description = "数据元id", example = "")
    @NotNull(message = "dataElemId null")
    private Long dataElemId;

    @Schema(description = "规则类型", example = "1:稽核规则 2:清洗规则")
    @NotNull(message = "type null")
    private String type;

    @Schema(description = "规则id", example = "")
    @NotNull(message = "ruleId null")
    private Long ruleId;

    @NotNull(message = "ruleCode null")
    private String ruleCode;

    @NotNull(message = "ruleName null")
    private String ruleName;

    private String dimensionType;

    @Schema(description = "规则描述")
    private String ruleDescription;

    @Schema(description = "错误描述")
    private String errDescription;

    @Schema(description = "修复建议")
    private String suggestion;

    @Schema(description = "where条件")
    private String whereClause;

    @Schema(description = "规则配置", example = "")
    @NotNull(message = "rule null")
    private String rule;

    /**
     * @see tech.qiantong.qdata.module.att.dal.dataobject.rule.AttAuditRuleDO#strategyKey
     */
    private String ruleType;

}
