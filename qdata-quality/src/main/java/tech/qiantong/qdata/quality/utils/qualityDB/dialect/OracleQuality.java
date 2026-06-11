/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 */

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
