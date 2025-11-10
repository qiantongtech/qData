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
 * For brand customization, please contact the official team for authorization.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
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
