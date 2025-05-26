package tech.qiantong.qdata.module.att.controller.admin.rule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 清洗规则 Request VO 对象 ATT_CLEAN_RULE
 *
 * @author qdata
 * @date 2025-01-20
 */
@Schema(description = "清洗规则 Request VO")
@Data
public class AttCleanRulePageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;

    @Schema(description = "规则名称", example = "")
    private String name;

    @Schema(description = "规则类型", example = "")
    private String type;
    @Schema(description = "规则级别", example = "")
    private String level;






}
