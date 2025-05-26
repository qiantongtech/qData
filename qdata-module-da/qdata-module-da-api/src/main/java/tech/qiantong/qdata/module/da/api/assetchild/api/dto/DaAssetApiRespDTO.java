package tech.qiantong.qdata.module.da.api.assetchild.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 数据资产-外部API DTO 对象 DA_ASSET_API
 *
 * @author qdata
 * @date 2025-04-14
 */
@Data
public class DaAssetApiRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 资产id */
    private Long assetId;

    /** API路径 */
    private String url;

    @Schema(description = "开发者", example = "")
    private String developerName;

    @Schema(description = "应用名称", example = "")
    private String appName;

    /** 请求方式 */
    private String httpMethod;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
