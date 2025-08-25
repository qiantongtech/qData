package tech.qiantong.qdata.quality.utils.qualityDB;

import org.apache.commons.collections4.MapUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.quality.dal.dataobject.quality.QualityRuleEntity;
import tech.qiantong.qdata.quality.utils.SqlBuilderUtils;
import tech.qiantong.qdata.quality.utils.quality.enums.CommonGenerator;
import tech.qiantong.qdata.quality.utils.qualityDB.dialect.QualityFragSql;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static tech.qiantong.qdata.quality.utils.quality.enums.CommonGenerator.*;

/**
 * <P>
 * 用途:数据质量sql
 * </p>
 *
 * @create: 2025-03-12 16:29
 **/
public interface ComponentItem extends QualityFragSql {

    default String addPagination(String sql, int limit, int offset) {
        return String.format("%s LIMIT %d OFFSET %d", sql, limit, offset);
    }

    /**
     * 生成字符串类型校验的SQL（如身份证只允许数字和X）
     * 规则编码：CHARACTER_VALIDATION
     * <p>
     * 输出：错误数据数 + 总数
     */
    default String generateCharacterValidationSql(QualityRuleEntity rule) {
        return generateSql(rule, fragCharacter(rule));
    }

    /**
     * 生成字符串类型校验的错误数据SQL
     * 规则编码：CHARACTER_VALIDATION
     * <p>
     * 输出：错误明细
     */
    default String generateCharacterValidationErrorSql(QualityRuleEntity rule) {
        return generateErrorSql(rule, fragCharacter(rule));
    }

    /**
     * 生成字符串类型校验的正常数据查询SQL（支持分页）
     * 规则编码：CHARACTER_VALIDATION
     * <p>
     * 用于查询符合正则的数据明细
     *
     * @param rule   质量规则实体，包含表名、字段名、正则
     * @param limit  最大行数
     * @param offset 偏移量（从第几行开始）
     * @return SQL字符串
     */
    default String generateCharacterValidationValidDataSql(QualityRuleEntity rule, int limit, int offset) {
        return addPagination(generateValidDataSql(rule, fragCharacter(rule)), limit, offset);
    }

    /**
     * 多字段组合唯一性校验 - 错误统计 SQL
     * 规则编码：COMPOSITE_UNIQUENESS_VALIDATION
     * <p>
     * 输出：组合字段重复数量 + 总记录数
     */
    default String generateCompositeUniquenessValidationSql(QualityRuleEntity rule) {
        String table = rule.getTableName();
        List<String> columns = rule.getRuleColumns();
        String whereClause = rule.getWhereClause();
        String groupByColumns = String.join(", ", columns);
        StringBuilder query = new StringBuilder();
        query.append("SELECT (select count(*) from ").append(table);
        if (StringUtils.isNotEmpty(whereClause)) {
            query.append(" WHERE ").append(whereClause);
        }
        query.append(") AS totalCount,")
                .append(" COALESCE(SUM(dup_count),0) AS errorCount")
                .append(" FROM ( ")
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
                .append(" HAVING COUNT(*)>1")
                .append(") AS grouped_data");
        return query.toString();
    }

    /**
     * 多字段组合唯一性校验 - 错误明细 SQL
     * 规则编码：COMPOSITE_UNIQUENESS_VALIDATION
     * <p>
     * 输出：重复组合的记录
     */
    default String generateCompositeUniquenessValidationErrorSql(QualityRuleEntity rule) {
        String table = rule.getTableName();
        List<String> columns = rule.getRuleColumns();
        String colList = String.join(", ", columns);
        String baseWhereClause = rule.getWhereClause();
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM ").append(table)
                .append(" WHERE ");
        if (StringUtils.isNotEmpty(baseWhereClause)) {
            query.append(baseWhereClause).append(" AND ");
        }
        query.append("(").append(colList)
                .append(") IN (SELECT ").append(colList)
                .append(" FROM ").append(table);
        if (StringUtils.isNotEmpty(baseWhereClause)) {
            query.append(" WHERE ").append(baseWhereClause);
        }
        query.append(" GROUP BY ").append(colList)
                .append(" HAVING COUNT(*) > 1)");
        return query.toString();
    }

