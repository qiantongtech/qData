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

package tech.qiantong.qdata.module.mc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Scheduler status enum
 *
 * @author qdata
 * @date 2026-05-11
 */
@Getter
@AllArgsConstructor
public enum SchedulerStatusEnum {

    /**
     * Disabled/offline status
     */
    DISABLED("0", "禁用"),

    /**
     * Enabled/online status
     */
    ENABLED("1", "启用");

    /**
     * Status value
     */
    private final String value;

    /**
     * Status name
     */
    private final String name;

    /**
     * Get enum based on status value
     *
     * @param value status value
     * @return enumeration object
     */
    public static SchedulerStatusEnum getByValue(String value) {
        if (value == null) {
            return null;
        }
        for (SchedulerStatusEnum status : values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        return null;
    }

    /**
     * Determine whether it is disabled
     *
     * @param value status value
     * @return whether it is disabled
     */
    public static boolean isDisabled(String value) {
        return DISABLED.getValue().equals(value);
    }

    /**
     * Determine whether it is enabled
     *
     * @param value status value
     * @return whether it is enabled
     */
    public static boolean isEnabled(String value) {
        return ENABLED.getValue().equals(value);
    }
}
