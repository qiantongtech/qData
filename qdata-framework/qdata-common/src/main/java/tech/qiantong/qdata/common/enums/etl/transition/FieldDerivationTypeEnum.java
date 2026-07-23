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

package tech.qiantong.qdata.common.enums.etl.transition;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FieldDerivationTypeEnum {

    FIELD_DERIVE_CONCAT("FIELD_DERIVE_CONCAT", "拼接"),
    FIELD_DERIVE_SUBSTRING("FIELD_DERIVE_SUBSTRING", "截取"),
    FIELD_DERIVE_REPLACE("FIELD_DERIVE_REPLACE", "替换"),
    FIELD_DERIVE_EXPRESSION("FIELD_DERIVE_EXPRESSION", "表达式"),
    FIELD_DERIVE_HASH("FIELD_DERIVE_HASH", "哈希"),
    FIELD_DERIVE_REGEX("FIELD_DERIVE_REGEX", "正则提取"),
    FIELD_DERIVE_CONSTANT("FIELD_DERIVE_CONSTANT", "常量赋值");

    @Getter
    private String code;
    @Getter
    private String description;

    public static FieldDerivationTypeEnum fromCode(String code) {
        for (FieldDerivationTypeEnum type : values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unsupported field derivation type code: " + code);
    }
}
