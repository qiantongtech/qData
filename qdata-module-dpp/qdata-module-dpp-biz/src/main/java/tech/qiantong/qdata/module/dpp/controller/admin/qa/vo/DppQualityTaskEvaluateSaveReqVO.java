package tech.qiantong.qdata.module.dpp.controller.admin.qa.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Size;

import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * 数据质量任务-评测规则 创建/修改 Request VO DPP_QUALITY_TASK_EVALUATE
 *
 * @author Chaos
 * @date 2025-07-21
 */
@Schema(description = "数据质量任务-评测规则 Response VO")
@Data
public class DppQualityTaskEvaluateSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
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
    @Size(max = 256, message = "告警等级长度不能超过256个字符")
    private String warningLevel;

    @Schema(description = "质量维度", example = "")
    private String dimensionType;

    @Schema(description = "规则类型", example = "")
    @Size(max = 32, message = "规则类型长度不能超过32个字符")
    private String ruleType;

    @Schema(description = "状态", example = "")
    @Size(max = 256, message = "状态长度不能超过256个字符")
    private String status;

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

    private Long datasourceId;

    // 检测信息
    private String title;



    /**
     * 分页参数（可选）
     */
    private Integer pageNum;

    private Integer pageSize;
}
