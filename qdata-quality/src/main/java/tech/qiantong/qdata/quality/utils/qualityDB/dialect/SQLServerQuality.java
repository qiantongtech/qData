package tech.qiantong.qdata.quality.utils.qualityDB.dialect;

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
        // SQL Server 用 PATINDEX，返回匹配位置，>0 表示匹配成功
        return String.format("PATINDEX('%s', %s) > 0", pattern, column);
    }

    @Override
    public String fragDecimalPrecision(QualityRuleEntity rule) {
        String column = rule.getRuleColumn();
        Map<String, Object> ruleConfig = rule.getConfig();
        boolean skipInteger = SqlBuilderUtils.parseBoolean(ruleConfig.get("skipInteger"));
        int scale = Integer.parseInt(String.valueOf(ruleConfig.get("scale")));

        // 基础：四舍五入到 scale 位不改变值 => 小数位不超过 scale
        String base = String.format("%s = ROUND(%s, %d)", column, column, scale);

        // 若不允许整数（必须有小数），可加一个“有小数部分”的限制
        if (!skipInteger) {
            // 有小数部分：与取整后不同
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

        // SQL Server 分页：必须有 ORDER BY；无特定字段时用 ORDER BY (SELECT 1)
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
            throw new IllegalArgumentException("组合唯一性校验需要至少一个字段");
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
            throw new IllegalArgumentException("组合唯一性校验需要至少一个字段");
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

}
