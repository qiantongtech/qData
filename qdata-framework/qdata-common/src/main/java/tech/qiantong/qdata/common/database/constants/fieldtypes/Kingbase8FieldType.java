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

package tech.qiantong.qdata.common.database.constants.fieldtypes;

/**
 * 人大金仓（Kingbase8）数据库支持的字段类型枚举
 */
public enum Kingbase8FieldType {
    VARCHAR("VARCHAR"),
    // 根据Kingbase8实际支持情况，可决定是否包含VARCHAR2
    VARCHAR2("VARCHAR2"),
    CHAR("CHAR"),
    NUMBER("NUMBER"),
    DATE("DATE"),
    TIMESTAMP("TIMESTAMP"),
    CLOB("CLOB");

    private final String type;

    Kingbase8FieldType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
