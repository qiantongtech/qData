package tech.qiantong.qdata.module.ds.controller.admin.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * API服务 创建/修改 Request VO DS_API
 *
 * @author lhs
 * @date 2025-02-12
 */
@Schema(description = "API服务 Response VO")
@Data
public class DsApiSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "类目id")
    private Long catId;

    @Schema(description = "类目编码")
    private String catCode;

    @Schema(description = "API服务名称", example = "")
    @Size(max = 256, message = "API服务名称长度不能超过256个字符")
    private String name;

    @Schema(description = "类目名称", example = "")
    private String catName;

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

    @Schema(description = "API版本", example = "")
    @Size(max = 256, message = "API版本长度不能超过256个字符")
    private String apiVersion;

    @Schema(description = "API路径", example = "")
    @Size(max = 256, message = "API路径长度不能超过256个字符")
    private String apiUrl;

    @Schema(description = "请求方式", example = "")
    @Size(max = 256, message = "请求方式长度不能超过256个字符")
    private String reqMethod;

    @Schema(description = "服务提供类型", example = "")
    @Size(max = 256, message = "服务提供类型长度不能超过256个字符")
    private String apiServiceType;

    @Schema(description = "返回结果类型", example = "")
    @Size(max = 256, message = "返回结果类型长度不能超过256个字符")
    private String resDataType;

    @Schema(description = "IP黑名单多个，隔开", example = "")
    @Size(max = 256, message = "IP黑名单多个，隔开长度不能超过256个字符")
    private String denyIp;

    @Schema(description = "执行配置JSON", example = "")
    @Size(max = 256, message = "执行配置JSON长度不能超过256个字符")
    private String configJson;

    @Schema(description = "限流配置JSON", example = "")
    @Size(max = 256, message = "限流配置JSON长度不能超过256个字符")
    private String limitJson;

    @Schema(description = "请求参数", example = "")
    @Size(max = 256, message = "请求参数长度不能超过256个字符")
    private String reqParams;

    @Schema(description = "返回参数", example = "")
    @Size(max = 256, message = "返回参数长度不能超过256个字符")
    private String resParams;

    @Schema(description = "描述", example = "")
    @Size(max = 256, message = "描述长度不能超过256个字符")
    private String description;

    @Schema(description = "状态", example = "")
    @Size(max = 256, message = "状态长度不能超过256个字符")
    private String status;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
