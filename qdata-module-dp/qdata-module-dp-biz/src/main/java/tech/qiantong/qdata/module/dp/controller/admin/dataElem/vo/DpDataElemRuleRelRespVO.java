package tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据元数据规则关联信息 Response VO 对象 DP_DATA_ELEM_RULE_REL
 *
 * @author qdata
 * @date 2025-01-21
 */
@Schema(description = "数据元数据规则关联信息 Response VO")
@Data
public class DpDataElemRuleRelRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "数据元id")
    @Schema(description = "数据元id", example = "")
    private String dataElemId;

    @Excel(name = "规则类型")
    @Schema(description = "规则类型", example = "")
    private String ruleType;

    @Excel(name = "规则id")
    @Schema(description = "规则id", example = "")
    private String ruleId;

    @Excel(name = "规则配置")
    @Schema(description = "规则配置", example = "")
    private String ruleConfig;

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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "")
    private Date createTime;

    @Excel(name = "更新人")
    @Schema(description = "更新人", example = "")
    private String updateBy;

    @Excel(name = "更新人id")
    @Schema(description = "更新人id", example = "")
    private Long updaterId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", example = "")
    private Date updateTime;

    @Excel(name = "备注")
    @Schema(description = "备注", example = "")
    private String remark;

    @Schema(description = "规则描述")
    private String ruleDescription;

    @Schema(description = "错误描述")
    private String errDescription;

    @Schema(description = "修复建议")
    private String suggestion;

    @Schema(description = "where条件")
    private String whereClause;


}
