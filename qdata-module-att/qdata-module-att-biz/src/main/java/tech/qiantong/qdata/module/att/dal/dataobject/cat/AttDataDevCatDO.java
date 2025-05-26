package tech.qiantong.qdata.module.att.dal.dataobject.cat;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * 数据开发类目管理 DO 对象 ATT_DATA_DEV_CAT
 *
 * @author qdata
 * @date 2025-03-11
 */
@Data
@TableName(value = "ATT_DATA_DEV_CAT")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("ATT_DATA_DEV_CAT_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AttDataDevCatDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 项目id */
    private Long projectId;

    /** 项目code */
    private String projectCode;

    /** 类别名称 */
    private String name;

    /** 关联上级ID */
    private Long parentId;

    /** 类别排序 */
    private Long sortOrder;

    /** 描述 */
    private String description;

    /** 层级编码 */
    private String code;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    @TableLogic
    private Boolean delFlag;


}
