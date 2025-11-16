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
 * For brand customization, please apply for brand customization authorization via official channels.
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
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.common.enums;

public enum WhereType {

    EQUALS("1", "=", "等于"),
    NOT_EQUALS("2", "!=", "不等于"),
    LIKE("3", "LIKE", "全模糊查询"),
    LIKE_LEFT("4", "LIKE", "左模糊查询"),
    LIKE_RIGHT("5", "LIKE", "右模糊查询"),
    GREATER_THAN("6", ">", "大于"),
    GREATER_THAN_EQUALS("7", ">=", "大于等于"),
    LESS_THAN("8", "<", "小于"),
    LESS_THAN_EQUALS("9", "<=", "小于等于"),
    NULL("10", "IS NULL", "是否为空"),
    NOT_NULL("11", "IS NOT NULL", "是否不为空"),
    IN("12", "IN", "IN");

    private final String type;

    private final String key;

    private final String desc;

    WhereType(String type, String key, String desc) {
        this.type = type;
        this.key = key;
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public String getDesc() {
        return desc;
    }

    public static WhereType getWhereType(String whereType) {
        for (WhereType type : WhereType.values()) {
            if (type.type.equals(whereType)) {
                return type;
            }
        }
        return EQUALS;
    }
}
