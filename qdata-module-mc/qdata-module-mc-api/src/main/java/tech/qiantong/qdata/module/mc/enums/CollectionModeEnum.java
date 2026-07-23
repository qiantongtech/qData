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
 * Collection mode enumeration
 *
 * @author qdata
 * @date 2026-04-27
 */
@Getter
@AllArgsConstructor
public enum CollectionModeEnum {

    /**
     * Full collection
     */
    FULL("0", "全量采集"),

    /**
     * Incremental acquisition
     */
    INCREMENTAL("1", "增量采集");

    /**
     * Acquisition mode value
     */
    private final String mode;

    /**
     * Collection mode name
     */
    private final String name;

    /**
     * Get enum based on pattern value
     *
     * @param mode mode value
     * @return enumeration object
     */
    public static CollectionModeEnum getByMode(String mode) {
        if (mode == null) {
            return null;
        }
        for (CollectionModeEnum value : values()) {
            if (value.getMode().equals(mode)) {
                return value;
            }
        }
        return null;
    }

    /**
     * Determine whether it is full collection
     *
     * @param mode mode value
     * @return Whether it is full collection
     */
    public static boolean isFull(String mode) {
        return FULL.getMode().equals(mode);
    }

    /**
     * Determine whether it is incremental collection
     *
     * @param mode mode value
     * @return Whether it is incremental collection
     */
    public static boolean isIncremental(String mode) {
        return INCREMENTAL.getMode().equals(mode);
    }
}
