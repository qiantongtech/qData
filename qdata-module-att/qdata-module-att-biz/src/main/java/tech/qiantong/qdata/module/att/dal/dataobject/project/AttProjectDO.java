package tech.qiantong.qdata.module.att.dal.dataobject.project;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * 项目 DO 对象 ATT_PROJECT
 *
 * @author shu
 * @date 2025-01-20
 */
@Data
@TableName(value = "ATT_PROJECT")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("ATT_PROJECT_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AttProjectDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 项目名称 */
    private String name;

    /** 项目编码 */
    private String code;

    /** 项目管理员id */
    private Long managerId;
    /** 项目管理员 */
    @TableField(exist = false)
    private String nickName;
    /** 项目管理员手机号 */
    @TableField(exist = false)
    private String managerPhone;

    /** 项目描述 */
    private String description;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    @TableLogic
    private Boolean delFlag;


}
