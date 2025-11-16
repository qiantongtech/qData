/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据资产质量结果记录 Response VO 对象 DA_ASSET_AUDIT_RULE
 *
 * @author qdata
 * @date 2025-05-09
 */
@Schema(description = "数据资产质量结果记录 Response VO")
@Data
public class DaAssetAuditRuleRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "资产ID")
    @Schema(description = "资产ID", example = "")
    private Long assetId;

    @Excel(name = "表名称")
    @Schema(description = "表名称", example = "")
    private String tableName;

    @Excel(name = "字段名称/英文名称")
    @Schema(description = "字段名称/英文名称", example = "")
    private String columnName;

    @Excel(name = "字段注释/中文名称")
    @Schema(description = "字段注释/中文名称", example = "")
    private String columnComment;

    @Excel(name = "规则名称")
    @Schema(description = "规则名称", example = "")
    private String ruleName;

    @Excel(name = "质量维度")
    @Schema(description = "质量维度", example = "")
    private String qualityDim;

    @Excel(name = "规则类型")
    @Schema(description = "规则类型", example = "")
    private String ruleType;

    @Excel(name = "规则级别")
    @Schema(description = "规则级别", example = "")
    private String ruleLevel;

    @Excel(name = "规则描述")
    @Schema(description = "规则描述", example = "")
    private String ruleDescription;

    @Excel(name = "规则配置")
    @Schema(description = "规则配置", example = "")
    private String ruleConfig;

    @Excel(name = "校验总数")
    @Schema(description = "校验总数", example = "")
    private Long totalCount;

    @Excel(name = "问题数")
    @Schema(description = "问题数", example = "")
    private Long issueCount;

    @Excel(name = "稽查时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "稽查时间", example = "")
    private Date auditTime;

    @Excel(name = "稽查批次号")
    @Schema(description = "稽查批次号", example = "")
    private String batchNo;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "")
    private Date createTime;

    @Excel(name = "更新人")
    @Schema(description = "更新人", example = "")
    private String updateBy;

    @Excel(name = "更新人id")
    @Schema(description = "更新人id", example = "")
    private Long updaterId;

    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", example = "")
    private Date updateTime;

    @Excel(name = "备注")
    @Schema(description = "备注", example = "")
    private String remark;

}
