package tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 数据资产质量结果记录 创建/修改 Request VO DA_ASSET_AUDIT_RULE
 *
 * @author qdata
 * @date 2025-05-09
 */
@Schema(description = "数据资产质量结果记录 Response VO")
@Data
public class DaAssetAuditRuleSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "资产ID", example = "")
    private Long assetId;

    @Schema(description = "表名称", example = "")
    @Size(max = 256, message = "表名称长度不能超过256个字符")
    private String tableName;

    @Schema(description = "字段名称/英文名称", example = "")
    @Size(max = 256, message = "字段名称/英文名称长度不能超过256个字符")
    private String columnName;

    @Schema(description = "字段注释/中文名称", example = "")
    @Size(max = 256, message = "字段注释/中文名称长度不能超过256个字符")
    private String columnComment;

    @Schema(description = "规则名称", example = "")
    @Size(max = 256, message = "规则名称长度不能超过256个字符")
    private String ruleName;

    @Schema(description = "质量维度", example = "")
    @Size(max = 256, message = "质量维度长度不能超过256个字符")
    private String qualityDim;

    @Schema(description = "规则类型", example = "")
    @Size(max = 256, message = "规则类型长度不能超过256个字符")
    private String ruleType;

    @Schema(description = "规则级别", example = "")
    @Size(max = 256, message = "规则级别长度不能超过256个字符")
    private String ruleLevel;

    @Schema(description = "规则描述", example = "")
    @Size(max = 256, message = "规则描述长度不能超过256个字符")
    private String ruleDescription;

    @Schema(description = "规则配置", example = "")
    @Size(max = 256, message = "规则配置长度不能超过256个字符")
    private String ruleConfig;

    @Schema(description = "校验总数", example = "")
    private Long totalCount;

    @Schema(description = "问题数", example = "")
    private Long issueCount;

    @Schema(description = "稽查时间", example = "")
    private Date auditTime;

    @Schema(description = "稽查批次号", example = "")
    @Size(max = 256, message = "稽查批次号长度不能超过256个字符")
    private String batchNo;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
