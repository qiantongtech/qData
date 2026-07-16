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
 * Cleaning Rule Type Enum
 */
@Getter
@AllArgsConstructor
public enum CleanRuleTypeEnum {

    STRING_TRANSFORM("1", "字符串转化"),
    NUMBER_PROCESS("2", "数值处理"),
    TIME_PROCESS("3", "时间处理"),
    DUPLICATE_PROCESS("4", "重复值处理"),
    NULL_PROCESS("5", "空值处理");

    private final String type;
    private final String name;

    public static String getNameByType(String type) {
        for (CleanRuleTypeEnum value : CleanRuleTypeEnum.values()) {
            if (value.getType().equals(type)) {
                return value.getName();
            }
        }
        return type;
    }
}
