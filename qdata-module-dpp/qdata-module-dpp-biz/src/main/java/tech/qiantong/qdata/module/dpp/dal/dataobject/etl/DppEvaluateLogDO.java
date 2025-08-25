package tech.qiantong.qdata.module.dpp.dal.dataobject.etl;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 评测规则结果 DO 对象 DPP_EVALUATE_LOG
 *
 * @author qdata
 * @date 2025-07-21
 */
@Data
@TableName(value = "DPP_EVALUATE_LOG")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("DPP_EVALUATE_LOG_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DppEvaluateLogDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
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

    /** 不同规则的自定义,JSON形式 */
    private String rule;

    /** 总数 */
    private Long total;

    /** 问题总数 */
    private Long problemTotal;

    /** 核查时间 */
    private Date checkDate;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    @TableLogic
    private Boolean delFlag;


}
