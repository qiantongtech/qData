package tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * 数据资产-外部API-参数 创建/修改 Request VO DA_ASSET_API_PARAM
 *
 * @author qdata
 * @date 2025-04-14
 */
@Schema(description = "数据资产-外部API-参数 Response VO")
@Data
public class DaAssetApiParamSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "API id", example = "")
    private Long apiId;

    @Schema(description = "父级id", example = "")
    private Long parentId;

    @Schema(description = "参数名称", example = "")
    @Size(max = 256, message = "参数名称长度不能超过256个字符")
    private String name;

    @Schema(description = "参数类型", example = "")
    @Size(max = 256, message = "参数类型长度不能超过256个字符")
    private String type;

    @Schema(description = "数据默认值", example = "")
    private String defaultValue;
    @Schema(description = "示例值", example = "")
    private String exampleValue;
    @Schema(description = "描述", example = "")
    private String description;

    @Schema(description = "是否必填", example = "")
    @Size(max = 256, message = "是否必填长度不能超过256个字符")
    private String requestFlag;

    @Schema(description = "字段类型", example = "")
    @Size(max = 256, message = "字段类型长度不能超过256个字符")
    private String columnType;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;

    @TableField(exist = false)
    private List<DaAssetApiParamSaveReqVO> daAssetApiParamList;

}
