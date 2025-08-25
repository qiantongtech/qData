package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;
import tech.qiantong.qdata.common.core.page.PageParam;

import java.util.Date;

/**
 * 评测规则结果 Request VO 对象 DPP_EVALUATE_LOG
 *
 * @author qdata
 * @date 2025-07-21
 */
@Schema(description = "评测规则结果 Request VO")
@Data
public class DppEvaluateLogPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "表名称", example = "")
    private String tableName;

    @Schema(description = "字段名", example = "")
    private String columnName;

    @Schema(description = "稽查规则编号", example = "")
    private String ruleCode;

    @Schema(description = "稽查规则名称", example = "")
    private String ruleName;

    @Schema(description = "质量维度", example = "")
    private String dimensionType;

    @Schema(description = "规则描述", example = "")
    private String ruleDescription;

    @Schema(description = "数据质量记录id", example = "")
    private String taskLogId;

    @Schema(description = "评测id", example = "")
    private String evaluateId;

    @Schema(description = "总数", example = "")
    private Long total;

    @Schema(description = "问题总数", example = "")
    private Long problemTotal;

    @Schema(description = "核查时间", example = "")
    private Date checkDate;

    @Excel(name = "不同规则的自定义,JSON形式")
    private String rule;


}
