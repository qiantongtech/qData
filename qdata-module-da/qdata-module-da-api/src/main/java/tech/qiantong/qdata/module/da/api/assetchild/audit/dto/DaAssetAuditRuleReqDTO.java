/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.module.da.api.assetchild.audit.dto;

import lombok.Data;

import java.util.Date;

/**
 * Data Asset Quality Result Record DTO DA_ASSET_AUDIT_RULE
 *
 * @author qdata
 * @date 2025-05-09
 */
@Data
public class DaAssetAuditRuleReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Asset ID */
    private Long assetId;

    /** Table Name */
    private String tableName;

    /** Column Name / English Name */
    private String columnName;

    /** Column Comment / Chinese Name */
    private String columnComment;

    /** Rule Name */
    private String ruleName;

    /** Quality Dimension */
    private String qualityDim;

    /** Rule Type */
    private String ruleType;

    /** Rule Level */
    private String ruleLevel;

    /** Rule Description */
    private String ruleDescription;

    /** Rule Configuration */
    private String ruleConfig;

    /** Validation Total */
    private Long totalCount;

    /** Issue Count */
    private Long issueCount;

    /** Audit Time */
    private Date auditTime;

    /** Audit Batch No. */
    private String batchNo;

    /** Valid Flag */
    private Boolean validFlag;

    /** Delete Flag */
    private Boolean delFlag;


}
