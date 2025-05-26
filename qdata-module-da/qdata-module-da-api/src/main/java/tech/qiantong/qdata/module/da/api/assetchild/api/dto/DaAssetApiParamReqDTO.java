package tech.qiantong.qdata.module.da.api.assetchild.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 数据资产-外部API-参数 DTO 对象 DA_ASSET_API_PARAM
 *
 * @author qdata
 * @date 2025-04-14
 */
@Data
public class DaAssetApiParamReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** API id */
    private Long apiId;

    /** 父级id */
    private Long parentId;

    /** 参数名称 */
    private String name;

    @Schema(description = "数据默认值", example = "")
    private String defaultValue;
    @Schema(description = "示例值", example = "")
    private String exampleValue;
    @Schema(description = "描述", example = "")
    private String description;

    /** 参数类型 */
    private String type;

    /** 是否必填 */
    private String requestFlag;

    /** 字段类型 */
    private String columnType;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
