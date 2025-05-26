package tech.qiantong.qdata.module.da.controller.admin.assetchild.gis.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * 数据资产-地理空间服务 创建/修改 Request VO DA_ASSET_GIS
 *
 * @author qdata
 * @date 2025-04-14
 */
@Schema(description = "数据资产-地理空间服务 Response VO")
@Data
public class DaAssetGisSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "资产id", example = "")
    private Long assetId;

    @Schema(description = "服务地址", example = "")
    @Size(max = 256, message = "服务地址长度不能超过256个字符")
    private String url;

    @Schema(description = "服务类型", example = "")
    @Size(max = 256, message = "服务类型长度不能超过256个字符")
    private String type;

    @Schema(description = "请求方式", example = "")
    @Size(max = 256, message = "请求方式长度不能超过256个字符")
    private String httpMethod;

    @Schema(description = "坐标系", example = "")
    @Size(max = 256, message = "坐标系长度不能超过256个字符")
    private String coordinateSystem;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
