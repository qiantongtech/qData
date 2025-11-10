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

package tech.qiantong.qdata.module.att.dal.dataobject.rule.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 规则类型枚举
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
