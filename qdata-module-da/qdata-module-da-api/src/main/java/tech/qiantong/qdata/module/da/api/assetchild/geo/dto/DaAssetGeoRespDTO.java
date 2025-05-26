package tech.qiantong.qdata.module.da.api.assetchild.geo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 数据资产-矢量 DTO 对象 DA_ASSET_GEO
 *
 * @author qdata
 * @date 2025-04-14
 */
@Data
public class DaAssetGeoRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 资产id */
    private Long assetId;

    /** 文件名称 */
    private String fileName;

    /** 文件路径 */
    private String fileUrl;

    @Schema(description = "文件类型", example = "")
    private String fileType;

    /** 要素类型 */
    private String elementType;

    /** 坐标系 */
    private String coordinateSystem;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
