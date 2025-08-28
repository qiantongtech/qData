package tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 数据元数据规则关联信息 Request VO 对象 DP_DATA_ELEM_RULE_REL
 *
 * @author qdata
 * @date 2025-01-21
 */
@Schema(description = "数据元数据规则关联信息 Request VO")
@Data
public class DpDataElemRuleRelPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID", example = "")
    private Long id;

    @Schema(description = "数据元id", example = "")
    private Long dataElemId;

    @Schema(description = "规则类型", example = "")
    private String type;

    @Schema(description = "规则id", example = "")
    private Long ruleId;

}
