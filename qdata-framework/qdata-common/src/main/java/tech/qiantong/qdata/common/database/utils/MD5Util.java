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

package tech.qiantong.qdata.common.database.utils;

import org.apache.commons.collections4.MapUtils;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.constants.DbType;
import tech.qiantong.qdata.common.utils.StringUtils;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MD5Util {
    private static final String ORACLE_SERVICE_NAME = "ORACLE_SERVICE_NAME"; // The default condition type is "NONE"
    private static final String ORACLE_SID = "ORACLE_SID"; // The default condition type is "NONE"


    public static void main(String[] args) throws InterruptedException {
        Object[] arr = new Object[]{"dbName"};
        Object[] objects = Arrays.copyOf(arr, arr.length + 2);
        System.out.println(objects.length);
        int length = arr.length;
        objects[length] = 1;
        objects[length + 1] = 2;
        System.out.println(Arrays.toString(objects));
//        String encrypt = MD5Util.encrypt("sql" + ":" + Arrays.toString(arr));
//        System.out.println(encrypt);
    }

    private static final char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * MD5 encryption
     */
    public static String encrypt(String value) {
        return encrypt(value.getBytes());
    }

    /**
     * MD5 encryption
     */
    public static String encrypt(byte[] value) {
        try {
            byte[] bytes = MessageDigest.getInstance("MD5").digest(value);
            char[] chars = new char[32];
            for (int i = 0; i < chars.length; i = i + 2) {
                byte b = bytes[i / 2];
                chars[i] = HEX_CHARS[(b >>> 0x4) & 0xf];
                chars[i + 1] = HEX_CHARS[b & 0xf];
            }
            return new String(chars);
        } catch (Exception e) {
            throw new RuntimeException("md5 encrypt error", e);
        }
    }


    /**
     * Convert a string to type Long. If the string is empty or cannot be converted, 0L is returned.
     *
     * @param dataLength The string to be converted
     * @return converted Long type value
     */
    public static Long getStringToLong(String dataLength) {
        if (StringUtils.isEmpty(dataLength)) {
            return 0L;
        }
        try {
            return Long.parseLong(dataLength);
        } catch (NumberFormatException e) {
            // If the conversion fails, 0L is returned
            return 0L;
        }
    }

    /**
     * Escape single quotes in strings to avoid errors when splicing SQL
     */
    public static String escapeSingleQuotes(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("'", "''");
    }

    /**
     * If the input string is all lowercase, it will be converted to uppercase and returned; otherwise, the original string will be returned directly.
     *
     * @param input input string
     * @return If it is all lowercase, return all uppercase string; otherwise return the original string
     */
    public static String convertIfLowercase(String input) {
        if (input == null) {
            return null;
        }
        // If the string is the same as its lowercase counterpart, it means all lowercase
        if (input.equals(input.toLowerCase())) {
            return input.toUpperCase();
        }
        return input;
    }

    /**
     * Normalized database type
     *
     * @param dbType database type, supports: MySql, Oracle11, Oracle, DM8, Kingbase8
     * @return Standardized database type, return value is MYSQL, ORACLE, DM8, KINGBASE, Oracle11 also returns ORACLE
     * @throws IllegalArgumentException thrown when dbType is null or empty string
     */
    public static String getNormalizedDbType(String dbType) {
        if (dbType == null || dbType.isEmpty()) {
            throw new IllegalArgumentException("数据库类型不能为空");
        }
        if (DbType.MYSQL.getDb().equals(dbType)) {
            return "MYSQL";
        } else if (DbType.ORACLE.getDb().equals(dbType) || DbType.ORACLE_12C.getDb().equals(dbType)) {
            return "ORACLE";
        } else if (DbType.DM8.getDb().equals(dbType)) {
            return "DAMENG";
        } else if (DbType.KINGBASE8.getDb().equals(dbType)) {
            return "KINGBASE";
        }
        // Returns primitive type value by default
        return dbType;
    }


    /**
     * Create object
     *
     * @param datasource
     * @return
     */
    public static DbQueryProperty buildJobDatasource(Map<String, Object> datasource) {
        String ip = MapUtils.getString(datasource, "ip");
        long port = MapUtils.getLong(datasource, "port");
        String datasourceConfig = MapUtils.getString(datasource, "datasourceConfig");
        String datasourceType = MapUtils.getString(datasource, "datasourceType");

        DbQueryProperty dbQueryProperty = new DbQueryProperty(datasourceType
                , ip, port, datasourceConfig);
        return dbQueryProperty;
    }

    /**
     * @param dbQueryProperty
     * @return
     */
    public static String wrapDsDatabaseParams(DbQueryProperty dbQueryProperty) {
        String dbType = dbQueryProperty.getDbType();
        if (DbType.ORACLE.getDb().equals(dbType) || DbType.ORACLE_12C.getDb().equals(dbType)) {
            return dbQueryProperty.getSid();
        }
        return dbQueryProperty.getDbName();
    }

    public static String wrapDsConnectTypeParams(DbQueryProperty dbQueryProperty) {
        String dbType = dbQueryProperty.getDbType();
        if (DbType.ORACLE.getDb().equals(dbType) || DbType.ORACLE_12C.getDb().equals(dbType)) {
            return ORACLE_SERVICE_NAME;
        }
        return ORACLE_SERVICE_NAME;
    }

    public static Map wrapOtherParams(DbQueryProperty dbQueryProperty) {
        Map<String, Object> map = new HashMap<>();
        String dbType = dbQueryProperty.getDbType();
        if (DbType.ORACLE.getDb().equals(dbType) || DbType.ORACLE_12C.getDb().equals(dbType)) {
            map.put("schema", dbQueryProperty.getDbName());
        }
        return map;
    }
}
