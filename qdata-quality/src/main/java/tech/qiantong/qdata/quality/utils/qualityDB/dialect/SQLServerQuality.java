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

package tech.qiantong.qdata.quality.utils.qualityDB.dialect;
import tech.qiantong.qdata.common.exception.ServiceException;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.quality.dal.dataobject.quality.QualityRuleEntity;
import tech.qiantong.qdata.quality.utils.SqlBuilderUtils;
import tech.qiantong.qdata.quality.utils.qualityDB.ComponentItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SQLServerQuality implements ComponentItem {


    @Override
    public String addPagination(String sql, int limit, int offset) {
        return SqlBuilderUtils.addSqlServerPagination(sql, limit, offset);
    }

    @Override
    public String fragCharacter(QualityRuleEntity rule) {
        String column = rule.getRuleColumn();
        String pattern = (String) rule.getConfig().get("regex");
        // SQL Server uses PATINDEX to return the matching position, >0 indicates successful matching
        return String.format("PATINDEX('%s', %s) > 0", pattern, column);
    }

    @Override
    public String fragDecimalPrecision(QualityRuleEntity rule) {
        String column = rule.getRuleColumn();
        Map<String, Object> ruleConfig = rule.getConfig();
        boolean skipInteger = SqlBuilderUtils.parseBoolean(ruleConfig.get("skipInteger"));
        int scale = Integer.parseInt(String.valueOf(ruleConfig.get("scale")));

        // Basics: rounding to scale does not change the value => decimal places do not exceed scale
        String base = String.format("%s = ROUND(%s, %d)", column, column, scale);

        // If integers are not allowed (must have decimals), you can add a restriction of "has a decimal part"
        if (!skipInteger) {
            // There is a decimal part: different from after rounding
            String hasFraction = String.format("%s <> FLOOR(%s)", column, column);
            base = String.format("(%s AND %s)", hasFraction, base);
        }
        return base;
    }

    @SuppressWarnings("unchecked")
    public String generateGroupFieldCompletenessValidDataSql(QualityRuleEntity rule, int limit, int offset) {
        List<String> columns = rule.getRuleColumns();
        Map<String, Object> ruleConfig = rule.getConfig();
        int fillStrategy = MapUtils.getIntValue(ruleConfig, "fillStrategy", 1);
        String table = rule.getTableName();
        String whereClause = rule.getWhereClause();

        String okExpr = fragFieldCompleteness(columns, fillStrategy);

        StringBuilder sql = new StringBuilder()
                .append("SELECT * FROM ").append(table)
                .append(" WHERE (").append(okExpr).append(")");

        if (StringUtils.isNotEmpty(whereClause)) {
            sql.append(" AND (").append(whereClause).append(")");
        }

        // SQL Server paging: ORDER BY is required; use ORDER BY (SELECT 1) when there is no specific field
        sql.append(" ORDER BY (SELECT 1)")
                .append(" OFFSET ").append(offset).append(" ROWS")
                .append(" FETCH NEXT ").append(limit).append(" ROWS ONLY");

        return sql.toString();
    }

    public String fragLength(QualityRuleEntity rule) {
        String column = rule.getRuleColumn();
        Map<String, Object> ruleConfig = rule.getConfig();
        Integer min = MapUtils.getInteger(ruleConfig, "minLength");
        Integer max = MapUtils.getInteger(ruleConfig, "maxLength");
        List<String> tmp = new ArrayList<>();
        if (min != null) {
            tmp.add(String.format("LEN(%s)>=%d", column, min));
        }
        if (max != null) {
            tmp.add(String.format("LEN(%s)<=%d", column, max));
        }
        return String.join(" and ", tmp);
    }

    public String generateCompositeUniquenessValidationErrorSql(QualityRuleEntity rule) {
        String table = rule.getTableName();
        List<String> columns = rule.getRuleColumns();
        if (columns == null || columns.isEmpty()) {
            throw new ServiceException("quality.error.unique.fields", "组合唯一性校验需要至少一个字段");
        }
        String baseWhereClause = rule.getWhereClause();

        String nullSafeEq = buildNullSafeEquals(columns, "t", "s");
        String groupByCols = columns.stream().map(c -> "s." + c)
                .collect(java.util.stream.Collectors.joining(", "));

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT t.* FROM ").append(table).append(" t ");
        if (StringUtils.isNotBlank(baseWhereClause)) {
            sql.append("WHERE ").append(baseWhereClause).append(" AND ");
        } else {
            sql.append("WHERE ");
        }
        sql.append("EXISTS ( ")
                .append("  SELECT 1 FROM ").append(table).append(" s ");
        if (StringUtils.isNotBlank(baseWhereClause)) {
            sql.append(" WHERE ").append(baseWhereClause).append(" AND ");
        } else {
            sql.append(" WHERE ");
        }
        sql.append(nullSafeEq).append(" ")
                .append(" GROUP BY ").append(groupByCols).append(" ")
                .append(" HAVING COUNT(*) > 1 ")
                .append(")");
        return sql.toString();
    }

    public String generateCompositeUniquenessValidationValidDataSql(QualityRuleEntity rule, int limit, int offset) {
        String table = rule.getTableName();
        List<String> columns = rule.getRuleColumns();
        if (columns == null || columns.isEmpty()) {
            throw new ServiceException("quality.error.unique.fields", "组合唯一性校验需要至少一个字段");
        }
        String baseWhereClause = rule.getWhereClause();

        String nullSafeEq = buildNullSafeEquals(columns, "t", "s");
        String groupByCols = columns.stream().map(c -> "s." + c)
                .collect(java.util.stream.Collectors.joining(", "));

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT t.* FROM ").append(table).append(" t ");
        if (StringUtils.isNotBlank(baseWhereClause)) {
            sql.append("WHERE ").append(baseWhereClause).append(" AND ");
        } else {
            sql.append("WHERE ");
        }
        sql.append("EXISTS ( ")
                .append("  SELECT 1 FROM ").append(table).append(" s ");
        if (StringUtils.isNotBlank(baseWhereClause)) {
            sql.append(" WHERE ").append(baseWhereClause).append(" AND ");
        } else {
            sql.append(" WHERE ");
        }
        sql.append(nullSafeEq).append(" ")
                .append(" GROUP BY ").append(groupByCols).append(" ")
                .append(" HAVING COUNT(*) = 1 ")
                .append(")");

        return addPagination(sql.toString(), limit, offset);
    }



    private static String buildNullSafeEquals(List<String> columns, String leftAlias, String rightAlias) {
        return columns.stream()
                .map(c -> String.format("((%s.%s = %s.%s) OR (%s.%s IS NULL AND %s.%s IS NULL))",
                        leftAlias, c, rightAlias, c, leftAlias, c, rightAlias, c))
                .collect(java.util.stream.Collectors.joining(" AND "));
    }


    /**
     * SQL Server 2008: Generate string validation SQL for "Customer Input Data"
     * Only used for customer input data, click detection
     * Return 0 / 1
     */
    @Override
    public String generateValidDataCheckSql(QualityRuleEntity rule, String inputValue) {

        String regex = (String) rule.getConfig().get("regex");

        // Consistent with statistical SQL: regex → LIKE mode
        regex = regex.replace("^[", "%[^")
                .replace("]+$", "]%")
                .replace("/s", " ")
                .replace("[:punct:]", "!\"#$%&''()*+,-./:;<=>?@[\\]^_`{|}~");

//        boolean ignoreNull = SqlBuilderUtils.parseBoolean(
//                rule.getConfig().get("ignoreNullValue")
//        );

        // Input value (escaped single quotes)
        String valueExpr = "'" + inputValue.replace("'", "''") + "'";

        // Matching conditions (same semantics as table verification)
        String condition = String.format("%s LIKE '%s'", valueExpr, regex);

//        if (ignoreNull) {
//            condition = String.format(
//                    "%s IS NOT NULL AND %s <> '' AND %s",
//                    valueExpr, valueExpr, condition
//            );
//        }

        // Returns 0 / 1 (SQL Server does not require FROM dual)
        return String.format(
                "SELECT CASE WHEN %s THEN 1 ELSE 0 END AS valid_flag",
                condition
        );
    }

}
