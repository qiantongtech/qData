package tech.qiantong.qdata.module.dp.controller.admin.codeMap.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 数据元代码映射 Request VO 对象 DP_CODE_MAP
 *
 * @author qdata
 * @date 2025-01-21
 */
@Schema(description = "数据元代码映射 Request VO")
@Data
public class DpCodeMapPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "数据元id", example = "")
    private String dataElemId;

    @Schema(description = "原始值", example = "")
    private String originalValue;

    @Schema(description = "代码名", example = "")
    private String codeName;

    @Schema(description = "代码值", example = "")
    private String codeValue;




}
