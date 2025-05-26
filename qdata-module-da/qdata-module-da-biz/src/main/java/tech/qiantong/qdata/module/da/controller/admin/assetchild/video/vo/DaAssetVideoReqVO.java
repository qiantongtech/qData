package tech.qiantong.qdata.module.da.controller.admin.assetchild.video.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 数据资产-视频数据 Request VO 对象 DA_ASSET_VIDEO
 *
 * @author qdata
 * @date 2025-04-14
 */
@Schema(description = "数据资产-视频数据 Request VO")
@Data
public class DaAssetVideoReqVO {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "资产id", example = "")
    private Long assetId;

    @Schema(description = "IP", example = "")
    private String ip;

    @Schema(description = "端口号", example = "")
    private Long port;

    @Schema(description = "协议", example = "")
    private String protocol;

    @Schema(description = "平台", example = "")
    private String platform;

    @Schema(description = "配置JSON", example = "")
    private String config;




}
