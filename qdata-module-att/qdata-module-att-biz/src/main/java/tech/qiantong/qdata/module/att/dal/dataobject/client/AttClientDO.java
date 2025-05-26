package tech.qiantong.qdata.module.att.dal.dataobject.client;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * 应用管理 DO 对象 ATT_CLIENT
 *
 * @author qdata
 * @date 2025-02-18
 */
@Data
@TableName(value = "ATT_CLIENT")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("ATT_CLIENT_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AttClientDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 应用名称 */
    private String name;

    /** 应用类型 */
    private String type;

    /** 应用秘钥 */
    private String secret;

    /** 主页地址 */
    private String homepageUrl;

    /** 允许授权的url */
    private String allowUrl;

    /** 同步地址 */
    private String syncUrl;

    /** 应用图标 */
    private String logo;

    /** 应用描述 */
    private String description;

    /** 是否公开 */
    private String publicFlag;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    @TableLogic
    private Boolean delFlag;


}
