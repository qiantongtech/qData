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
 * For brand customization, please contact the official team for authorization.
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
 * 如需定制品牌，请通过官方渠道申请品牌授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.module.dpp.controller.admin.qa.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 数据质量任务-评测规则 Request VO 对象 DPP_QUALITY_TASK_EVALUATE
 *
 * @author Chaos
 * @date 2025-07-21
 */
@Schema(description = "数据质量任务-评测规则 Request VO")
@Data
public class DppQualityTaskEvaluatePageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "数据质量任务ID", example = "")
    private Long taskId;

    @Schema(description = "评测名称", example = "")
    private String name;

    @Schema(description = "稽查规则编号", example = "")
    private String ruleCode;

    @Schema(description = "稽查规则名称", example = "")
    private String ruleName;

    @Schema(description = "告警等级", example = "")
    private String warningLevel;

    @Schema(description = "状态", example = "")
    private String status;

    @Schema(description = "质量维度", example = "")
    private String dimensionType;

    @Schema(description = "规则类型", example = "")
    private String ruleType;

    @Schema(description = "规则描述", example = "")
    private String ruleDescription;

    @Schema(description = "错误描述", example = "")
    private String errDescription;

    @Schema(description = "修复建议", example = "")
    private String suggestion;

    @Schema(description = "where条件", example = "")
    private String whereClause;

    @Schema(description = "评测对象ID", example = "")
    private Long objId;

    @Schema(description = "稽查对象名称", example = "")
    private String objName;

    @Schema(description = "表名称", example = "")
    private String tableName;

    @Schema(description = "检查字段，多个时逗号隔开", example = "")
    private String evaColumn;

    @Schema(description = "不同规则的自定义,JSON形式", example = "")
    private String rule;




}
