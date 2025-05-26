package tech.qiantong.qdata.module.dp.controller.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 逻辑模型属性信息 Request VO 对象 DP_MODEL_COLUMN
 *
 * @author qdata
 * @date 2025-01-21
 */
@Schema(description = "逻辑模型属性信息 Request VO")
@Data
public class DpModelColumnPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "逻辑模型表ID", example = "")
    private Long modelId;

    @Schema(description = "英文名称", example = "")
    private String engName;

    @Schema(description = "中文名称", example = "")
    private String cnName;

    @Schema(description = "数据类型", example = "")
    private String columnType;

    @Schema(description = "属性长度", example = "")
    private Long columnLength;

    @Schema(description = "小数长度", example = "")
    private Long columnScale;

    @Schema(description = "默认值", example = "")
    private String defaultValue;

    @Schema(description = "是否主键", example = "")
    private String pkFlag;

    @Schema(description = "是否必填", example = "")
    private String nullableFlag;

    @Schema(description = "排序", example = "")
    private Long sortOrder;

    @Schema(description = "权威部门", example = "")
    private String authorityDept;

    @Schema(description = "数据元id", example = "")
    private Long dataElemId;




}
