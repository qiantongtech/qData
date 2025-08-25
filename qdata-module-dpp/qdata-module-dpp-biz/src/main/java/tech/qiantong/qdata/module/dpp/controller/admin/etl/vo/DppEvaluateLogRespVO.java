package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 评测规则结果 Response VO 对象 DPP_EVALUATE_LOG
 *
 * @author qdata
 * @date 2025-07-21
 */
@Schema(description = "评测规则结果 Response VO")
@Data
public class DppEvaluateLogRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "表名称")
    @Schema(description = "表名称", example = "")
    private String tableName;
    private Long datasourceId;

    @Excel(name = "字段名")
    @Schema(description = "字段名", example = "")
    private String columnName;

    @Excel(name = "稽查规则编号")
    @Schema(description = "稽查规则编号", example = "")
    private String ruleCode;

    @Excel(name = "稽查规则名称")
    @Schema(description = "稽查规则名称", example = "")
    private String ruleName;

    @Excel(name = "质量维度")
    @Schema(description = "质量维度", example = "")
    private String dimensionType;

    @Excel(name = "规则描述")
    @Schema(description = "规则描述", example = "")
    private String ruleDescription;

    @Excel(name = "数据质量记录id")
    @Schema(description = "数据质量记录id", example = "")
    private String taskLogId;

    @Excel(name = "评测id")
    @Schema(description = "评测id", example = "")
    private String evaluateId;

    @Excel(name = "总数")
    @Schema(description = "总数", example = "")
    private Long total;

    @Excel(name = "问题总数")
    @Schema(description = "问题总数", example = "")
    private Long problemTotal;

    @Excel(name = "核查时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "核查时间", example = "")
    private Date checkDate;

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

    @Excel(name = "备注")
    @Schema(description = "备注", example = "")
    private String remark;

    @Excel(name = "不同规则的自定义,JSON形式")
    @Schema(description = "不同规则的自定义,JSON形式", example = "")
    private String rule;
    // 占比
    private BigDecimal proportion;

    /** 数据源名称 */
    private String datasourceType;
    private String datasourceName;


}
