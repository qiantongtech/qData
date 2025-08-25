package tech.qiantong.qdata.module.att.api.rule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 稽查规则 DTO 对象 ATT_AUDIT_RULE
 *
 * @author qdata
 * @date 2025-01-20
 */
@Data
public class AttAuditRuleRespDTO {

    private static final long serialVersionUID = 1L;

    /** 规则ID */
    private Long id;

    /** 规则名称 */
    private String name;

    /** 质量维度 */
    private String qualityDim;

    /** 规则类型 */
    private String type;

    /** 规则级别 */
    private String level;

    /** 规则描述 */
    private String description;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;

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

}
