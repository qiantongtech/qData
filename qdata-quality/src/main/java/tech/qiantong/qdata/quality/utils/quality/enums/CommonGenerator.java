package tech.qiantong.qdata.quality.utils.quality.enums;

import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.quality.dal.dataobject.quality.QualityRuleEntity;
import tech.qiantong.qdata.quality.utils.SqlBuilderUtils;

public interface CommonGenerator {

    static boolean ignoreNull(QualityRuleEntity rule) {
        Boolean ignoreNull = SqlBuilderUtils.parseBoolean(rule.getConfig().get("ignoreNullValue"));
        return ignoreNull != null && ignoreNull;
    }

    static String generateSql(QualityRuleEntity rule, String frag) {
        String table = rule.getTableName();
        String column = rule.getRuleColumn();
        String whereClause = rule.getWhereClause();

        StringBuilder query = new StringBuilder()
                .append("SELECT COUNT(*) AS totalCount, ")
                .append("COUNT(CASE WHEN ");
        if (!ignoreNull(rule)) {
            query.append(column).append(" IS NULL OR ");
        }
        query.append("NOT (").append(frag).append(") THEN 1 END) AS errorCount ")
                .append("FROM ").append(table);
        if (StringUtils.isNotEmpty(whereClause)) {
            query.append(" WHERE ").append(whereClause);
        }
        return query.toString();
    }

    static String generateErrorSql(QualityRuleEntity rule, String frag) {
        String table = rule.getTableName();
        String column = rule.getRuleColumn();
        String whereClause = rule.getWhereClause();
        StringBuilder query = new StringBuilder()
                .append("SELECT * FROM ").append(table)
                .append(" WHERE (");
        if (!ignoreNull(rule)) {
            query.append(column).append(" IS NULL OR ");
        }
        query.append("NOT (").append(frag).append("))");
        if (StringUtils.isNotEmpty(whereClause)) {
            query.append(" AND ").append(whereClause);
        }
        return query.toString();
    }

    static String generateValidDataSql(QualityRuleEntity rule, String frag) {
        String table = rule.getTableName();
        String column = rule.getRuleColumn();
        String whereClause = rule.getWhereClause();

        StringBuilder query = new StringBuilder()
                .append("SELECT * FROM ").append(table)
                .append(" WHERE (");
        if (ignoreNull(rule)) {
            query.append(column).append(" IS NULL OR ");
        }
        query.append(frag).append(") ");
        if (StringUtils.isNotEmpty(whereClause)) {
            query.append(" AND ").append(whereClause);
        }
        return query.toString();
    }

}
