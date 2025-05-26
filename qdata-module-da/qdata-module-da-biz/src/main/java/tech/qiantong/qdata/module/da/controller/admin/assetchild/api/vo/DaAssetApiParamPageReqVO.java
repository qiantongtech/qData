package tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 数据资产-外部API-参数 Request VO 对象 DA_ASSET_API_PARAM
 *
 * @author qdata
 * @date 2025-04-14
 */
@Schema(description = "数据资产-外部API-参数 Request VO")
@Data
public class DaAssetApiParamPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "API id", example = "")
    private Long apiId;

    @Schema(description = "父级id", example = "")
    private Long parentId;

    @Schema(description = "参数名称", example = "")
    private String name;

    @Schema(description = "参数类型", example = "")
    private String type;

    @Schema(description = "是否必填", example = "")
    private String requestFlag;

    @Schema(description = "字段类型", example = "")
    private String columnType;


    @Schema(description = "数据默认值", example = "")
    private String defaultValue;
    @Schema(description = "示例值", example = "")
    private String exampleValue;
    @Schema(description = "描述", example = "")
    private String description;



}
