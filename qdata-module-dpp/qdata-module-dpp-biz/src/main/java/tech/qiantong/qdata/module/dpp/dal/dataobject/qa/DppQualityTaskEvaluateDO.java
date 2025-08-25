package tech.qiantong.qdata.module.dpp.dal.dataobject.qa;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * 数据质量任务-评测规则 DO 对象 DPP_QUALITY_TASK_EVALUATE
 *
 * @author Chaos
 * @date 2025-07-21
 */
@Data
@TableName(value = "DPP_QUALITY_TASK_EVALUATE")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("DPP_QUALITY_TASK_EVALUATE_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DppQualityTaskEvaluateDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 数据质量任务ID */
    private Long taskId;

    /** 评测名称 */
    private String name;

    /** 稽查规则编号 */
    private String ruleCode;

    /** 稽查规则名称 */
    private String ruleName;

    /** 告警等级 */
    private String warningLevel;

    /** 状态 */
    private String status;

    /** 质量维度*/
    private String dimensionType;

     /** 规则类型*/
    private String ruleType;

    /** 规则描述 */
    private String ruleDescription;

    /** 错误描述 */
    private String errDescription;

    /** 修复建议 */
    private String suggestion;

    /** where条件 */
    private String whereClause;

    /** 评测对象ID */
    private Long objId;

    /** 稽查对象名称 */
    private String objName;

    /** 表名称 */
    private String tableName;

    /** 检查字段，多个时逗号隔开 */
    private String evaColumn;

    /** 不同规则的自定义,JSON形式 */
    private String rule;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    @TableLogic
    private Boolean delFlag;


}
