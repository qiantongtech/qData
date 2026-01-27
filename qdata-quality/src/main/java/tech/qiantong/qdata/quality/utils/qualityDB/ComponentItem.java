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
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.quality.utils.qualityDB;

import org.apache.commons.collections4.MapUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.quality.dal.dataobject.quality.ColumnCompare;
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
        String frag = fragCharacter(rule);
        frag = neg(frag, rule);
        return generateSql(rule, frag);
    }

    /**
     * 生成字符串类型校验的错误数据SQL
     * 规则编码：CHARACTER_VALIDATION
     * <p>
     * 输出：错误明细
     */
    default String generateCharacterValidationErrorSql(QualityRuleEntity rule) {
        String frag = fragCharacter(rule);
        frag = neg(frag, rule);
        return generateDataSql(rule, frag);
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
        String frag = fragCharacter(rule);
        frag = pos(frag, rule);
        return addPagination(generateDataSql(rule, frag), limit, offset);
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
        String frag = fragDecimalPrecision(rule);
        frag = neg(frag, rule);
        return generateSql(rule, frag);
    }

    /**
     * 数值精度校验 - 错误明细 SQL
     * 规则编码：DECIMAL_PRECISION_VALIDATION
     * <p>
     * 返回所有小数位数超出指定精度的记录。
     */
    default String generateDecimalPrecisionValidationErrorSql(QualityRuleEntity rule) {
        String frag = fragDecimalPrecision(rule);
        frag = neg(frag, rule);
        return generateDataSql(rule, frag);
    }

    /**
     * 数值精度校验 - 正常数据分页 SQL
     * 规则编码：DECIMAL_PRECISION_VALIDATION
     * <p>
     * 返回所有符合小数精度要求的记录（不超过指定小数位数）。
     */
    default String generateDecimalPrecisionValidationValidDataSql(QualityRuleEntity rule, int limit, int offset) {
        String frag = fragDecimalPrecision(rule);
        frag = pos(frag, rule);
        return addPagination(generateDataSql(rule, frag), limit, offset);
     }

    /**
     * 枚举值校验 - 错误统计 SQL
     * 规则编码：ENUM_VALIDATION
     * <p>
     * 检查字段值是否在指定枚举值列表内，统计不合法数量 + 总数。
     */
    default String generateEnumValidationSql(QualityRuleEntity rule) {
        String frag = fragEnum(rule);
        frag = neg(frag, rule);
        return generateSql(rule, frag);
    }

    /**
     * 枚举值校验 - 错误明细 SQL
     * 规则编码：ENUM_VALIDATION
     * <p>
     * 返回字段值不在指定枚举列表中的记录。
     */
    default String generateEnumValidationErrorSql(QualityRuleEntity rule) {
        String frag = fragEnum(rule);
        frag = neg(frag, rule);
        return generateDataSql(rule, frag);
    }

    /**
     * 枚举值校验 - 正常数据分页 SQL
     * 规则编码：ENUM_VALIDATION
     * <p>
     * 返回字段值在枚举列表中的记录，支持分页。
     */
    default String generateEnumValidationValidDataSql(QualityRuleEntity rule, int limit, int offset) {
        String frag = fragEnum(rule);
        frag = pos(frag, rule);
        return addPagination(generateDataSql(rule, frag), limit, offset);
    }

    /**
     * 字段长度范围校验 - 错误统计 SQL
     * 规则编码：LENGTH_VALIDATION
     * <p>
     * 检查字段字符串长度是否超出 [min, max] 范围，返回错误数 + 总数。
     */
    default String generateLengthValidationSql(QualityRuleEntity rule) {
        String frag = fragLength(rule);
        frag = neg(frag, rule);
        return generateSql(rule, frag);
    }

    /**
     * 字段长度范围校验 - 错误明细 SQL
     * 规则编码：LENGTH_VALIDATION
     * <p>
     * 返回字段长度不在合法区间内的记录。
     */
    default String generateLengthValidationErrorSql(QualityRuleEntity rule) {
        String frag = fragLength(rule);
        frag = neg(frag, rule);
        return generateDataSql(rule, frag);
    }

    /**
     * 字段长度范围校验 - 正常数据分页 SQL
     * 规则编码：LENGTH_VALIDATION
     * <p>
     * 返回字段长度在合法范围内的记录，支持分页。
     */
    default String generateLengthValidationValidDataSql(QualityRuleEntity rule, int limit, int offset) {
        String frag = fragLength(rule);
        frag = pos(frag, rule);
        return addPagination(generateDataSql(rule, frag), limit, offset);
    }
    /**
     * 数值字段范围校验 - 错误统计 SQL
     * 规则编码：NUMERIC_RANGE_VALIDATION
     * <p>
     * 检查字段数值是否超出 [min, max] 范围，返回错误数量 + 总记录数。
     */
    default String generateNumericRangeValidationSql(QualityRuleEntity rule) {
        String frag = fragNumericRange(rule);
        frag = neg(frag, rule);
        return generateSql(rule, frag);
    }

    /**
     * 数值字段范围校验 - 错误明细 SQL
     * 规则编码：NUMERIC_RANGE_VALIDATION
     * <p>
     * 返回字段值不在 [min, max] 范围内的记录。
     */
    default String generateNumericRangeValidationErrorSql(QualityRuleEntity rule) {
        String frag = fragNumericRange(rule);
        frag = neg(frag, rule);
        return generateDataSql(rule, frag);
    }

    /**
     * 数值字段范围校验 - 正常数据分页 SQL
     * 规则编码：NUMERIC_RANGE_VALIDATION
     * <p>
     * 返回字段值在 [min, max] 范围内的记录，支持分页。
     */
    default String generateNumericRangeValidationValidDataSql(QualityRuleEntity rule, int limit, int offset) {
        String frag = fragNumericRange(rule);
        frag = pos(frag, rule);
        return addPagination(generateDataSql(rule, frag), limit, offset);
    }

    default String generateLogicCompareValidationSql(QualityRuleEntity rule) {
        boolean ignoreNullValue = ignoreNullValue(rule);
        List<ColumnCompare> conditions = BeanUtils.toBean((List<?>) rule.getConfig().get("conditions"), ColumnCompare.class);
        return generateSql(rule, ColumnCompare.toExpressionsNeg(conditions, ignoreNullValue));
    }

    default String generateLogicCompareValidationErrorSql(QualityRuleEntity rule) {
        boolean ignoreNullValue = ignoreNullValue(rule);
        List<ColumnCompare> conditions = BeanUtils.toBean((List<?>) rule.getConfig().get("conditions"), ColumnCompare.class);
        return generateDataSql(rule, ColumnCompare.toExpressionsNeg(conditions, ignoreNullValue));
    }

    default String generateLogicCompareValidationValidDataSql(QualityRuleEntity rule, int limit, int offset) {
        boolean ignoreNullValue = ignoreNullValue(rule);
        List<ColumnCompare> conditions = BeanUtils.toBean((List<?>) rule.getConfig().get("conditions"), ColumnCompare.class);
        String sql = generateDataSql(rule, ColumnCompare.toExpressions(conditions, ignoreNullValue));
        return addPagination(sql, limit, offset);
    }

    /**
     * 字段组完整性校验 - 错误统计 SQL
     * 规则编码：GROUP_FIELD_COMPLETENESS
     * <p>
     * 检查字段组中是否存在任一字段为 NULL，统计错误数量和总数。
     */
    default String generateGroupFieldCompletenessSql(QualityRuleEntity rule) {
        List<String> columns = rule.getRuleColumns();
        Map<String, Object> ruleConfig = rule.getConfig();
        int fillStrategy = MapUtils.getIntValue(ruleConfig, "fillStrategy", 1);
        String frag = String.format("NOT (%s)", fragFieldCompleteness(columns, fillStrategy));
        return generateSql(rule, frag);
    }

    /**
     * 字段组完整性校验 - 错误明细 SQL
     * 规则编码：GROUP_FIELD_COMPLETENESS
     * <p>
     * 返回字段组中存在 NULL 值的记录明细。
     */
    default String generateGroupFieldCompletenessErrorSql(QualityRuleEntity rule) {
        List<String> columns = rule.getRuleColumns();
        Map<String, Object> ruleConfig = rule.getConfig();
        int fillStrategy = MapUtils.getIntValue(ruleConfig, "fillStrategy", 1);
        String frag = String.format("NOT (%s)", fragFieldCompleteness(columns, fillStrategy));
        return generateDataSql(rule, frag);
    }

    /**
     * 字段组完整性校验 - 正常数据分页 SQL
     * 规则编码：GROUP_FIELD_COMPLETENESS
     * <p>
     * 返回字段组中所有字段都非空的记录，支持分页。
     */
    default String generateGroupFieldCompletenessValidDataSql(QualityRuleEntity rule, int limit, int offset) {
        List<String> columns = rule.getRuleColumns();
        Map<String, Object> ruleConfig = rule.getConfig();
        int fillStrategy = MapUtils.getIntValue(ruleConfig, "fillStrategy", 1);
        String frag = fragFieldCompleteness(columns, fillStrategy);
        return addPagination(generateDataSql(rule, frag), limit, offset);
    }



    /**
     * 生成字符串类型校验的SQL
     * 只用于客户输入数据，点击检测的sql生成方法
     *
     * @param rule
     * @param inputValue
     * @return
     */
    default String generateValidDataCheckSql(QualityRuleEntity rule, String inputValue){
        // 1. 构造“输入值”的 SQL 表达式（防止直接拼 Java 变量）
        String valueExpr = "'" + inputValue.replace("'", "''") + "'";

        // 2. 构造规则校验片段（基于输入值，而不是字段）
        String regex = (String) rule.getConfig().get("regex");
        String frag = regex(valueExpr, regex);

        // 3. 处理是否忽略 NULL
//        frag = neg(frag, rule);

        // 4. 最终 SQL（只返回 0 / 1）
        return new StringBuilder()
                .append("SELECT CASE WHEN ")
                .append(frag)
                .append(" THEN 1 ELSE 0 END AS valid_flag")
                .toString();
    }
}
