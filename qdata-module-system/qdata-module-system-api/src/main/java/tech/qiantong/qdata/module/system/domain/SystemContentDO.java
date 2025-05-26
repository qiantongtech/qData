package tech.qiantong.qdata.module.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * 系统配置 DO 对象 system_content
 *
 * @author qdata
 * @date 2024-12-31
 */
@Data
@TableName(value = "system_content")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("system_content_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SystemContentDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 系统名称 */
    private String sysName;

    /** logo */
    private String loginLogo;

    private String logo;

    /** 轮播图 */
    private String carouselImage;

    /** 联系电话 */
    private String contactNumber;

    /** 电子邮箱 */
    private String email;

    /** 版权方 */
    private String copyright;

    /** 备案号 */
    private String recordNumber;

    /** 删除标记 */
    @TableLogic
    private Boolean delFlag;

    /** 状态 */
    private Integer status;

    /** 备注 */
    private String remark;


}
