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


}
