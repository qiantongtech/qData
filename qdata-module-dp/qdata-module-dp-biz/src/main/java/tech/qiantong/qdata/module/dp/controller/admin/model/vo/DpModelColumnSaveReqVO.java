package tech.qiantong.qdata.module.dp.controller.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * 逻辑模型属性信息 创建/修改 Request VO DP_MODEL_COLUMN
 *
 * @author qdata
 * @date 2025-01-21
 */
@Schema(description = "逻辑模型属性信息 Response VO")
@Data
public class DpModelColumnSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "逻辑模型表ID", example = "")
    private Long modelId;

    @Schema(description = "英文名称", example = "")
    @Size(max = 256, message = "英文名称长度不能超过256个字符")
    private String engName;

    @Schema(description = "中文名称", example = "")
    @Size(max = 256, message = "中文名称长度不能超过256个字符")
    private String cnName;

    @Schema(description = "数据类型", example = "")
    @Size(max = 256, message = "数据类型长度不能超过256个字符")
    private String columnType;

    @Schema(description = "属性长度", example = "")
    private Long columnLength;

    @Schema(description = "小数长度", example = "")
    private Long columnScale;

    @Schema(description = "默认值", example = "")
    @Size(max = 256, message = "默认值长度不能超过256个字符")
    private String defaultValue;

    @Schema(description = "是否主键", example = "")
    @Size(max = 256, message = "是否主键长度不能超过256个字符")
    private String pkFlag;

    @Schema(description = "是否必填", example = "")
    @Size(max = 256, message = "是否必填长度不能超过256个字符")
    private String nullableFlag;

    @Schema(description = "排序", example = "")
    private Long sortOrder;

    @Schema(description = "权威部门", example = "")
    @Size(max = 256, message = "权威部门长度不能超过256个字符")
    private String authorityDept;

    @Schema(description = "数据元id", example = "")
    private Long dataElemId;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
