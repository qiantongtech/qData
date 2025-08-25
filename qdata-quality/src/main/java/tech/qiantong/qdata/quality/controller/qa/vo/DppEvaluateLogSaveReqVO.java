package tech.qiantong.qdata.quality.controller.qa.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.qiantong.qdata.common.core.domain.BaseEntity;
import tech.qiantong.qdata.common.utils.JSONUtils;
import tech.qiantong.qdata.quality.dal.dataobject.quality.QualityRuleEntity;

import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Map;

/**
 * 评测规则结果 创建/修改 Request VO DPP_EVALUATE_LOG
 *
 * @author qdata
 * @date 2025-07-21
 */
@Schema(description = "评测规则结果 Response VO")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DppEvaluateLogSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "表名称", example = "")
    @Size(max = 256, message = "表名称长度不能超过256个字符")
    private String tableName;

    @Schema(description = "字段名", example = "")
    @Size(max = 256, message = "字段名长度不能超过256个字符")
    private String columnName;

    @Schema(description = "稽查规则编号", example = "")
    @Size(max = 256, message = "稽查规则编号长度不能超过256个字符")
    private String ruleCode;

    @Schema(description = "稽查规则名称", example = "")
    @Size(max = 256, message = "稽查规则名称长度不能超过256个字符")
    private String ruleName;

    @Schema(description = "质量维度", example = "")
    @Size(max = 256, message = "质量维度长度不能超过256个字符")
    private String dimensionType;

    @Schema(description = "规则描述", example = "")
    @Size(max = 256, message = "规则描述长度不能超过256个字符")
    private String ruleDescription;

    @Schema(description = "数据质量记录id", example = "")
    @Size(max = 256, message = "数据质量记录id长度不能超过256个字符")
    private String taskLogId;

    @Schema(description = "评测id", example = "")
    @Size(max = 256, message = "评测id长度不能超过256个字符")
    private String evaluateId;

    @Schema(description = "总数", example = "")
    private Long total;

    @Schema(description = "问题总数", example = "")
    private Long problemTotal;

    @Schema(description = "核查时间", example = "")
    private Date checkDate;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;
    private String rule;


    public DppEvaluateLogSaveReqVO(QualityRuleEntity rule) {
        this.tableName = rule.getTableName();
        this.columnName = rule.getRuleColumn();
        this.ruleCode = rule.getRuleCode();
        this.ruleName = rule.getRuleName();
        this.dimensionType = rule.getDimensionType();
        this.ruleDescription = rule.getRuleDescription();
        this.taskLogId = rule.getTaskLogId() == null ? null : String.valueOf(rule.getTaskLogId());
        this.evaluateId = rule.getEvaluateId() == null ? null : String.valueOf(rule.getEvaluateId());
        this.checkDate = new Date(); // 默认当前时间

        Map<String, Object> config = rule.getConfig();
//        config.put("","");

        this.rule = JSONUtils.toJson(config);
    }
}
