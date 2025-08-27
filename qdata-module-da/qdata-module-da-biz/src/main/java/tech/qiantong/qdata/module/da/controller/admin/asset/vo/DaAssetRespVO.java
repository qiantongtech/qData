package tech.qiantong.qdata.module.da.controller.admin.asset.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiParamRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.geo.vo.DaAssetGeoRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.gis.vo.DaAssetGisRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.theme.vo.DaAssetThemeRelRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.video.vo.DaAssetVideoRespVO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 数据资产 Response VO 对象 DA_ASSET
 *
 * @author lhs
 * @date 2025-01-21
 */
@Schema(description = "数据资产 Response VO")
@Data
public class DaAssetRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "资产名称")
    @Schema(description = "资产名称", example = "")
    private String name;

    @Excel(name = "资产类型")
    @Schema(description = "资产类型", example = "")
    private String type;

    @Excel(name = "类目编码")
    @Schema(description = "类目编码", example = "")
    private String catCode;

    private String catName;

    @TableField(exist = false)
    private List<DaAssetThemeRelRespVO> daAssetThemeRelList;

    @Excel(name = "是申请过来的资产还是项目自己生成的资产0：申请，1：自创")
    @Schema(description = "是申请过来的资产还是项目自己生成的资产0：申请，1：自创", example = "")
    private String sourceType;


    @Excel(name = "数据连接id")
    @Schema(description = "数据连接id", example = "")
    private Long datasourceId;

    @TableField(exist = false)
    private String datasourceName;

    @TableField(exist = false)
    private String datasourceIp;

    @TableField(exist = false)
    private String datasourceType;

    @Excel(name = "表名称")
    @Schema(description = "表名称", example = "")
    private String tableName;

    @Excel(name = "表描述")
    @Schema(description = "表描述", example = "")
    private String tableComment;

    @Excel(name = "数据量")
    @Schema(description = "数据量", example = "")
    private Long dataCount;

    @Excel(name = "字段量")
    @Schema(description = "字段量", example = "")
    private Long fieldCount;

    /** 来源;1:数据发现；2:数据模型； */
    @Schema(description = "来源", example = "")
    private String source;

    @Excel(name = "状态")
    @Schema(description = "状态", example = "")
    private String status;

    @Excel(name = "描述")
    @Schema(description = "描述", example = "")
    private String description;

    @Excel(name = "是否有效")
    @Schema(description = "是否有效", example = "")
    private Boolean validFlag;

    @Excel(name = "删除标志")
    @Schema(description = "删除标志", example = "")
    private Boolean delFlag;

    @Excel(name = "创建人")
    @Schema(description = "创建人", example = "")
    private String createBy;

    @Excel(name = "创建人id")
    @Schema(description = "创建人id", example = "")
    private Long creatorId;

    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")

    private Date createTime;

    @Excel(name = "更新人")
    @Schema(description = "更新人", example = "")
    private String updateBy;

    @Excel(name = "更新人id")
    @Schema(description = "更新人id", example = "")
    private Long updaterId;

    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")

    private Date updateTime;

    @Excel(name = "备注")
    @Schema(description = "备注", example = "")
    private String remark;

    //api
    @TableField(exist = false)
    private DaAssetApiRespVO daAssetApi;
    //api
    @TableField(exist = false)
    private List<DaAssetApiParamRespVO> daAssetApiParamList;

    /**
     * 矢量
     */
    @TableField(exist = false)
    private DaAssetGeoRespVO daAssetGeo;

    /**
     * 地理空间服务
     */
    @TableField(exist = false)
    private DaAssetGisRespVO daAssetGis;

    /**
     * 视频数据
     */
    @TableField(exist = false)
    private DaAssetVideoRespVO daAssetVideo;

    @Schema(description = "项目id", example = "")
    private Long projectId;

    @Schema(description = "项目编码", example = "")
    private String projectCode;

    @Schema(description = "创建类型", example = "")
    private String createType;

}
