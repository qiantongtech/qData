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

package tech.qiantong.qdata.module.da.utils;

import java.net.SocketTimeoutException;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.SQLTimeoutException;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.IdentityHashMap;
import java.util.Locale;
import java.util.Set;

/** Classifies query failures into stable frontend translation keys; never exposes driver details. */
public final class SqlQueryErrorMessages {
    public static final String OBJECT_MISSING = "da.dataQuery.errors.objectMissing";
    public static final String ACCESS_DENIED = "da.dataQuery.errors.accessDenied";
    public static final String SYNTAX_ERROR = "da.dataQuery.errors.syntaxError";
    public static final String TIMEOUT = "da.dataQuery.errors.timeout";
    public static final String EXECUTION_ERROR = "da.dataQuery.errors.executionError";

    private SqlQueryErrorMessages() { }

    /** Classifies a failure when the datasource type is unavailable. */
    public static String resolve(Throwable failure) {
        return resolve(failure, null);
    }

    /** Resolves a frontend translation key from the exception chain and stored datasource type. */
    public static String resolve(Throwable failure, String datasourceType) {
        String database = normalize(datasourceType);
        if (failure == null) {
            return EXECUTION_ERROR;
        }
        // Driver exceptions may be linked through both cause and nextException.
        // Track instances by identity so a repeated reference or cycle is visited only once.
        Set<Throwable> visited = Collections.newSetFromMap(new IdentityHashMap<Throwable, Boolean>());
        Deque<Throwable> pending = new ArrayDeque<>();
        pending.add(failure);
        boolean missing = false;
        boolean denied = false;
        boolean syntax = false;
        boolean timeout = false;
        while (!pending.isEmpty()) {
            Throwable current = pending.removeFirst();
            if (!visited.add(current)) {
                continue;
            }
            if (current.getCause() != null) {
                pending.addLast(current.getCause());
            }
            // Some drivers and client libraries use their own timeout exception classes.
            String type = current.getClass().getSimpleName();
            if (current instanceof SQLTimeoutException || current instanceof SocketTimeoutException
                    || type.contains("QueryTimeout") || type.contains("ConnectTimeout")) {
                timeout = true;
            }
            if (!(current instanceof SQLException)) {
                continue; // Wrapper messages can contain user SQL; never classify by their text.
            }
            SQLException sql = (SQLException) current;
            if (sql.getNextException() != null) {
                pending.addLast(sql.getNextException());
            }
            String state = sql.getSQLState() == null ? "" : sql.getSQLState().toUpperCase(Locale.ROOT);
            String message = sql.getMessage() == null ? "" : sql.getMessage().toLowerCase(Locale.ROOT);
            // Prefer driver codes, then SQLState, and consult message text as a fallback.
            String classified = vendorMessage(database, sql.getErrorCode(), state);
            if (classified == null) {
                classified = standardMessage(state);
            }
            if (classified == null) {
                classified = describeDriverError(database, sql, message);
            }
            timeout |= TIMEOUT.equals(classified);
            denied |= ACCESS_DENIED.equals(classified);
            missing |= OBJECT_MISSING.equals(classified);
            syntax |= SYNTAX_ERROR.equals(classified);
        }
        // If the chain yields several categories, use this fixed priority regardless of traversal order.
        if (timeout) {
            return TIMEOUT;
        }
        if (denied) {
            return ACCESS_DENIED;
        }
        if (missing) {
            return OBJECT_MISSING;
        }
        return syntax ? SYNTAX_ERROR : EXECUTION_ERROR;
    }

    /** Normalizes stored datasource names such as SQL_Server2008 to lookup keys. */
    private static String normalize(String type) {
        return type == null ? "" : type.toLowerCase(Locale.ROOT).replaceAll("[ _-]", "");
    }

