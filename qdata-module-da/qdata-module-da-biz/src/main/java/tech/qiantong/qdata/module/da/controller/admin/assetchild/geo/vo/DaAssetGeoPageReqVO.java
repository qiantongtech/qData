package tech.qiantong.qdata.module.da.controller.admin.assetchild.geo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 数据资产-矢量 Request VO 对象 DA_ASSET_GEO
 *
 * @author qdata
 * @date 2025-04-14
 */
@Schema(description = "数据资产-矢量 Request VO")
@Data
public class DaAssetGeoPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "资产id", example = "")
    private Long assetId;

    @Schema(description = "文件名称", example = "")
    private String fileName;

    @Schema(description = "文件路径", example = "")
    private String fileUrl;

    @Schema(description = "文件类型", example = "")
    private String fileType;

    @Schema(description = "要素类型", example = "")
    private String elementType;

    @Schema(description = "坐标系", example = "")
    private String coordinateSystem;


}
