package tech.qiantong.qdata.module.ds.controller.admin.apiLog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * API服务调用日志 Request VO 对象 DS_API_LOG
 *
 * @author lhs
 * @date 2025-02-12
 */
@Schema(description = "API服务调用日志 Request VO")
@Data
public class DsApiLogPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;

    @Schema(description = "类目ID", example = "")
    private Long catId;

    @Schema(description = "类目编码", example = "")
    private String catCode;

    @Schema(description = "类目名称", example = "")
    private String catName;

    @Schema(description = "调用API服务Id", example = "")
    private String apiId;


    @Schema(description = "调用API服务名称", example = "")
    private String apiName;


    @Schema(description = "调用者id", example = "")
    private String callerId;

    private String status;












}
