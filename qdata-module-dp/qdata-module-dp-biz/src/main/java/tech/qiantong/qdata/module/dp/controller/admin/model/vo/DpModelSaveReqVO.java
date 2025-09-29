package tech.qiantong.qdata.module.dp.controller.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * 逻辑模型 创建/修改 Request VO DP_MODEL
 *
 * @author qdata
 * @date 2025-01-21
 */
@Schema(description = "逻辑模型 Response VO")
@Data
public class DpModelSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "模型编码", example = "")
    @NotBlank(message = "模型编码不能为空")
    @Size(max = 256, message = "模型编码长度不能超过256个字符")
    private String modelName;

    @Schema(description = "模型名称", example = "")
    @NotBlank(message = "模型名称不能为空")
    @Size(max = 256, message = "模型名称长度不能超过256个字符")
    private String modelComment;

    @Schema(description = "数据源id", example = "")
    private String datasourceId;

    private Long documentId;

    @Schema(description = "类目编码", example = "")
    @NotBlank(message = "类目编码不能为空")
    @Size(max = 256, message = "类目编码长度不能超过256个字符")
    private String catCode;

    @Schema(description = "状态", example = "")
    @NotBlank(message = "状态不能为空")
    @Size(max = 256, message = "状态长度不能超过256个字符")
    private String status;

    @Schema(description = "创建方式", example = "")
    @NotBlank(message = "创建方式不能为空")
    @Size(max = 256, message = "创建方式长度不能超过256个字符")
    private String createType;

    @Schema(description = "联系人", example = "")
    @Size(max = 256, message = "联系人长度不能超过256个字符")
    private String contact;

    @Schema(description = "联系电话", example = "")
    @Size(max = 256, message = "联系电话长度不能超过256个字符")
    private String contactNumber;

    @Schema(description = "描述", example = "")
    @Size(max = 256, message = "描述长度不能超过256个字符")
    private String description;


}
