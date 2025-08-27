package tech.qiantong.qdata.module.da.controller.admin.asset.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiParamSaveReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiSaveReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.geo.vo.DaAssetGeoSaveReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.gis.vo.DaAssetGisSaveReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.video.vo.DaAssetVideoSaveReqVO;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * 数据资产 创建/修改 Request VO DA_ASSET
 *
 * @author lhs
 * @date 2025-01-21
 */
@Schema(description = "数据资产 Response VO")
@Data
public class DaAssetSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "资产名称", example = "")
    @Size(max = 256, message = "资产名称长度不能超过256个字符")
    private String name;

    @Schema(description = "资产类型", example = "")
    private String type;

    @Schema(description = "类目编码", example = "")
    @Size(max = 256, message = "类目编码长度不能超过256个字符")
    private String catCode;

    @Schema(description = "是申请过来的资产还是项目自己生成的资产0：申请，1：自创", example = "")
    private String sourceType;

    private List<String> themeIdList;

    @Schema(description = "数据连接id", example = "")
    @Size(max = 256, message = "数据连接id长度不能超过256个字符")
    private String datasourceId;

    @Schema(description = "表名称", example = "")
    @Size(max = 256, message = "表名称长度不能超过256个字符")
    private String tableName;

    @Schema(description = "表描述", example = "")
    @Size(max = 256, message = "表描述长度不能超过256个字符")
    private String tableComment;

    @Schema(description = "数据量", example = "")
    private Long dataCount;

    @Schema(description = "字段量", example = "")
    private Long fieldCount;

    /** 来源;1:数据发现；2:数据模型； */
    @Schema(description = "来源", example = "")
    private String source;

    @Schema(description = "状态", example = "")
    @Size(max = 256, message = "状态长度不能超过256个字符")
    private String status;

    @Schema(description = "描述", example = "")
    @Size(max = 256, message = "描述长度不能超过256个字符")
    private String description;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;

    //api
    @TableField(exist = false)
    private DaAssetApiSaveReqVO daAssetApi;
    //api
    @TableField(exist = false)
    private List<DaAssetApiParamSaveReqVO> daAssetApiParamList;

    /**
     * 矢量
     */
    @TableField(exist = false)
    private DaAssetGeoSaveReqVO daAssetGeo;

    /**
     * 地理空间服务
     */
    @TableField(exist = false)
    private DaAssetGisSaveReqVO daAssetGis;

    /**
     * 视频数据
     */
    @TableField(exist = false)
    private DaAssetVideoSaveReqVO daAssetVideo;


    @Schema(description = "项目id", example = "")
    private Long projectId;

    @Schema(description = "项目编码", example = "")
    private String projectCode;

    @Schema(description = "创建类型", example = "")
    private String createType;
}
