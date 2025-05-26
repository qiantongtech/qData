package tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

import java.util.Date;

/**
 * 数据资产质量结果记录 Request VO 对象 DA_ASSET_AUDIT_RULE
 *
 * @author qdata
 * @date 2025-05-09
 */
@Schema(description = "数据资产质量结果记录 Request VO")
@Data
public class DaAssetAuditRulePageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "资产ID", example = "")
    private Long assetId;

    @Schema(description = "表名称", example = "")
    private String tableName;

    @Schema(description = "字段名称/英文名称", example = "")
    private String columnName;

    @Schema(description = "字段注释/中文名称", example = "")
    private String columnComment;

    @Schema(description = "规则名称", example = "")
    private String ruleName;

    @Schema(description = "质量维度", example = "")
    private String qualityDim;

    @Schema(description = "规则类型", example = "")
    private String ruleType;

    @Schema(description = "规则级别", example = "")
    private String ruleLevel;

    @Schema(description = "规则描述", example = "")
    private String ruleDescription;

    @Schema(description = "规则配置", example = "")
    private String ruleConfig;

    @Schema(description = "校验总数", example = "")
    private Long totalCount;

    @Schema(description = "问题数", example = "")
    private Long issueCount;

    @Schema(description = "稽查时间", example = "")
    private Date auditTime;

    @Schema(description = "稽查批次号", example = "")
    private String batchNo;




}
