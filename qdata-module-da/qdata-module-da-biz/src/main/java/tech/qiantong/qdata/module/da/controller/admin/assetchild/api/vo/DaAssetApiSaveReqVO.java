package tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * 数据资产-外部API 创建/修改 Request VO DA_ASSET_API
 *
 * @author qdata
 * @date 2025-04-14
 */
@Schema(description = "数据资产-外部API Response VO")
@Data
public class DaAssetApiSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "资产id", example = "")
    private Long assetId;

    @Schema(description = "API路径", example = "")
    @Size(max = 256, message = "API路径长度不能超过256个字符")
    private String url;

    @Schema(description = "请求方式", example = "")
    @Size(max = 256, message = "请求方式长度不能超过256个字符")
    private String httpMethod;

    @Schema(description = "开发者", example = "")
    private String developerName;

    @Schema(description = "应用名称", example = "")
    private String appName;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
