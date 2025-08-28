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
