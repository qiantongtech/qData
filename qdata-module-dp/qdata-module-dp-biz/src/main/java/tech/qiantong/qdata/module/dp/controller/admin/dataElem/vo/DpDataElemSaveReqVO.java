package tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * 数据元 创建/修改 Request VO DP_DATA_ELEM
 *
 * @author qdata
 * @date 2025-01-21
 */
@Schema(description = "数据元 Response VO")
@Data
public class DpDataElemSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "名称", example = "")
    @Size(max = 256, message = "名称长度不能超过256个字符")
    private String name;

    @Schema(description = "英文名称", example = "")
    @Size(max = 256, message = "英文名称长度不能超过256个字符")
    private String engName;

    @Schema(description = "类目编码", example = "")
    @Size(max = 256, message = "类目编码长度不能超过256个字符")
    private String catCode;

    @Schema(description = "类型", example = "")
    @Size(max = 256, message = "类型长度不能超过256个字符")
    private String type;

    @Schema(description = "责任人", example = "")
    @Size(max = 256, message = "责任人长度不能超过256个字符")
    private String personCharge;

    @Schema(description = "联系电话", example = "")
    @Size(max = 256, message = "联系电话长度不能超过256个字符")
    private String contactNumber;

    @Schema(description = "字段类型", example = "")
    @Size(max = 256, message = "字段类型长度不能超过256个字符")
    private String columnType;

    @Schema(description = "状态", example = "")
    @Size(max = 256, message = "状态长度不能超过256个字符")
    private String status;

    @Schema(description = "描述", example = "")
    @Size(max = 256, message = "描述长度不能超过256个字符")
    private String description;

    private Long documentId;
}
