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

package tech.qiantong.qdata.common.enums;

import lombok.Getter;

/**
 * 达梦数据库字段类型枚举
 */
@Getter
public enum DmColumnTypeEnum {
    TINYINT("TINYINT"),
    INTEGER("INTEGER"),
    BIGINT("BIGINT"),
    DECIMAL("DECIMAL"),
    NUMERIC("NUMERIC"),
    FLOAT("FLOAT"),
    DOUBLE("DOUBLE"),
    NUMBER("NUMBER"),
    CHAR("CHAR"),
    VARCHAR("VARCHAR"),
    VARCHAR2("VARCHAR2"),
    TEXT("TEXT"),
    DATE("DATE"),
    TIMESTAMP("TIMESTAMP"),
    DATETIME("DATETIME");

    private final String type;

    DmColumnTypeEnum(String type) {
        this.type = type;
    }
}
