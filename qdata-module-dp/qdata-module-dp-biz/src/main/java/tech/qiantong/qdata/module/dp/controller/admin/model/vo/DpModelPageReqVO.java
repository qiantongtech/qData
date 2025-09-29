package tech.qiantong.qdata.module.dp.controller.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 逻辑模型 Request VO 对象 DP_MODEL
 *
 * @author qdata
 * @date 2025-01-21
 */
@Schema(description = "逻辑模型 Request VO")
@Data
public class DpModelPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;

    @Schema(description = "模型编码", example = "")
    private String modelName;

    @Schema(description = "模型名称", example = "")
    private String modelComment;

    @Schema(description = "类目编码", example = "")
    private String catCode;


    @Schema(description = "状态", example = "")
    private String status;


    private Long documentId;








}