    /** Numeric codes are scoped to the persisted database type, never the request's label. */
    private static String vendorMessage(String database, int code, String state) {
        switch (database) {
            case "mysql":
            case "mariadb":
            case "2": // DbType.MARIADB's persisted value
                if (oneOf(code, 1049, 1051, 1109, 1146)) return OBJECT_MISSING;
                if (oneOf(code, 1044, 1142, 1143, 1227)) return ACCESS_DENIED;
                if (oneOf(code, 1064, 1149)) return SYNTAX_ERROR;
                if (oneOf(code, 1205, 3024)) return TIMEOUT;
                break;
            case "dm8":
                if (oneOf(code, -2103, -2106)) return OBJECT_MISSING;
                if (oneOf(code, -5515, -5516) || "01007".equals(state)) return ACCESS_DENIED;
                if (code == -2007) return SYNTAX_ERROR;
                if (code == -6407) return TIMEOUT;
                break;
            case "oracle":
            case "oracle11":
                if (code == 942) return OBJECT_MISSING;
                if (code == 1031) return ACCESS_DENIED;
                if (oneOf(code, 900, 903, 904, 905, 906, 907, 911, 917, 920, 921, 923, 933, 936)) {
                    return SYNTAX_ERROR;
                }
                if (oneOf(code, 12170, 12535)) return TIMEOUT;
                break;
            case "sqlserver":
            case "sqlserver2008":
                if (code == 208) return OBJECT_MISSING;
                if (oneOf(code, 229, 230)) return ACCESS_DENIED;
                if (oneOf(code, 102, 105, 156, 170)) return SYNTAX_ERROR;
                if (code == -2) return TIMEOUT;
                break;
            case "kingbase8":
            case "postgresql":
                // These drivers identify errors by SQLState instead of stable numeric codes.
                return standardMessage(state);
            case "doris":
                if (oneOf(code, 1049, 1051, 1109, 1146)) return OBJECT_MISSING;
                if (oneOf(code, 1044, 1142, 1221, 1222, 1223, 1224, 1225, 1227)) return ACCESS_DENIED;
                // Doris 1105/HY000 wraps many errors; neither it nor 1064 alone means syntax.
                break;
            default:
                break;
        }
        return null;
    }

    /** Maps selected SQLState values that identify an error category without a vendor code. */
    private static String standardMessage(String state) {
        if ("HYT00".equals(state) || "HYT01".equals(state)) return TIMEOUT;
        if ("42P01".equals(state) || "42S02".equals(state) || "3F000".equals(state)) {
            return OBJECT_MISSING;
        }
        if ("42501".equals(state)) return ACCESS_DENIED;
        if ("42601".equals(state)) return SYNTAX_ERROR;
        // PostgreSQL/Kingbase 57014 also means manual cancellation, so don't call it a timeout here.
        // 42000 combines syntax and access errors; it must never imply syntax by itself.
        return null;
    }

    /** Matches English and Chinese driver text as a fallback. Only the translation key is returned. */
    private static String describeDriverError(String database, SQLException sql, String message) {
        if (contains(message, "timed out", "timeout", "time out", "超时")) return TIMEOUT;
        if (contains(message, "permission denied", "permission was denied", "insufficient privilege",
                "insufficient permission", "no select privilege", "no query privilege", "command denied", "access denied;", "权限不足", "没有权限", "无访问权限")
                || (contains(message, "没有", "无权") && contains(message, "查询权限", "访问权限", "对象的权限"))) {
            return ACCESS_DENIED;
        }
        if (contains(message, "table or view does not exist", "table or view not found", "no such table",
                "unknown table", "invalid object name", "无效的表或者视图名", "无效的表或视图名", "无效的模式名")
                || (contains(message, "table", "relation", "view", "表", "视图", "关系")
                    && contains(message, "does not exist", "doesn't exist", "不存在"))) {
            return OBJECT_MISSING;
        }
        if (contains(message, "syntax error", "sql syntax", "parse error", "incorrect syntax",
                "mismatched input", "语法错误", "语法分析出错")) return SYNTAX_ERROR;
        // In Doris, Connector/J may classify a generic 1105/42000 as SQLSyntaxErrorException.
        if (!"doris".equals(database) && sql instanceof SQLSyntaxErrorException) return SYNTAX_ERROR;
        return null;
    }

    private static boolean oneOf(int code, int... candidates) {
        for (int candidate : candidates) {
            if (code == candidate) return true;
        }
        return false;
    }

    private static boolean contains(String text, String... fragments) {
        for (String fragment : fragments) {
            if (text.contains(fragment)) {
                return true;
            }
        }
        return false;
    }
}
