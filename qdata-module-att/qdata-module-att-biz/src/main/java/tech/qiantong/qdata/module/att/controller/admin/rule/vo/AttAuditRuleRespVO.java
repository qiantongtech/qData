package tech.qiantong.qdata.module.att.controller.admin.rule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 稽查规则 Response VO 对象 ATT_AUDIT_RULE
 *
 * @author qdata
 * @date 2025-01-20
 */
@Schema(description = "稽查规则 Response VO")
@Data
public class AttAuditRuleRespVO implements Serializable {

    private static final long serialVersionUID = 1L;


    @Excel(name = "规则ID")
    @Schema(description = "规则ID", example = "")
    private Long id;

    @Excel(name = "规则名称")
    @Schema(description = "规则名称", example = "")
    private String name;

    @Excel(name = "质量维度")
    @Schema(description = "质量维度", example = "")
    private String qualityDim;

    @Excel(name = "规则类型")
    @Schema(description = "规则类型", example = "")
    private String type;

    @Excel(name = "规则级别")
    @Schema(description = "规则级别", example = "")
    private String level;

    @Excel(name = "规则描述")
    @Schema(description = "规则描述", example = "")
    private String description;

    @Excel(name = "是否有效")
    @Schema(description = "是否有效", example = "")
    private Boolean validFlag;

    @Excel(name = "删除标志")
    @Schema(description = "删除标志", example = "")
    private Boolean delFlag;

    @Excel(name = "创建人")
    @Schema(description = "创建人", example = "")
    private String createBy;

    @Excel(name = "创建人id")
    @Schema(description = "创建人id", example = "")
    private Long creatorId;

    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "")
    private Date createTime;

    @Excel(name = "更新人")
    @Schema(description = "更新人", example = "")
    private String updateBy;

    @Excel(name = "更新人id")
    @Schema(description = "更新人id", example = "")
    private Long updaterId;

    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", example = "")
    private Date updateTime;

    @Excel(name = "备注")
    @Schema(description = "备注", example = "")
    private String remark;

    /**
     * 数据元规则关联id
     */
    private Long ruleRelId;

    /**
     * tree数据类型 1:规则类型 2:规则
     */
    private String dataType;

    /**
     * 父级ID
     */
    private Long parentId;

    /**
     * 规则配置
     */
    private String ruleConfig;

    /**
     * 子节点列表
     */
    private List<AttAuditRuleRespVO> children;

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
