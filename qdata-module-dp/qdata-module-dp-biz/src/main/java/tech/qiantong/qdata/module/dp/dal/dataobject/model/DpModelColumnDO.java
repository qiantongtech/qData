package tech.qiantong.qdata.module.dp.dal.dataobject.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * 逻辑模型属性信息 DO 对象 DP_MODEL_COLUMN
 *
 * @author qdata
 * @date 2025-01-21
 */
@Data
@TableName(value = "DP_MODEL_COLUMN")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("DP_MODEL_COLUMN_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DpModelColumnDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 逻辑模型表ID */
    private Long modelId;

    /** 英文名称 */
    private String engName;

    /** 中文名称 */
    private String cnName;

    /** 数据类型 */
    private String columnType;

    /** 属性长度 */
    private Long columnLength;

    /** 小数长度 */
    private Long columnScale;

    /** 默认值 */
    private String defaultValue;

    /** 是否主键 */
    private String pkFlag;

    /** 是否必填 */
    private String nullableFlag;

    /** 排序 */
    private Long sortOrder;

    /** 权威部门 */
    private Long authorityDept;

    /** 数据元id */
    private Long dataElemId;

    /** 数据元名称 */
    @TableField(exist = false)
    private String dataElemName;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    @TableLogic
    private Boolean delFlag;


}
