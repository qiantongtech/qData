package tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 数据元代码 Request VO 对象 DP_DATA_ELEM_CODE
 *
 * @author qdata
 * @date 2025-01-21
 */
@Schema(description = "数据元代码 Request VO")
@Data
public class DpDataElemCodePageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "数据元id", example = "")
    private String dataElemId;

    @Schema(description = "代码值", example = "")
    private String codeValue;

    @Schema(description = "代码名称", example = "")
    private String codeName;




}
