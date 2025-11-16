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

import lombok.Getter;
import tech.qiantong.qdata.common.database.utils.MD5Util;

/**
 * MySQL数据库字段类型枚举
 */
@Getter
public enum MySqlColumnTypeEnum {
    TINYINT("TINYINT", DmColumnTypeEnum.TINYINT),
    SMALLINT("SMALLINT", DmColumnTypeEnum.TINYINT),
    MEDIUMINT("MEDIUMINT", DmColumnTypeEnum.INTEGER),
    INT("INT", DmColumnTypeEnum.INTEGER),
    INTEGER("INTEGER", DmColumnTypeEnum.INTEGER),
    BIGINT("BIGINT", DmColumnTypeEnum.BIGINT),
    DECIMAL("DECIMAL", DmColumnTypeEnum.DECIMAL),
    NUMERIC("NUMERIC", DmColumnTypeEnum.NUMERIC),
    FLOAT("FLOAT", DmColumnTypeEnum.FLOAT),
    DOUBLE("DOUBLE", DmColumnTypeEnum.DOUBLE),
    REAL("REAL", DmColumnTypeEnum.DOUBLE),
    CHAR("CHAR", DmColumnTypeEnum.CHAR),
    VARCHAR("VARCHAR", DmColumnTypeEnum.VARCHAR2),
    TINYTEXT("TINYTEXT", DmColumnTypeEnum.TEXT),
    TEXT("TEXT", DmColumnTypeEnum.TEXT),
    MEDIUMTEXT("MEDIUMTEXT", DmColumnTypeEnum.TEXT),
    LONGTEXT("LONGTEXT", DmColumnTypeEnum.TEXT),
    DATE("DATE", DmColumnTypeEnum.DATE),
    TIME("TIME", DmColumnTypeEnum.TIMESTAMP),
    TIMESTAMP("TIMESTAMP", DmColumnTypeEnum.TIMESTAMP),
    DATETIME("DATETIME", DmColumnTypeEnum.DATETIME);

    private final String type;
    private final DmColumnTypeEnum dmType;

    MySqlColumnTypeEnum(String type, DmColumnTypeEnum dmType) {
        this.type = type;
        this.dmType = dmType;
    }

    /**
     * 将MySQL类型转换为达梦类型
     */
    public static String convertToDmType(String type) {
        String mysqlType = MD5Util.convertIfLowercase(type);
        mysqlType = mysqlType.replaceAll("\\(.*\\)", "").trim().toUpperCase();
        for (MySqlColumnTypeEnum typeEnum : values()) {
            if (typeEnum.getType().equals(mysqlType)) {
                return typeEnum.getDmType().getType();
            }
        }
        return mysqlType;
//        return DmColumnTypeEnum.VARCHAR.getType(); // 默认转为VARCHAR
    }
}
