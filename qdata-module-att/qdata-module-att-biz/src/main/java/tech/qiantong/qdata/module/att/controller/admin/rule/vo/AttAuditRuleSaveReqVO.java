package tech.qiantong.qdata.module.att.controller.admin.rule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * 稽查规则 创建/修改 Request VO ATT_AUDIT_RULE
 *
 * @author qdata
 * @date 2025-01-20
 */
@Schema(description = "稽查规则 Response VO")
@Data
public class AttAuditRuleSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "规则名称", example = "")
    @Size(max = 256, message = "规则名称长度不能超过256个字符")
    private String name;

    @Schema(description = "质量维度", example = "")
    @Size(max = 256, message = "质量维度长度不能超过256个字符")
    private String qualityDim;

    @Schema(description = "规则类型", example = "")
    @Size(max = 256, message = "规则类型长度不能超过256个字符")
    private String type;

    @Schema(description = "规则级别", example = "")
    @Size(max = 256, message = "规则级别长度不能超过256个字符")
    private String level;

    @Schema(description = "规则描述", example = "")
    @Size(max = 256, message = "规则描述长度不能超过256个字符")
    private String description;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;

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
