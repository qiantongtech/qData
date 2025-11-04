package tech.qiantong.qdata.quality.utils.qualityDB.dialect;


import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.quality.dal.dataobject.quality.QualityRuleEntity;
import tech.qiantong.qdata.quality.utils.SqlBuilderUtils;
import tech.qiantong.qdata.quality.utils.qualityDB.ComponentItem;

import java.util.ArrayList;
import java.util.List;

public class Oracle12cQuality implements ComponentItem {

    @Override
    public String generateCharacterValidationSql(QualityRuleEntity rule) {
        String table = rule.getTableName();
        String column = rule.getRuleColumn();
        String whereClause = rule.getWhereClause();
        String regex = (String) rule.getConfig().get("regex");
        boolean ignoreNull = SqlBuilderUtils.parseBoolean(rule.getConfig().get("ignoreNullValue"));

        String condition = String.format("NOT REGEXP_LIKE(%s, '%s')", column, regex);
        if (ignoreNull) {
            condition += String.format(" AND %s IS NOT NULL AND %s <> ''", column, column);
        }

        String tableCount = table ;
        if(StringUtils.isNotEmpty(whereClause)){
            condition = whereClause + " AND" + condition;
            tableCount = tableCount + " WHERE  " + whereClause;
        }

        return String.format(
                "SELECT COUNT(*) AS errorCount, (SELECT COUNT(*) FROM %s) AS totalCount FROM %s WHERE %s",
                tableCount, table, condition
        );
    }


    /**
     * 生成字符串类型校验的错误数据SQL
     * 规则编码：CHARACTER_VALIDATION
     *
     * 输出：错误明细
     */
    @Override
    public String generateCharacterValidationErrorSql(QualityRuleEntity rule) {
        String table = rule.getTableName();
        String column = rule.getRuleColumn();
        String whereClause = rule.getWhereClause();
        String regex = (String) rule.getConfig().get("regex");
        boolean ignoreNull = SqlBuilderUtils.parseBoolean(rule.getConfig().get("ignoreNullValue"));

        String condition = String.format("NOT REGEXP_LIKE(%s, '%s')", column, regex);
        if (ignoreNull) {
            condition += String.format(" AND %s IS NOT NULL AND %s <> ''", column, column);
        }

        if(StringUtils.isNotEmpty(whereClause)){
            condition = whereClause + " AND" + condition;
        }
        return String.format("SELECT * FROM %s WHERE %s", table, condition);
    }


    /**
     * 生成字符串类型校验的正常数据查询SQL（支持分页）
     * 规则编码：CHARACTER_VALIDATION
     *
     * 用于查询符合正则的数据明细
     *
     * @param rule  质量规则实体，包含表名、字段名、正则
     * @param limit 最大行数
     * @param offset 偏移量（从第几行开始）
     * @return SQL字符串
     */
    @Override
    public String generateCharacterValidationValidDataSql(QualityRuleEntity rule, int limit, int offset) {
        String table = rule.getTableName();
        String column = rule.getRuleColumn();
        String whereClause = rule.getWhereClause();
        String regex = (String) rule.getConfig().get("regex");
        boolean ignoreNull = SqlBuilderUtils.parseBoolean(rule.getConfig().get("ignoreNullValue"));

        String condition = String.format("REGEXP_LIKE(%s, '%s')", column, regex);
        if (ignoreNull) {
            condition += String.format(" AND %s IS NOT NULL AND %s <> ''", column, column);
        }
        if(StringUtils.isNotEmpty(whereClause)){
            condition = whereClause + " AND" + condition;
        }

        return String.format(
                "SELECT * FROM (" +
                        "  SELECT a.*, ROWNUM rn FROM (" +
                        "    SELECT * FROM %s WHERE %s" +
                        "  ) a WHERE ROWNUM <= %d" +
                        ") WHERE rn > %d",
                table, condition, offset + limit, offset
        );
    }


    @Override
    public String generateCompositeUniquenessValidationSql(QualityRuleEntity rule) {
        String table = rule.getTableName();
        List<String> columns = rule.getRuleColumns();
        String groupBy = String.join(", ", columns);
        String whereClause = rule.getWhereClause();

        String where = StringUtils.isNotEmpty(whereClause) ? " WHERE " + whereClause : "";

        return String.format(
                "SELECT COUNT(*) AS errorCount, " +
                        "       (SELECT COUNT(*) FROM %s%s) AS totalCount " +
                        "FROM (SELECT %s, COUNT(*) AS cnt FROM %s%s GROUP BY %s HAVING COUNT(*) > 1)",
                table, where, groupBy, table, where, groupBy
        );
    }

    @Override
    public String generateCompositeUniquenessValidationErrorSql(QualityRuleEntity rule) {
        String table = rule.getTableName();
        List<String> columns = rule.getRuleColumns();
        String groupBy = String.join(", ", columns);
        String whereClause = rule.getWhereClause();

        String where = StringUtils.isNotEmpty(whereClause) ? " WHERE " + whereClause : "";

        return String.format(
                "SELECT %s, COUNT(*) AS cnt FROM %s%s GROUP BY %s HAVING COUNT(*) > 1",
                groupBy, table, where, groupBy
        );
    }

