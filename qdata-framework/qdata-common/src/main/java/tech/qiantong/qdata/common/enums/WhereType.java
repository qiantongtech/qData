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
