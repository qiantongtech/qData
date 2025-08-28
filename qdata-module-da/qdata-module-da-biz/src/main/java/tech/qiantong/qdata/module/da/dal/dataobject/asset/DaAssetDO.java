package tech.qiantong.qdata.module.da.dal.dataobject.asset;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.theme.vo.DaAssetThemeRelRespVO;

import java.util.List;

/**
 * 数据资产 DO 对象 DA_ASSET
 *
 * @author lhs
 * @date 2025-01-21
 */
@Data
@TableName(value = "DA_ASSET")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("DA_ASSET_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DaAssetDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 资产名称 */
    private String name;

    @Schema(description = "资产类型", example = "")
    private String type;

    /** 类目编码 */
    private String catCode;

    @TableField(exist = false)
    private String catName;

    /**是申请过来的资产还是项目自己生成的资产0：申请，1：自创 */
    @TableField(exist = false)
    private String sourceType;

    @TableField(exist = false)
    private List<DaAssetThemeRelRespVO> daAssetThemeRelList;

    /** 数据连接id */
    private Long datasourceId;

    @TableField(exist = false)
    private String datasourceName;

    @TableField(exist = false)
    private String datasourceIp;

    @TableField(exist = false)
    private String datasourceType;

    /** 表名称 */
    private String tableName;

    /** 表描述 */
    private String tableComment;

    /** 数据量 */
    private Long dataCount;

    /** 字段量 */
    private Long fieldCount;

    /** 来源;1:数据发现；2:数据模型； */
    private String source;

    /** 状态 */
    private String status;

    /** 描述 */
    private String description;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    @TableLogic
    private Boolean delFlag;

//    //api
//    @TableField(exist = false)
//    private DaAssetApiRespVO daAssetApi;
//    //api
//    @TableField(exist = false)
//    private List<DaAssetApiParamRespVO> daAssetApiParamList;
//
//    /**
//     * 矢量
//     */
//    @TableField(exist = false)
//    private DaAssetGeoRespVO daAssetGeo;
//
//    /**
//     * 地理空间服务
//     */
//    @TableField(exist = false)
//    private DaAssetGisRespVO daAssetGis;
//
//    /**
//     * 视频数据
//     */
//    @TableField(exist = false)
//    private DaAssetVideoRespVO daAssetVideo;

    @Schema(description = "项目id", example = "")
    @TableField(exist = false)
    private Long projectId;

    @Schema(description = "项目编码", example = "")
    @TableField(exist = false)
    private String projectCode;
    @Schema(description = "创建类型", example = "")
    private String createType;
}
