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

package tech.qiantong.qdata.api.ds.api.etl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <P>
 * 用途:手动启动一次的参数
 * </p>
 *
 * @author: FXB
 * @create: 2025-03-26 18:12
 **/
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DsStartTaskReqDTO {
    /**
     * 流程编码
     */
    private Long processDefinitionCode;
    /**
     * 失败策略 写死 CONTINUE
     * CONTINUE 为继续
     */
    private String failureStrategy;
    /**
     * 写死 NONE
     */
    private String warningType;
    /**
     * 写死 MEDIUM
     */
    private String processInstancePriority;
    /**
     * 写死{"complementStartDate":"当前天 00:00:00","complementEndDate":"当前天 00:00:00"}
     * 例子{"complementStartDate":"2025-03-26 00:00:00","complementEndDate":"2025-03-26 00:00:00"}
     */
    private String scheduleTime;//: {"complementStartDate":"2025-03-26 00:00:00","complementEndDate":"2025-03-26 00:00:00"}
}