    /**
     * 多字段组合唯一性校验 - 正常数据 SQL（分页）
     * 规则编码：COMPOSITE_UNIQUENESS_VALIDATION
     * <p>
     * 输出：未重复组合数据明细（即组合唯一的记录）
     */
    default String generateCompositeUniquenessValidationValidDataSql(QualityRuleEntity rule, int limit, int offset) {
        String table = rule.getTableName();
        List<String> columns = rule.getRuleColumns();
        String colList = String.join(", ", columns);
        String baseWhereClause = rule.getWhereClause();
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM ").append(table)
                .append(" WHERE ");
        if (StringUtils.isNotEmpty(baseWhereClause)) {
            query.append(baseWhereClause).append(" AND ");
        }
        query.append("(").append(colList)
                .append(") IN (SELECT ").append(colList)
                .append(" FROM ").append(table);
        if (StringUtils.isNotEmpty(baseWhereClause)) {
            query.append(" WHERE ").append(baseWhereClause);
        }
        query.append(" GROUP BY ").append(colList)
                .append(" HAVING COUNT(*) = 1)");
        return addPagination(query.toString(), limit, offset);
    }


    /**
     * 数值精度校验 - 错误统计 SQL
     * 规则编码：DECIMAL_PRECISION_VALIDATION
     * <p>
     * 检查小数点后超过指定精度的数量，统计错误总数 + 全部记录数。
     */
    default String generateDecimalPrecisionValidationSql(QualityRuleEntity rule) {
        return generateSql(rule, fragDecimalPrecision(rule));
    }

    /**
     * 数值精度校验 - 错误明细 SQL
     * 规则编码：DECIMAL_PRECISION_VALIDATION
     * <p>
     * 返回所有小数位数超出指定精度的记录。
     */
    default String generateDecimalPrecisionValidationErrorSql(QualityRuleEntity rule) {
        return generateErrorSql(rule, fragDecimalPrecision(rule));
    }

    /**
     * 数值精度校验 - 正常数据分页 SQL
     * 规则编码：DECIMAL_PRECISION_VALIDATION
     * <p>
     * 返回所有符合小数精度要求的记录（不超过指定小数位数）。
     */
    default String generateDecimalPrecisionValidationValidDataSql(QualityRuleEntity rule, int limit, int offset) {
        return addPagination(generateValidDataSql(rule, fragDecimalPrecision(rule)), limit, offset);
    }

    /**
     * 枚举值校验 - 错误统计 SQL
     * 规则编码：ENUM_VALIDATION
     * <p>
     * 检查字段值是否在指定枚举值列表内，统计不合法数量 + 总数。
     */
    @SuppressWarnings("unchecked")
    default String generateEnumValidationSql(QualityRuleEntity rule) {
        return CommonGenerator.generateSql(rule, fragEnum(rule));
    }

    /**
     * 枚举值校验 - 错误明细 SQL
     * 规则编码：ENUM_VALIDATION
     * <p>
     * 返回字段值不在指定枚举列表中的记录。
     */
    @SuppressWarnings("unchecked")
    default String generateEnumValidationErrorSql(QualityRuleEntity rule) {
        return CommonGenerator.generateErrorSql(rule, fragEnum(rule));
    }

    /**
     * 枚举值校验 - 正常数据分页 SQL
     * 规则编码：ENUM_VALIDATION
     * <p>
     * 返回字段值在枚举列表中的记录，支持分页。
     */
    @SuppressWarnings("unchecked")
    default String generateEnumValidationValidDataSql(QualityRuleEntity rule, int limit, int offset) {
        return addPagination(generateValidDataSql(rule, fragEnum(rule)), limit, offset);
    }

    /**
     * 字段组完整性校验 - 错误统计 SQL
     * 规则编码：GROUP_FIELD_COMPLETENESS
     * <p>
     * 检查字段组中是否存在任一字段为 NULL，统计错误数量和总数。
     */
    @SuppressWarnings("unchecked")
    default String generateGroupFieldCompletenessSql(QualityRuleEntity rule) {
        List<String> columns = rule.getRuleColumns();
        Map<String, Object> ruleConfig = rule.getConfig();
        int fillStrategy = MapUtils.getIntValue(ruleConfig, "fillStrategy", 1);
        String table = rule.getTableName();
        String whereClause = rule.getWhereClause();
        StringBuilder query = new StringBuilder()
                .append("SELECT COUNT(*) AS totalCount, ")
                .append("COUNT(CASE WHEN ");
        String frag = String.format("NOT (%s)", fragFieldCompleteness(columns, fillStrategy));
        query.append("(").append(frag).append(") THEN 1 END) AS errorCount ")
                .append("FROM ").append(table);
        if (StringUtils.isNotEmpty(whereClause)) {
            query.append(" WHERE ").append(whereClause);
        }
        return query.toString();
    }

