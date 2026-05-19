package tech.qiantong.qdata.module.dg.dal.dataobject.sensitiveLevel;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * 敏感等级 DO 对象 DG_SENSITIVE_LEVEL
 *
 * @author Chaos
 * @date 2025-01-21
 */
@Data
@TableName(value = "DG_SENSITIVE_LEVEL")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("DG_SENSITIVE_LEVEL_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DgSensitiveLevelDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 敏感级别
     */
    private String sensitiveLevel;

    /**
     * 敏感规则
     */
    private String sensitiveRule;

    /**
     * 起始字符位置
     */
    private Long startCharLoc;

    /**
     * 截止字符位置
     */
    private Long endCharLoc;

    /**
     * 遮盖字符
     */
    private String maskCharacter;

    /**
     * 上下线标识
     * 0：下线，1：上线
     */
    private String onlineFlag;

    /**
     * 描述
     */
    private String description;

    /**
     * 是否有效
     */
    private Boolean validFlag;

    /**
     * 删除标志
     */
    @TableLogic
    private Boolean delFlag;

}
