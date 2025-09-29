package tech.qiantong.qdata.module.dp.dal.dataobject.dataElem;

import java.util.List;
import java.util.Set;

import lombok.*;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * 数据元 DO 对象 DP_DATA_ELEM
 *
 * @author qdata
 * @date 2025-01-21
 */
@Data
@TableName(value = "DP_DATA_ELEM")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("DP_DATA_ELEM_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DpDataElemDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 英文名称
     */
    private String engName;

    /**
     * 类目编码
     */
    private String catCode;

    @TableField(exist = false)
    private String catName;

    /**
     * 类型
     */
    private String type;

    /**
     * 责任人
     */
    private String personCharge;

    /**
     * 责任人名称
     */
    @TableField(exist = false)
    private String personChargeName;

    /**
     * 联系电话
     */
    private String contactNumber;

    /**
     * 字段类型
     */
    private String columnType;

    /**
     * 状态
     */
    private String status;

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

    @TableField(exist = false)
    private Set<Long> columnId;

    private Long documentId;

    /** 名称 */
    @TableField(exist = false)
    private String documentName;

    /** 名称 */
    @TableField(exist = false)
    private String documentCode;

    /** 文件标准类型字段， */
    @TableField(exist = false)
    private String documentType;
}