    /**
     * 字段组完整性校验 - 错误明细 SQL
     * 规则编码：GROUP_FIELD_COMPLETENESS
     * <p>
     * 返回字段组中存在 NULL 值的记录明细。
     */
    @SuppressWarnings("unchecked")
    default String generateGroupFieldCompletenessErrorSql(QualityRuleEntity rule) {
        List<String> columns = rule.getRuleColumns();
        Map<String, Object> ruleConfig = rule.getConfig();
        int fillStrategy = MapUtils.getIntValue(ruleConfig, "fillStrategy", 1);
        String table = rule.getTableName();
        String whereClause = rule.getWhereClause();
        StringBuilder query = new StringBuilder()
                .append("SELECT * FROM ").append(table)
                .append(" WHERE (");
        String frag = String.format("NOT (%s)", fragFieldCompleteness(columns, fillStrategy));
        query.append(frag).append(")");
        if (StringUtils.isNotEmpty(whereClause)) {
            query.append(" AND ").append(whereClause);
        }
        return query.toString();
    }

    /**
     * 字段组完整性校验 - 正常数据分页 SQL
     * 规则编码：GROUP_FIELD_COMPLETENESS
     * <p>
     * 返回字段组中所有字段都非空的记录，支持分页。
     */
    @SuppressWarnings("unchecked")
    default String generateGroupFieldCompletenessValidDataSql(QualityRuleEntity rule, int limit, int offset) {
        List<String> columns = rule.getRuleColumns();
        Map<String, Object> ruleConfig = rule.getConfig();
        int fillStrategy = MapUtils.getIntValue(ruleConfig, "fillStrategy", 1);
        String table = rule.getTableName();
        String whereClause = rule.getWhereClause();
        StringBuilder query = new StringBuilder()
                .append("SELECT * FROM ").append(table)
                .append(" WHERE (");
        String frag = fragFieldCompleteness(columns, fillStrategy);
        query.append(frag).append(") ");
        if (StringUtils.isNotEmpty(whereClause)) {
            query.append(" AND ").append(whereClause);
        }

        return addPagination(query.toString(),limit,offset);
    }

    /**
     * 字段长度范围校验 - 错误统计 SQL
     * 规则编码：LENGTH_VALIDATION
     * <p>
     * 检查字段字符串长度是否超出 [min, max] 范围，返回错误数 + 总数。
     */
    default String generateLengthValidationSql(QualityRuleEntity rule) {
        return generateSql(rule, fragLength(rule));
    }

    /**
     * 字段长度范围校验 - 错误明细 SQL
     * 规则编码：LENGTH_VALIDATION
     * <p>
     * 返回字段长度不在合法区间内的记录。
     */
    default String generateLengthValidationErrorSql(QualityRuleEntity rule) {
        return generateErrorSql(rule, fragLength(rule));
    }

    /**
     * 字段长度范围校验 - 正常数据分页 SQL
     * 规则编码：LENGTH_VALIDATION
     * <p>
     * 返回字段长度在合法范围内的记录，支持分页。
     */
    default String generateLengthValidationValidDataSql(QualityRuleEntity rule, int limit, int offset) {
        return addPagination(generateValidDataSql(rule, fragLength(rule)), limit, offset);
    }

    /**
     * 数值字段范围校验 - 错误统计 SQL
     * 规则编码：NUMERIC_RANGE_VALIDATION
     * <p>
     * 检查字段数值是否超出 [min, max] 范围，返回错误数量 + 总记录数。
     */
    default String generateNumericRangeValidationSql(QualityRuleEntity rule) {
        return generateSql(rule, fragNumericRange(rule));
    }

    /**
     * 数值字段范围校验 - 错误明细 SQL
     * 规则编码：NUMERIC_RANGE_VALIDATION
     * <p>
     * 返回字段值不在 [min, max] 范围内的记录。
     */
    default String generateNumericRangeValidationErrorSql(QualityRuleEntity rule) {
        return generateErrorSql(rule, fragNumericRange(rule));
    }

    /**
     * 数值字段范围校验 - 正常数据分页 SQL
     * 规则编码：NUMERIC_RANGE_VALIDATION
     * <p>
     * 返回字段值在 [min, max] 范围内的记录，支持分页。
     */
    default String generateNumericRangeValidationValidDataSql(QualityRuleEntity rule, int limit, int offset) {
        return addPagination(generateValidDataSql(rule, fragNumericRange(rule)), limit, offset);
    }

