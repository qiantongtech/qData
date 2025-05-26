package tech.qiantong.qdata.module.dp.dal.dataobject.dataElem;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * 数据元数据规则关联信息 DO 对象 DP_DATA_ELEM_RULE_REL
 *
 * @author qdata
 * @date 2025-01-21
 */
@Data
@TableName(value = "DP_DATA_ELEM_RULE_REL")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("DP_DATA_ELEM_RULE_REL_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DpDataElemRuleRelDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 数据元id
     */
    private String dataElemId;

    /**
     * 规则类型
     */
    private String ruleType;

    /**
     * 规则id
     */
    private String ruleId;

    /**
     * 规则配置
     */
    private String ruleConfig;

    /**
     * 是否有效
     */
    private Boolean validFlag;

    /**
     * 删除标志
     */
    @TableLogic
    private Boolean delFlag;

    /**
     * 规则名称
     */
    @TableField(exist = false)
    private String name;

    /**
     * 规则类别
     */
    @TableField(exist = false)
    private String type;

    /**
     * 规则级别
     */
    @TableField(exist = false)
    private String level;

    /**
     * 质量维度
     */
    @TableField(exist = false)
    private String qualityDim;

    /**
     * 规则描述
     */
    @TableField(exist = false)
    private String description;
}
