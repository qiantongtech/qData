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

package tech.qiantong.qdata.quality.dal.dataobject.quality;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QualityCheckResult {

    private String ruleId;         // Quality rule ID
    private String batch;          // Execution batch number (such as 20250715123001)
    private Integer errorCount;    // Number of abnormal records
    private Integer totalCount;    // Total number of records
    private String errorMessage;   // Error message (if there is an exception)

    public QualityCheckResult(String ruleId, String batch, String errorMessage) {
        this.ruleId = ruleId;
        this.batch = batch;
        this.errorMessage = errorMessage;
    }
    public QualityCheckResult(String ruleId, String batch, Integer errorCount,Integer totalCount) {
        this.ruleId = ruleId;
        this.batch = batch;
        this.errorCount = errorCount;
        this.totalCount = totalCount;
    }
}
