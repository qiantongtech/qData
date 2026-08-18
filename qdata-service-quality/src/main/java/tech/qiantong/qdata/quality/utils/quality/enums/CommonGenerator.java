package tech.qiantong.qdata.quality.utils.quality.enums;

import org.apache.commons.collections4.CollectionUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.quality.dal.dataobject.quality.ColumnExpression;
import tech.qiantong.qdata.quality.dal.dataobject.quality.QualityRuleEntity;

import java.util.ArrayList;
import java.util.List;

public interface CommonGenerator {

    static String generateSql(QualityRuleEntity rule, String frag) {
        String table = rule.getTableName();
        List<String> where = new ArrayList<>();
        String whereClause = rule.getWhereClause();
        List<ColumnExpression> preconditions = BeanUtils.toBean((List<?>) rule.getConfig().get("preconditions"), ColumnExpression.class);
        StringBuilder query = new StringBuilder()
                .append("SELECT COUNT(*) AS totalCount, ")
                .append("COUNT(CASE WHEN ");
        query.append("(").append(frag).append(") THEN 1 END) AS errorCount ")
                .append("FROM ").append(table);
        if (StringUtils.isNotBlank(whereClause)) {
            where.add(whereClause);
        }
        if (CollectionUtils.isNotEmpty(preconditions)) {
            where.add(ColumnExpression.toExpressions(preconditions));
        }
        if (!where.isEmpty()) {
            query.append(" WHERE (").append(String.join(") and (", where)).append(")");
        }
        return query.toString();
    }

    static String generateDataSql(QualityRuleEntity rule, String frag) {
        String table = rule.getTableName();
        String whereClause = rule.getWhereClause();
        List<ColumnExpression> preconditions = BeanUtils.toBean((List<?>) rule.getConfig().get("preconditions"), ColumnExpression.class);
        StringBuilder query = new StringBuilder()
                .append("SELECT * FROM ").append(table)
                .append(" WHERE (").
                append(frag)
                .append(")");
        if (StringUtils.isNotBlank(whereClause)) {
            query.append(" AND (").append(whereClause).append(")");
        }
        if (CollectionUtils.isNotEmpty(preconditions)) {
            query.append(" AND (").append(ColumnExpression.toExpressions(preconditions)).append(")");
        }
        return query.toString();
    }

}
