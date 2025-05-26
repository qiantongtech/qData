package tech.qiantong.qdata.module.dp.dal.dataobject.dataElem;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * 数据元数据资产关联信息 DO 对象 DP_DATA_ELEM_ASSET_REL
 *
 * @author qdata
 * @date 2025-01-21
 */
@Data
@TableName(value = "DP_DATA_ELEM_ASSET_REL")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("DP_DATA_ELEM_ASSET_REL_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DpDataElemAssetRelDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 数据元类型 */
    private String dataElemType;

    /** 数据元id */
    private String dataElemId;

    /** 资产id(数据表id) */
    private String assetId;

    /** 资产名称 */
    @TableField(exist = false)
    private String assetName;

    /** 表描述 */
    @TableField(exist = false)
    private String tableComment;

    /** 资产描述 */
    @TableField(exist = false)
    private String description;

    /** 数据表 */
    private String tableName;

    /** 关联字段id */
    private String columnId;

    /** 关联字段 */
    private String columnName;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    @TableLogic
    private Boolean delFlag;


}
