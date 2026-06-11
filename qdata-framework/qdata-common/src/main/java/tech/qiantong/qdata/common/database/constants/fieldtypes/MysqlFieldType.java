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

package tech.qiantong.qdata.common.database.constants.fieldtypes;

/**
 * MySQL 数据库支持的字段类型枚举
 */
public enum MysqlFieldType {
    VARCHAR("VARCHAR"),
    CHAR("CHAR"),
    // MySQL 一般采用 INT/DECIMAL 来代替 NUMBER
    INT("INT"),
    DECIMAL("DECIMAL"),
    DATE("DATE"),
    DATETIME("DATETIME"),
    TIMESTAMP("TIMESTAMP"),
    // 使用 TEXT 表示大文本类型
    TEXT("TEXT");

    private final String type;

    MysqlFieldType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
