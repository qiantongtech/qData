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
 * Business License: https://docs.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.spark.etl.utils;

/**
 * String conversion tool class
 */
public class ValueParserUtils {
    /**
     * Safe parsing of int, illegal return of 0
     */
    public static int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Safely parse long, illegally return 0L
     */
    public static long parseLong(String value) {
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            return 0L;
        }
    }

    /**
     * Safely parses double, returns 0.0 illegally
     */
    public static double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return 0.0;
        }
    }

    /**
     * Safely parse boolean, support true/1 to be recognized as true, others as false
     */
    public static boolean parseBoolean(String value) {
        return "true".equalsIgnoreCase(value) || "1".equals(value);
    }

    /**
     * Escape string for regular matching
     * @param str
     * @return
     */
    public static String escapeLiteral(String str) {
        return str.replaceAll("([\\\\.^$|?*+()\\[\\]{}])", "\\\\$1");
    }


    /**
     * SQL string escaping methods
     * //Replace the single quote with two single quotes to prevent SQL errors.
     * @param str
     * @return
     */
    public static String escapeForSQL(String str) {
        if (str == null) return "";
        // Replace single quotes with two single quotes to prevent SQL errors.
        return str.replace("'", "''");
    }

}