    @Override
    public String generateCompositeUniquenessValidationValidDataSql(QualityRuleEntity rule, int limit, int offset) {
        String table = rule.getTableName();
        List<String> columns = rule.getRuleColumns();
        String colList = String.join(", ", columns);
        String whereClause = rule.getWhereClause();

        String where = StringUtils.isNotEmpty(whereClause) ? " WHERE " + whereClause : "";

        return String.format(
                "SELECT * FROM (" +
                        "  SELECT a.*, ROWNUM rn FROM (" +
                        "    SELECT * FROM %s t%s " +
                        "    WHERE NOT EXISTS ( " +
                        "      SELECT 1 FROM %s t2%s " +
                        "      GROUP BY %s " +
                        "      HAVING COUNT(*) > 1 AND %s" +
                        "    )" +
                        "  ) a WHERE ROWNUM <= %d" +
                        ") WHERE rn > %d",
                table, where,
                table, where,
                colList,
                SqlBuilderUtils.buildAndEquals(columns, "t", "t2"),
                offset + limit, offset
        );
    }


    /**
     * 数值精度校验 - 错误统计 SQL
     * 规则编码：DECIMAL_PRECISION_VALIDATION
     *
     * 检查小数点后超过指定精度的数量，统计错误总数 + 全部记录数。
     */
    @Override
    public String generateDecimalPrecisionValidationSql(QualityRuleEntity rule) {
        String table = rule.getTableName();
        String column = rule.getRuleColumn();
        int scale = Integer.parseInt(rule.getConfig().get("scale").toString());

        boolean ignoreNull = SqlBuilderUtils.parseBoolean(rule.getConfig().get("ignoreNullValue"));
        boolean skipInteger = SqlBuilderUtils.parseBoolean(rule.getConfig().get("skipInteger"));

        List<String> conditions = new ArrayList<>();
        conditions.add(String.format("INSTR(%s, '.') > 0", column));
        conditions.add(String.format("LENGTH(SUBSTR(%s, INSTR(%s, '.') + 1)) > %d", column, column, scale));
        if (ignoreNull) {
            conditions.add(String.format("%s IS NOT NULL AND %s <> ''", column, column));
        }
        if (skipInteger) {
            conditions.add(String.format("INSTR(%s, '.') > 0", column));
        }

        return String.format(
                "SELECT COUNT(*) AS errorCount, " +
                        "       (SELECT COUNT(*) FROM %s) AS totalCount " +
                        "FROM %s WHERE %s",
                table, table, String.join(" AND ", conditions)
        );
    }

    /**
     * 数值精度校验 - 错误明细 SQL
     * 规则编码：DECIMAL_PRECISION_VALIDATION
     *
     * 返回所有小数位数超出指定精度的记录。
     */
    @Override
    public String generateDecimalPrecisionValidationErrorSql(QualityRuleEntity rule) {
        String table = rule.getTableName();
        String column = rule.getRuleColumn();
        int scale = Integer.parseInt(rule.getConfig().get("scale").toString());

        boolean ignoreNull = SqlBuilderUtils.parseBoolean(rule.getConfig().get("ignoreNullValue"));
        boolean skipInteger = SqlBuilderUtils.parseBoolean(rule.getConfig().get("skipInteger"));

        List<String> conditions = new ArrayList<>();
        conditions.add(String.format("INSTR(%s, '.') > 0", column));
        conditions.add(String.format("LENGTH(SUBSTR(%s, INSTR(%s, '.') + 1)) > %d", column, column, scale));
        if (ignoreNull) {
            conditions.add(String.format("%s IS NOT NULL AND %s <> ''", column, column));
        }
        if (skipInteger) {
            conditions.add(String.format("INSTR(%s, '.') > 0", column));
        }

        return String.format(
                "SELECT * FROM %s WHERE %s",
                table, String.join(" AND ", conditions)
        );
    }

    /**
     * 数值精度校验 - 正常数据分页 SQL
     * 规则编码：DECIMAL_PRECISION_VALIDATION
     *
     * 返回所有符合小数精度要求的记录（不超过指定小数位数）。
     */
    @Override
    public String generateDecimalPrecisionValidationValidDataSql(QualityRuleEntity rule, int limit, int offset) {
        String table = rule.getTableName();
        String column = rule.getRuleColumn();
        int scale = Integer.parseInt(rule.getConfig().get("scale").toString());

        boolean ignoreNull = SqlBuilderUtils.parseBoolean(rule.getConfig().get("ignoreNullValue"));
        boolean skipInteger = SqlBuilderUtils.parseBoolean(rule.getConfig().get("skipInteger"));

        List<String> conditions = new ArrayList<>();
        conditions.add(String.format(
                "INSTR(%s, '.') = 0 OR LENGTH(SUBSTR(%s, INSTR(%s, '.') + 1)) <= %d",
                column, column, column, scale
        ));
        if (ignoreNull) {
            conditions.add(String.format("%s IS NOT NULL AND %s <> ''", column, column));
        }
        if (skipInteger) {
            conditions.add(String.format("INSTR(%s, '.') > 0", column));
        }

        return String.format(
                "SELECT * FROM (" +
                        "  SELECT a.*, ROWNUM rn FROM (" +
                        "    SELECT * FROM %s WHERE %s" +
                        "  ) a WHERE ROWNUM <= %d" +
                        ") WHERE rn > %d",
                table, String.join(" AND ", conditions), offset + limit, offset
        );
    }


}
