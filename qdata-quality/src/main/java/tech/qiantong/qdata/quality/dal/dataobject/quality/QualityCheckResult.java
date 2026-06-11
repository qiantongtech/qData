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

    private String ruleId;         // 质量规则ID
    private String batch;          // 执行批次号（如20250715123001）
    private Integer errorCount;    // 异常记录数
    private Integer totalCount;    // 总记录数
    private String errorMessage;   // 错误信息（如果有异常）

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
