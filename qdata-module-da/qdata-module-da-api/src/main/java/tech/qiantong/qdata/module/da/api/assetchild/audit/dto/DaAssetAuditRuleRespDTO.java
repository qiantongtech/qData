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

package tech.qiantong.qdata.module.da.api.assetchild.audit.dto;

import lombok.Data;

import java.util.Date;

/**
 * 数据资产质量结果记录 DTO 对象 DA_ASSET_AUDIT_RULE
 *
 * @author qdata
 * @date 2025-05-09
 */
@Data
public class DaAssetAuditRuleRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 资产ID */
    private Long assetId;

    /** 表名称 */
    private String tableName;

    /** 字段名称/英文名称 */
    private String columnName;

    /** 字段注释/中文名称 */
    private String columnComment;

    /** 规则名称 */
    private String ruleName;

    /** 质量维度 */
    private String qualityDim;

    /** 规则类型 */
    private String ruleType;

    /** 规则级别 */
    private String ruleLevel;

    /** 规则描述 */
    private String ruleDescription;

    /** 规则配置 */
    private String ruleConfig;

    /** 校验总数 */
    private Long totalCount;

    /** 问题数 */
    private Long issueCount;

    /** 稽查时间 */
    private Date auditTime;

    /** 稽查批次号 */
    private String batchNo;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
