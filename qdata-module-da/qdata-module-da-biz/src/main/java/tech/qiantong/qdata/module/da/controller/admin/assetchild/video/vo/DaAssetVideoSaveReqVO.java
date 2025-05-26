package tech.qiantong.qdata.module.da.controller.admin.assetchild.video.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * 数据资产-视频数据 创建/修改 Request VO DA_ASSET_VIDEO
 *
 * @author qdata
 * @date 2025-04-14
 */
@Schema(description = "数据资产-视频数据 Response VO")
@Data
public class DaAssetVideoSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "资产id", example = "")
    private Long assetId;

    @Schema(description = "IP", example = "")
    @Size(max = 256, message = "IP长度不能超过256个字符")
    private String ip;

    @Schema(description = "端口号", example = "")
    private Long port;

    @Schema(description = "协议", example = "")
    @Size(max = 256, message = "协议长度不能超过256个字符")
    private String protocol;

    @Schema(description = "平台", example = "")
    @Size(max = 256, message = "平台长度不能超过256个字符")
    private String platform;

    @Schema(description = "配置JSON", example = "")
    private String config;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
