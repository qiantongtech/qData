package tech.qiantong.qdata.module.da.controller.admin.assetchild.geo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * 数据资产-矢量 创建/修改 Request VO DA_ASSET_GEO
 *
 * @author qdata
 * @date 2025-04-14
 */
@Schema(description = "数据资产-矢量 Response VO")
@Data
public class DaAssetGeoSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "资产id", example = "")
    private Long assetId;

    @Schema(description = "文件名称", example = "")
    @Size(max = 256, message = "文件名称长度不能超过256个字符")
    private String fileName;

    @Schema(description = "文件路径", example = "")
    @Size(max = 256, message = "文件路径长度不能超过256个字符")
    private String fileUrl;

    @Schema(description = "文件类型", example = "")
    private String fileType;

    @Schema(description = "要素类型", example = "")
    @Size(max = 256, message = "要素类型长度不能超过256个字符")
    private String elementType;

    @Schema(description = "坐标系", example = "")
    @Size(max = 256, message = "坐标系长度不能超过256个字符")
    private String coordinateSystem;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
