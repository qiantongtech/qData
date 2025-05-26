package tech.qiantong.qdata.module.att.dal.dataobject.rule;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * 清洗规则 DO 对象 ATT_CLEAN_RULE
 *
 * @author qdata
 * @date 2025-01-20
 */
@Data
@TableName(value = "ATT_CLEAN_RULE")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("ATT_CLEAN_RULE_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AttCleanRuleDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** 规则ID */
    private Long id;

    /** 规则名称 */
    private String name;

    /** 规则类型 */
    private String type;

    /** 规则级别 */
    private String level;

    /** 规则描述 */
    private String description;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    @TableLogic
    private Boolean delFlag;

    /**
     * 数据元规则关联id
     */
    @TableField(exist = false)
    private Long ruleRelId;

    /**
     * 规则配置
     */
    @TableField(exist = false)
    private String ruleConfig;
}
