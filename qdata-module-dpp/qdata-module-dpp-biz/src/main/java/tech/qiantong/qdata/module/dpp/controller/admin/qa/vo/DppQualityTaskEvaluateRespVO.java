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

package tech.qiantong.qdata.module.dpp.controller.admin.qa.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据质量任务-评测规则 Response VO 对象 DPP_QUALITY_TASK_EVALUATE
 *
 * @author Chaos
 * @date 2025-07-21
 */
@Schema(description = "数据质量任务-评测规则 Response VO")
@Data
public class DppQualityTaskEvaluateRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "数据质量任务ID")
    @Schema(description = "数据质量任务ID", example = "")
    private Long taskId;

    @Excel(name = "评测名称")
    @Schema(description = "评测名称", example = "")
    private String name;

    @Excel(name = "稽查规则编号")
    @Schema(description = "稽查规则编号", example = "")
    private String ruleCode;

    @Excel(name = "稽查规则名称")
    @Schema(description = "稽查规则名称", example = "")
    private String ruleName;

    @Excel(name = "告警等级")
    @Schema(description = "告警等级", example = "")
    private String warningLevel;

    @Excel(name = "质量维度")
    @Schema(description = "质量维度", example = "")
    private String dimensionType;

    @Excel(name = "规则类型")
    @Schema(description = "规则类型", example = "")
    private String ruleType;

    @Excel(name = "状态")
    @Schema(description = "状态", example = "")
    private String status;

    @Excel(name = "规则描述")
    @Schema(description = "规则描述", example = "")
    private String ruleDescription;

    @Excel(name = "错误描述")
    @Schema(description = "错误描述", example = "")
    private String errDescription;

    @Excel(name = "修复建议")
    @Schema(description = "修复建议", example = "")
    private String suggestion;

    @Excel(name = "where条件")
    @Schema(description = "where条件", example = "")
    private String whereClause;

    @Excel(name = "评测对象ID")
    @Schema(description = "评测对象ID", example = "")
    private Long objId;

    @Excel(name = "稽查对象名称")
    @Schema(description = "稽查对象名称", example = "")
    private String objName;

    @Excel(name = "表名称")
    @Schema(description = "表名称", example = "")
    private String tableName;

    @Excel(name = "检查字段，多个时逗号隔开")
    @Schema(description = "检查字段，多个时逗号隔开", example = "")
    private String evaColumn;

    @Excel(name = "不同规则的自定义,JSON形式")
    @Schema(description = "不同规则的自定义,JSON形式", example = "")
    private String rule;

    @Excel(name = "是否有效")
    @Schema(description = "是否有效", example = "")
    private Boolean validFlag;

    @Excel(name = "删除标志")
    @Schema(description = "删除标志", example = "")
    private Boolean delFlag;

    @Excel(name = "创建人")
    @Schema(description = "创建人", example = "")
    private String createBy;

    @Excel(name = "创建人id")
    @Schema(description = "创建人id", example = "")
    private Long creatorId;

    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "")
    private Date createTime;

    @Excel(name = "更新人")
    @Schema(description = "更新人", example = "")
    private String updateBy;

    @Excel(name = "更新人id")
    @Schema(description = "更新人id", example = "")
    private Long updaterId;

    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", example = "")
    private Date updateTime;

}
