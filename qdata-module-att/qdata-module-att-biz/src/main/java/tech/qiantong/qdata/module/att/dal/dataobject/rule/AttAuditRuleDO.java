package tech.qiantong.qdata.module.att.dal.dataobject.rule;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * 稽查规则 DO 对象 ATT_AUDIT_RULE
 *
 * @author qdata
 * @date 2025-01-20
 */
@Data
@TableName(value = "ATT_AUDIT_RULE")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("ATT_AUDIT_RULE_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AttAuditRuleDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** 规则ID */
    private Long id;

    /** 规则名称 */
    private String name;

    /** 质量维度 */
    private String qualityDim;

    /** 规则类型 */
    private String type;

    /**
     * 规则级别
     * 1：字段级，2：表级
     */
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

    @Schema(description = "规则编码", example = "101")
    private String code;

    @Schema(description = "使用场景", example = "用于身份证号非空检查")
    private String useCase;

    @Schema(description = "示例", example = "字段值不能为空，如：ID=123456")
    private String example;

    @Schema(description = "图标地址", example = "/images/icon.png")
    private String iconPath;

    @Schema(description = "策略标识", example = "NOT_NULL_ID_CHECK")
    private String strategyKey;
}
