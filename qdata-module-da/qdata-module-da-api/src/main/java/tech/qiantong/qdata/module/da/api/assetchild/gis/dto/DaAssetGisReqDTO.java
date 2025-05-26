package tech.qiantong.qdata.module.da.api.assetchild.gis.dto;

import lombok.Data;

/**
 * 数据资产-地理空间服务 DTO 对象 DA_ASSET_GIS
 *
 * @author qdata
 * @date 2025-04-14
 */
@Data
public class DaAssetGisReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
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
    private Boolean delFlag;


}
