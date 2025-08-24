package tech.qiantong.qdata.module.att.controller.admin.rule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 稽查规则 Request VO 对象 ATT_AUDIT_RULE
 *
 * @author qdata
 * @date 2025-01-20
 */
@Schema(description = "稽查规则 Request VO")
@Data
public class AttAuditRulePageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;

    @Schema(description = "规则名称", example = "")
    private String name;

    @Schema(description = "质量维度", example = "")
    private String qualityDim;

    @Schema(description = "规则类型", example = "")
    private String type;
    @Schema(description = "规则级别", example = "")
    private String level;

    @Schema(description = "规则编码", example = "101")
    private String code;

    @Schema(description = "使用场景", example = "用于身份证号非空检查")
    private String useCase;

    @Schema(description = "示例", example = "字段值不能为空，如：ID=123456")
    private String example;

    @Schema(description = "图标地址", example = "/images/icon.png")
    private String iconPath;

    @Schema(description = "策略标识", example = "NOT_NULL_ID_CHECK")
    private String strategyKey;


    /** 是否有效 */
    private String validFlag;




}
