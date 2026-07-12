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

package tech.qiantong.qdata.module.att.dal.dataobject.rule.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Rule Type Enum
 */
@Getter
@AllArgsConstructor
public enum RuleTypeEnum {

    CHARACTER_CHECK("1", "字符校验"),
    NUMBER_CHECK("2", "数值校验"),
    NULL_CHECK("3", "空值校验"),
    LENGTH_CHECK("4", "长度校验"),
    DUPLICATE_CHECK("5", "重复检查"),
    FORMAT_CHECK("6", "格式检查");

    private final String type;
    private final String name;

    public static String getNameByType(String type) {
        for (RuleTypeEnum value : RuleTypeEnum.values()) {
            if (value.getType().equals(type)) {
                return value.getName();
            }
        }
        return type;
    }
}
