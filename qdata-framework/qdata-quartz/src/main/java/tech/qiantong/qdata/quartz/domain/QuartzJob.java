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

package tech.qiantong.qdata.quartz.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import tech.qiantong.qdata.common.annotation.Excel;

/**
 Quartz business scheduling task table: quartz_job. * <p>
 The field definitions are consistent with {@link SysJob} to facilitate the reuse of the existing Quartz scheduling execution chain. *
 * @author qdata
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuartzJob extends SysJob {
    private static final long serialVersionUID = 1L;
    /**
     * Execution strategies (PARALLEL, SERIAL_WAIT, SERIAL_DISCARD, SERIAL_PRIORITY)
     */
    @Excel(name = "执行策略")
    private String executionType;
}
