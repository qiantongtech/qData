package tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

/**
 * 数据资产-外部API Request VO 对象 DA_ASSET_API
 *
 * @author qdata
 * @date 2025-04-14
 */
@Schema(description = "数据资产-外部API Request VO")
@Data
public class DaAssetApiReqVO {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "资产id", example = "")
    private Long assetId;

    @Schema(description = "API路径", example = "")
    private String url;

    @Schema(description = "请求方式", example = "")
    private String httpMethod;

    @Schema(description = "开发者", example = "")
    private String developerName;

    @Schema(description = "应用名称", example = "")
    private String appName;

    Map<String, Object> queryParams;
}