    /**
     * 时间字段先后顺序校验 - 错误统计 SQL
     * 规则编码：TIME_ORDER_VALIDATION
     * <p>
     * 检查开始时间字段是否晚于结束时间字段，返回错误数量 + 总记录数。
     */
    @SuppressWarnings("unchecked")
    default String generateTimeOrderValidationSql(QualityRuleEntity rule) {
        Map<String, Object> ruleConfig = rule.getConfig();
        Boolean allowPartialNull = SqlBuilderUtils.parseBoolean(ruleConfig.get("allowPartialNull"));
        allowPartialNull = allowPartialNull != null && allowPartialNull;
        List<Map<String, String>> conditions = (List<Map<String, String>>) rule.getConfig().get("conditions");
        String table = rule.getTableName();
        String whereClause = rule.getWhereClause();

        StringBuilder query = new StringBuilder()
                .append("SELECT COUNT(*) AS totalCount, ")
                .append("COUNT(CASE WHEN ");
        query.append("(").append(fragOrderNeg(conditions, allowPartialNull)).append(") THEN 1 END) AS errorCount ")
                .append("FROM ").append(table);
        if (StringUtils.isNotEmpty(whereClause)) {
            query.append(" WHERE ").append(whereClause);
        }
        return query.toString();
    }

    /**
     * 时间字段先后顺序校验 - 错误明细 SQL
     * 规则编码：TIME_ORDER_VALIDATION
     * <p>
     * 返回开始时间字段晚于结束时间字段的记录。
     */
    @SuppressWarnings("unchecked")
    default String generateTimeOrderValidationErrorSql(QualityRuleEntity rule) {
        Map<String, Object> ruleConfig = rule.getConfig();
        Boolean allowPartialNull = SqlBuilderUtils.parseBoolean(ruleConfig.get("allowPartialNull"));
        allowPartialNull = allowPartialNull != null && allowPartialNull;
        List<Map<String, String>> conditions = (List<Map<String, String>>) rule.getConfig().get("conditions");
        String table = rule.getTableName();
        String whereClause = rule.getWhereClause();
        StringBuilder query = new StringBuilder()
                .append("SELECT * FROM ").append(table)
                .append(" WHERE (");
        String frag = fragOrderNeg(conditions, allowPartialNull);
        query.append(frag).append(")");
        if (StringUtils.isNotEmpty(whereClause)) {
            query.append(" AND ").append(whereClause);
        }
        return query.toString();
    }

    /**
     * 时间字段先后顺序校验 - 正常数据分页 SQL
     * 规则编码：TIME_ORDER_VALIDATION
     * <p>
     * 返回开始时间早于或等于结束时间的记录，支持分页。
     */
    @SuppressWarnings("unchecked")
    default String generateTimeOrderValidationValidDataSql(QualityRuleEntity rule, int limit, int offset) {
        Map<String, Object> ruleConfig = rule.getConfig();
        Boolean allowPartialNull = SqlBuilderUtils.parseBoolean(ruleConfig.get("allowPartialNull"));
        allowPartialNull = allowPartialNull != null && allowPartialNull;
        List<Map<String, String>> conditions = (List<Map<String, String>>) rule.getConfig().get("conditions");
        String table = rule.getTableName();
        String whereClause = rule.getWhereClause();
        StringBuilder query = new StringBuilder()
                .append("SELECT * FROM ").append(table)
                .append(" WHERE (");
        String frag;
        if (allowPartialNull) {
            frag = conditions.stream()
                    .map(c -> {
                        String leftField = c.get("leftField");
                        String rightField = c.get("rightField");
                        return String.format("(%s IS NULL OR %s IS NULL OR %s %s %s)", leftField, rightField, leftField, c.get("operator"), rightField);
                    })
                    .collect(Collectors.joining(" AND "));
        } else {
            frag = conditions.stream()
                    .map(c -> String.format("%s %s %s", c.get("leftField"), c.get("operator"), c.get("rightField")))
                    .collect(Collectors.joining(" AND "));
        }
        query.append(frag).append(") ");
        if (StringUtils.isNotEmpty(whereClause)) {
            query.append(" AND ").append(whereClause);
        }
        return addPagination(query.toString(),limit,offset);
    }

}
