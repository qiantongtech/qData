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
import tech.qiantong.qdata.common.database.utils.MD5Util;

/**
 * 人大金仓数据库字段类型枚举
 */
@Getter
public enum KingbaseColumnTypeEnum {
    TINYINT("TINYINT", DmColumnTypeEnum.TINYINT),
    SMALLINT("SMALLINT", DmColumnTypeEnum.TINYINT),
    INTEGER("INTEGER", DmColumnTypeEnum.INTEGER),
    INT("INT", DmColumnTypeEnum.INTEGER),
    BIGINT("BIGINT", DmColumnTypeEnum.BIGINT),
    DECIMAL("DECIMAL", DmColumnTypeEnum.DECIMAL),
    NUMERIC("NUMERIC", DmColumnTypeEnum.NUMERIC),
    FLOAT("FLOAT", DmColumnTypeEnum.FLOAT),
    DOUBLE("DOUBLE", DmColumnTypeEnum.DOUBLE),
    NUMBER("NUMBER", DmColumnTypeEnum.NUMBER),
    CHAR("CHAR", DmColumnTypeEnum.CHAR),
    VARCHAR("VARCHAR", DmColumnTypeEnum.VARCHAR),
    VARCHAR2("VARCHAR2", DmColumnTypeEnum.VARCHAR2),
    TEXT("TEXT", DmColumnTypeEnum.TEXT),
    CLOB("CLOB", DmColumnTypeEnum.TEXT),
    DATE("DATE", DmColumnTypeEnum.DATE),
    TIMESTAMP("TIMESTAMP", DmColumnTypeEnum.TIMESTAMP),
    DATETIME("DATETIME", DmColumnTypeEnum.DATETIME);

    private final String type;
    private final DmColumnTypeEnum dmType;

    KingbaseColumnTypeEnum(String type, DmColumnTypeEnum dmType) {
        this.type = type;
        this.dmType = dmType;
    }

    /**
     * 将人大金仓类型转换为达梦类型
     */
    public static String convertToDmType(String type) {
        String kingbaseType = MD5Util.convertIfLowercase(type);
        kingbaseType = kingbaseType.replaceAll("\\(.*\\)", "").trim().toUpperCase();
        for (KingbaseColumnTypeEnum typeEnum : values()) {
            if (typeEnum.getType().equals(kingbaseType)) {
                return typeEnum.getDmType().getType();
            }
        }
        return kingbaseType;
//        return DmColumnTypeEnum.VARCHAR.getType(); // 默认转为VARCHAR
    }
}
