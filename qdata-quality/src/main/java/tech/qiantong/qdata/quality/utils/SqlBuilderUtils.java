package tech.qiantong.qdata.quality.utils;

import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.quality.dal.dataobject.quality.QualityRuleEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SqlBuilderUtils {

    /**
     * 构建 AND 拼接的等值比较语句：如 t.col1 = t2.col1 AND t.col2 = t2.col2
     *
     * @param columns     字段列表
     * @param leftAlias   左表别名（如 t）
     * @param rightAlias  右表别名（如 t2）
     * @return 拼接后的 SQL 条件语句
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
        // 工具类不允许实例化
        throw new UnsupportedOperationException("SqlBuilderUtils should not be instantiated");
    }

    /**
     * 将操作符翻转，用于构造错误条件（如配置为 <=，错误就是 >）
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
     * 将对象转为 Boolean，支持 "1"/"0"、1/0、true/false 等常见形式
     *
     * @param value 原始值
     * @return Boolean.TRUE 表示是；Boolean.FALSE 表示否；null 表示无法识别
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
        // 去掉末尾分号并裁剪空白
        String trimmedQuery = sqlQuery.replace(";", "").trim();

        if (offset == 0) {
            // 首页：直接用 TOP
            return String.format(
                    "SELECT TOP (%d) * FROM (%s) AS t",
                    limit, trimmedQuery
            );
        } else {
            // 通用：ROW_NUMBER 分页（不依赖 ORDER BY 的场景用 (SELECT 1) 占位）
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
                        String op = c.get("operator"); // 例如 <= / < / >= / >
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

        // SQL Server 分页：必须带 ORDER BY；无固定字段时用 ORDER BY (SELECT 1)
        sql.append(" ORDER BY (SELECT 1)")
                .append(" OFFSET ").append(offset).append(" ROWS")
                .append(" FETCH NEXT ").append(limit).append(" ROWS ONLY");

        return sql.toString();
    }
}
