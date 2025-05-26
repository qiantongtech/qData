package tech.qiantong.qdata.module.ds.controller.admin.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

/**
 * API服务 Request VO 对象 DS_API
 *
 * @author lhs
 * @date 2025-02-12
 */
@Schema(description = "API服务 Request VO")
@Data
public class DsApiReqVO{

    private static final long serialVersionUID = 1L;

    @Schema(description = "类目id", example = "")
    private Long catId;

    @Schema(description = "类目编码", example = "")
    private String catCode;

    @Schema(description = "类目名称", example = "")
    private String catName;

    @Schema(description = "API服务名称", example = "")
    private String name;

    @Schema(description = "状态", example = "")
    private String status;

    private String startDate;

    private String endDate;

    /**
     *转发类型;1:API 2:地理空间数据'
     */
    private String transmitType;

    /**
     *apiId
     */
    private String apiId;

    /**
     *Header配置json
     */
    private String headerJson;

    Map<String, Object> queryParams;

}
