package tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 数据元 Request VO 对象 DP_DATA_ELEM
 *
 * @author qdata
 * @date 2025-01-21
 */
@Schema(description = "数据元 Request VO")
@Data
public class DpDataElemPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;

    @Schema(description = "名称", example = "")
    private String name;

    @Schema(description = "英文名称", example = "")
    private String engName;

    @Schema(description = "类目编码", example = "")
    private String catCode;

    @Schema(description = "类型", example = "")
    private String type;

    @Schema(description = "责任人", example = "")
    private String personCharge;

    @Schema(description = "联系电话", example = "")
    private String contactNumber;

    @Schema(description = "字段类型", example = "")
    private String columnType;

    @Schema(description = "状态", example = "")
    private String status;

    @Schema(description = "描述", example = "")
    private String description;

    private Long documentId;
}
