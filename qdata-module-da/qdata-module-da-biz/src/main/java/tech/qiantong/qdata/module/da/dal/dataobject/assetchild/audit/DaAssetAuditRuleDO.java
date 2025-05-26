package tech.qiantong.qdata.module.da.dal.dataobject.assetchild.audit;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 数据资产质量结果记录 DO 对象 DA_ASSET_AUDIT_RULE
 *
 * @author qdata
 * @date 2025-05-09
 */
@Data
@TableName(value = "DA_ASSET_AUDIT_RULE")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("DA_ASSET_AUDIT_RULE_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DaAssetAuditRuleDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 资产ID */
    private Long assetId;

    /** 表名称 */
    private String tableName;

    /** 字段名称/英文名称 */
    private String columnName;

    /** 字段注释/中文名称 */
    private String columnComment;

    /** 规则名称 */
    private String ruleName;

    /** 质量维度 */
    private String qualityDim;

    /** 规则类型 */
    private String ruleType;

    /** 规则级别 */
    private String ruleLevel;

    /** 规则描述 */
    private String ruleDescription;

    /** 规则配置 */
    private String ruleConfig;

    /** 校验总数 */
    private Long totalCount;

    /** 问题数 */
    private Long issueCount;

    /** 稽查时间 */
    private Date auditTime;

    /** 稽查批次号 */
    private String batchNo;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    @TableLogic
    private Boolean delFlag;


}
