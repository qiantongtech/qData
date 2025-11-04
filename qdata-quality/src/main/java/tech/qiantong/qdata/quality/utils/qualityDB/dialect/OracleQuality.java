package tech.qiantong.qdata.quality.utils.qualityDB.dialect;


import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.quality.dal.dataobject.quality.QualityRuleEntity;
import tech.qiantong.qdata.quality.utils.SqlBuilderUtils;
import tech.qiantong.qdata.quality.utils.qualityDB.ComponentItem;

import java.util.List;


public class OracleQuality implements ComponentItem {

    @Override
    public String addPagination(String sql, int limit, int offset) {
        return SqlBuilderUtils.addOracle11gPagination(sql, limit, offset);
    }

    public String generateCompositeUniquenessValidationSql(QualityRuleEntity rule) {
        String table = rule.getTableName();
        List<String> columns = rule.getRuleColumns();
        String whereClause = rule.getWhereClause();
        String groupByColumns = String.join(", ", columns);
        StringBuilder query = new StringBuilder();
        query.append("SELECT ")
                .append("(SELECT COUNT(*) FROM ").append(table);
        if (StringUtils.isNotEmpty(whereClause)) {
            query.append(" WHERE ").append(whereClause);
        }
        query.append(") AS totalCount, ")
                .append("(SELECT COALESCE(SUM(dup_count), 0) FROM ( ")
                .append("   SELECT ")
                .append(groupByColumns)
                .append(", COUNT(*) AS dup_count ")
                .append("   FROM ")
                .append(table);
        if (StringUtils.isNotEmpty(whereClause)) {
            query.append(" WHERE ").append(whereClause);
        }
        query.append(" GROUP BY ")
                .append(groupByColumns)
                .append(" HAVING COUNT(*) > 1")
                .append(")) AS errorCount ")
                .append("FROM dual");

        return query.toString();
    }

}
