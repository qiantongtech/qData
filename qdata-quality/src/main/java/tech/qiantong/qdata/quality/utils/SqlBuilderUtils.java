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

package tech.qiantong.qdata.quality.utils;

import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.quality.dal.dataobject.quality.QualityRuleEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SqlBuilderUtils {

    /**
     * Construct an equality comparison statement of AND concatenation: such as t.col1 = t2.col1 AND t.col2 = t2.col2
     *
     * @param columns field list
     * @param leftAlias left table alias (such as t)
     * @param rightAlias Right table alias (such as t2)
     * @return spliced SQL conditional statement
     */
    public static String buildAndEquals(List<String> columns, String leftAlias, String rightAlias) {
        if (columns == null || columns.isEmpty()) {
            return "";
        }
        return columns.stream()
                .map(col -> String.format("%s.%s = %s.%s", leftAlias, col, rightAlias, col))
                .collect(Collectors.joining(" AND "));
    }

    private SqlBuilderUtils() {
        // Utility classes do not allow instantiation
        throw new UnsupportedOperationException("SqlBuilderUtils should not be instantiated");
    }

    /**
     * Flip the operator to construct error conditions (if configured as <=, the error is >)
     */
    public static String reverseOperator(String op) {
        switch (op) {
            case "<":
                return ">=";
            case "<=":
                return ">";
            default:
                throw new IllegalArgumentException("Unsupported operator: " + op);
        }
    }
    /**
     * Convert the object to Boolean, supporting common forms such as "1"/"0", 1/0, true/false
     *
     * @param value original value
     * @return Boolean.TRUE means yes; Boolean.FALSE means no; null means unrecognized
     */
    public static Boolean parseBoolean(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Boolean) {
            return (Boolean) value;
        }

        String str = value.toString().trim();
        if ("1".equals(str) || "true".equalsIgnoreCase(str)) {
            return Boolean.TRUE;
        } else if ("0".equals(str) || "false".equalsIgnoreCase(str)) {
            return Boolean.FALSE;
        }

        return null;
    }

    public static String addOracle11gPagination(String sqlQuery, int limit, int offset) {
        // Remove any existing semicolon and trim whitespace
        String trimmedQuery = sqlQuery.replace(";", "").trim();

        // Oracle 11g pagination using ROWNUM
        if (offset == 0) {
            // Simple case for first page (no offset)
            return String.format(
                    "SELECT * FROM (%s) WHERE ROWNUM <= %d",
                    trimmedQuery, limit
            );
        } else {
            // General case with offset
            return String.format(
                    "SELECT * FROM (" +
                            "  SELECT a.*, ROWNUM rnum FROM (%s) a" +
                            "  WHERE ROWNUM <= %d" +
                            ") WHERE rnum > %d",
                    trimmedQuery, offset + limit, offset
            );
        }
    }

    public static String addSqlServerPagination(String sqlQuery, int limit, int offset) {
        // Remove trailing semicolon and trim whitespace
        String trimmedQuery = sqlQuery.replace(";", "").trim();

        if (offset == 0) {
            // Home page: Use TOP directly
            return String.format(
                    "SELECT TOP (%d) * FROM (%s) AS t",
                    limit, trimmedQuery
            );
        } else {
            // General: ROW_NUMBER paging (use (SELECT 1) placeholder for scenarios that do not rely on ORDER BY)
            return String.format(
                    "SELECT * FROM (\n" +
                            "  SELECT t.*, ROW_NUMBER() OVER (ORDER BY (SELECT 1)) AS rn\n" +
                            "  FROM (%s) AS t\n" +
                            ") AS x\n" +
                            "WHERE x.rn > %d AND x.rn <= %d",
                    trimmedQuery, offset, offset + limit
            );
        }
    }

    @SuppressWarnings("unchecked")
    public String generateTimeOrderValidationValidDataSql(QualityRuleEntity rule, int limit, int offset) {
        Map<String, Object> ruleConfig = rule.getConfig();
        boolean allowPartialNull = SqlBuilderUtils.parseBoolean(ruleConfig.get("allowPartialNull"));
        List<Map<String, String>> conditions = (List<Map<String, String>>) rule.getConfig().get("conditions");
        String table = rule.getTableName();
        String whereClause = rule.getWhereClause();

        String frag;
        if (allowPartialNull) {
            frag = conditions.stream()
                    .map(c -> {
                        String l = c.get("leftField");
                        String r = c.get("rightField");
                        String op = c.get("operator"); // For example <= / < / >= / >
                        return String.format("(%s IS NULL OR %s IS NULL OR %s %s %s)", l, r, l, op, r);
                    })
                    .collect(Collectors.joining(" AND "));
        } else {
            frag = conditions.stream()
                    .map(c -> String.format("%s %s %s", c.get("leftField"), c.get("operator"), c.get("rightField")))
                    .collect(Collectors.joining(" AND "));
        }

        StringBuilder sql = new StringBuilder()
                .append("SELECT * FROM ").append(table)
                .append(" WHERE (").append(frag).append(")");

        if (StringUtils.isNotEmpty(whereClause)) {
            sql.append(" AND (").append(whereClause).append(")");
        }

        // SQL Server paging: ORDER BY is required; use ORDER BY (SELECT 1) when there is no fixed field
        sql.append(" ORDER BY (SELECT 1)")
                .append(" OFFSET ").append(offset).append(" ROWS")
                .append(" FETCH NEXT ").append(limit).append(" ROWS ONLY");

        return sql.toString();
    }
}
