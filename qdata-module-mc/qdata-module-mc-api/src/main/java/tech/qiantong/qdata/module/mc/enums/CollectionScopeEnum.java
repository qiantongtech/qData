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
 * Collection range enumeration
 *
 * @author qdata
 * @date 2026-04-27
 */
@Getter
@AllArgsConstructor
public enum CollectionScopeEnum {

    /**
     * All databases
     */
    ALL("2", "全部库"),

    /**
     * Custom library
     */
    CUSTOM("1", "自定义库");

    /**
     * Collection range value
     */
    private final String scope;

    /**
     * Collection range name
     */
    private final String name;

    /**
     * Get enum based on range value
     *
     * @param scope scope value
     * @return enumeration object
     */
    public static CollectionScopeEnum getByScope(String scope) {
        if (scope == null) {
            return null;
        }
        for (CollectionScopeEnum value : values()) {
            if (value.getScope().equals(scope)) {
                return value;
            }
        }
        return null;
    }

    /**
     * Determine whether it is all databases
     *
     * @param scope scope value
     * @return whether it is all databases
     */
    public static boolean isAll(String scope) {
        return ALL.getScope().equals(scope);
    }

    /**
     * Determine whether it is a custom library
     *
     * @param scope scope value
     * @return whether it is a custom library
     */
    public static boolean isCustom(String scope) {
        return CUSTOM.getScope().equals(scope);
    }
}
