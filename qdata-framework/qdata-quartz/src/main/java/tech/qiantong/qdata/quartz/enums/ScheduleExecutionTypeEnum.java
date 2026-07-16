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

package tech.qiantong.qdata.quartz.enums;

import tech.qiantong.qdata.common.utils.StringUtils;

/**
 * Quartz执行策略。
 */
public enum ScheduleExecutionTypeEnum {
    PARALLEL,
    SERIAL_WAIT,
    SERIAL_DISCARD,
    SERIAL_PRIORITY;

    public static ScheduleExecutionTypeEnum resolve(String executionType, String concurrent) {
        if (StringUtils.isNotBlank(executionType)) {
            for (ScheduleExecutionTypeEnum type : values()) {
                if (type.name().equalsIgnoreCase(executionType)) {
                    return type;
                }
            }
        }
        return "0".equals(concurrent) ? PARALLEL : SERIAL_WAIT;
    }

    public boolean shouldUseDisallowConcurrentJob() {
        return this == SERIAL_WAIT;
    }
}
