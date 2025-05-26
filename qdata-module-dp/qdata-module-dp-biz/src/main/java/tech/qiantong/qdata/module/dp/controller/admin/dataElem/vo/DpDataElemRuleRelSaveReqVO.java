package tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * 数据元数据规则关联信息 创建/修改 Request VO DP_DATA_ELEM_RULE_REL
 *
 * @author qdata
 * @date 2025-01-21
 */
@Schema(description = "数据元数据规则关联信息 Response VO")
@Data
public class DpDataElemRuleRelSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "数据元id", example = "")
    @Size(max = 256, message = "数据元id长度不能超过256个字符")
    private String dataElemId;

    @Schema(description = "规则类型", example = "")
    @Size(max = 256, message = "规则类型长度不能超过256个字符")
    private String ruleType;

    @Schema(description = "规则id", example = "")
    @Size(max = 256, message = "规则id长度不能超过256个字符")
    private String ruleId;

    @Schema(description = "规则配置", example = "")
    @Size(max = 256, message = "规则配置长度不能超过256个字符")
    private String ruleConfig;


}
