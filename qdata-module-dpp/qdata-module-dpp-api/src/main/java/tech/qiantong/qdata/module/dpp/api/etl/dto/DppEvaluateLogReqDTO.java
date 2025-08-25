package tech.qiantong.qdata.module.dpp.api.etl.dto;

import lombok.Data;

import java.util.Date;

/**
 * 评测规则结果 DTO 对象 DPP_EVALUATE_LOG
 *
 * @author qdata
 * @date 2025-07-21
 */
@Data
public class DppEvaluateLogReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 表名称 */
    private String tableName;

    /** 字段名 */
    private String columnName;

    /** 稽查规则编号 */
    private String ruleCode;

    /** 稽查规则名称 */
    private String ruleName;

    /** 质量维度 */
    private String dimensionType;

    /** 规则描述 */
    private String ruleDescription;

    /** 数据质量记录id */
    private String taskLogId;

    /** 评测id */
    private String evaluateId;

    /** 总数 */
    private Long total;

    /** 问题总数 */
    private Long problemTotal;

    /** 核查时间 */
    private Date checkDate;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
