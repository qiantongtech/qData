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
 * For brand customization, please contact the official team for authorization.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

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
