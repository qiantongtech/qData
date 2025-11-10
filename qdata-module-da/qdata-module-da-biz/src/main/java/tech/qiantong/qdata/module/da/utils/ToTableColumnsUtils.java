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

package tech.qiantong.qdata.module.da.utils;

import lombok.Getter;
import tech.qiantong.qdata.common.database.core.DbColumn;

/**
 * <P>
 * 用途:生成临时表列（非关系型数据库）
 * </p>
 *
 * @author: FXB
 * @create: 2025-03-25 10:04
 **/
public class ToTableColumnsUtils {

    public enum Type {
        EXCEL(1),
        KAFKA(2);

        @Getter
        private Integer type;

        Type(Integer type) {
            this.type = type;
        }
    }


    /**
     * excel
     *
     * @param columnType
     * @param dbColumn
     */
    public static void excel(String columnType, DbColumn dbColumn) {
        switch (columnType) {
            case "long":
                dbColumn.setDataType("BIGINT");
                break;
            case "boolean":
                dbColumn.setDataType("VARCHAR2");
                dbColumn.setDataLength("1");
                break;
            case "double":
                dbColumn.setDataType("DECIMAL");
                dbColumn.setDataPrecision("20");
                dbColumn.setDataScale("10");
                break;
            case "string":
                dbColumn.setDataType("VARCHAR2");
                dbColumn.setDataLength("1000");
                break;
            case "date":
                dbColumn.setDataType("DATETIME");
                break;
        }
    }

    /**
     * kafka
     *
     * @param columnType
     * @param dbColumn
     */
    public static void kafka(String columnType, DbColumn dbColumn) {
        switch (columnType) {
            case "LONG":
                dbColumn.setDataType("BIGINT");
                break;
            case "BOOL":
                dbColumn.setDataType("VARCHAR2");
                dbColumn.setDataLength("10");
                break;
            case "DOUBLE":
                dbColumn.setDataType("DECIMAL");
                dbColumn.setDataPrecision("20");
                dbColumn.setDataScale("10");
                break;
            case "STRING":
                dbColumn.setDataType("VARCHAR2");
                dbColumn.setDataLength("1000");
                break;
            case "DATE":
                dbColumn.setDataType("DATETIME");
                break;
        }
    }
}
