package tech.qiantong.qdata.module.da.dal.dataobject.assetchild.gis;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * 数据资产-地理空间服务 DO 对象 DA_ASSET_GIS
 *
 * @author qdata
 * @date 2025-04-14
 */
@Data
@TableName(value = "DA_ASSET_GIS")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("DA_ASSET_GIS_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DaAssetGisDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 资产id */
    private Long assetId;

    /** 服务地址 */
    private String url;

    /** 服务类型 */
    private String type;

    /** 请求方式 */
    private String httpMethod;

    /** 坐标系 */
    private String coordinateSystem;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    @TableLogic
    private Boolean delFlag;


}
