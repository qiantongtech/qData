/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
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
 * Purpose: data quality sql
 * </p>
 *
 * @create: 2025-03-12 16:29
 **/
public interface ComponentItem extends QualityFragSql {

    default String addPagination(String sql, int limit, int offset) {
        return String.format("%s LIMIT %d OFFSET %d", sql, limit, offset);
    }

    /**
     * Generate SQL for string type verification (for example, ID cards only allow numbers and X)
     * Rule code: CHARACTER_VALIDATION
     * <p>
     * Output: number of error data + total number
     */
    default String generateCharacterValidationSql(QualityRuleEntity rule) {
        String frag = fragCharacter(rule);
        frag = neg(frag, rule);
        return generateSql(rule, frag);
    }

    /**
     * Generating error data SQL for string type verification
     * Rule code: CHARACTER_VALIDATION
     * <p>
     * Output: error details
     */
    default String generateCharacterValidationErrorSql(QualityRuleEntity rule) {
        String frag = fragCharacter(rule);
        frag = neg(frag, rule);
        return generateDataSql(rule, frag);
    }

    /**
     * Generate normal data query SQL for string type verification (supports paging)
     * Rule code: CHARACTER_VALIDATION
     * <p>
     * Used to query data details that comply with regular rules
     *
     * @param rule quality rule entity, including table name, field name, regular expression
     * @param limit maximum number of rows
     * @param offset offset (from which line to start)
     * @return SQL string
     */
    default String generateCharacterValidationValidDataSql(QualityRuleEntity rule, int limit, int offset) {
        String frag = fragCharacter(rule);
        frag = pos(frag, rule);
        return addPagination(generateDataSql(rule, frag), limit, offset);
    }

    /**
     * Multi-field combination uniqueness check - error statistics SQL
     * Rule code: COMPOSITE_UNIQUNESS_VALIDATION
     * <p>
     * Output: Number of combined field repetitions + total number of records
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
     * Multi-field combination uniqueness check - error details SQL
     * Rule code: COMPOSITE_UNIQUNESS_VALIDATION
     * <p>
     * Output: Repeated combination of records
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
     * Multi-field combination uniqueness check - normal data SQL (paging)
     * Rule code: COMPOSITE_UNIQUNESS_VALIDATION
     * <p>
     * Output: Unduplicated combined data details (i.e. unique records in the combination)
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
     * Numeric precision check - error statistics SQL
     * Rule code: DECIMAL_PRECISION_VALIDATION
     * <p>
     * Check the number after the decimal point that exceeds the specified precision, and count the total number of errors + the number of all records.
     */
    default String generateDecimalPrecisionValidationSql(QualityRuleEntity rule) {
        String frag = fragDecimalPrecision(rule);
        frag = neg(frag, rule);
        return generateSql(rule, frag);
    }

    /**
     * Numeric precision check - error details SQL
     * Rule code: DECIMAL_PRECISION_VALIDATION
     * <p>
     * Returns all records with more decimal places than the specified precision.
     */
    default String generateDecimalPrecisionValidationErrorSql(QualityRuleEntity rule) {
        String frag = fragDecimalPrecision(rule);
        frag = neg(frag, rule);
        return generateDataSql(rule, frag);
    }

    /**
     * Numeric precision check - normal data paging SQL
     * Rule code: DECIMAL_PRECISION_VALIDATION
     * <p>
     * Returns all records that meet the decimal precision requirement (no more than the specified number of decimal places).
     */
    default String generateDecimalPrecisionValidationValidDataSql(QualityRuleEntity rule, int limit, int offset) {
        String frag = fragDecimalPrecision(rule);
        frag = pos(frag, rule);
        return addPagination(generateDataSql(rule, frag), limit, offset);
     }

    /**
     * Enumeration value verification - error statistics SQL
     * Rule code: ENUM_VALIDATION
     * <p>
     * Check whether the field value is within the specified enumeration value list, and count the illegal number + total number.
     */
    default String generateEnumValidationSql(QualityRuleEntity rule) {
        String frag = fragEnum(rule);
        frag = neg(frag, rule);
        return generateSql(rule, frag);
    }

    /**
     * Enumeration value verification - error details SQL
     * Rule code: ENUM_VALIDATION
     * <p>
     * Returns records whose field values ​​are not in the specified enumeration list.
     */
    default String generateEnumValidationErrorSql(QualityRuleEntity rule) {
        String frag = fragEnum(rule);
        frag = neg(frag, rule);
        return generateDataSql(rule, frag);
    }

    /**
     * Enumeration value verification - normal data paging SQL
     * Rule code: ENUM_VALIDATION
     * <p>
     * Returns the records whose field values are in the enumeration list, and supports paging.
     */
    default String generateEnumValidationValidDataSql(QualityRuleEntity rule, int limit, int offset) {
        String frag = fragEnum(rule);
        frag = pos(frag, rule);
        return addPagination(generateDataSql(rule, frag), limit, offset);
    }

    /**
     * Field length range verification - error statistics SQL
     * Rule code: LENGTH_VALIDATION
     * <p>
     * Checks whether the field string length exceeds the [min, max] range and returns the number of errors + total number.
     */
    default String generateLengthValidationSql(QualityRuleEntity rule) {
        String frag = fragLength(rule);
        frag = neg(frag, rule);
        return generateSql(rule, frag);
    }

    /**
     * Field length range verification - error details SQL
     * Rule code: LENGTH_VALIDATION
     * <p>
     * Returns records whose field length is not within the legal range.
     */
    default String generateLengthValidationErrorSql(QualityRuleEntity rule) {
        String frag = fragLength(rule);
        frag = neg(frag, rule);
        return generateDataSql(rule, frag);
    }

    /**
     * Field length range verification - normal data paging SQL
     * Rule code: LENGTH_VALIDATION
     * <p>
     * Returns records whose field length is within the legal range and supports paging.
     */
    default String generateLengthValidationValidDataSql(QualityRuleEntity rule, int limit, int offset) {
        String frag = fragLength(rule);
        frag = pos(frag, rule);
        return addPagination(generateDataSql(rule, frag), limit, offset);
    }
    /**
     * Numeric field range verification - error statistics SQL
     * Rule code: NUMERIC_RANGE_VALIDATION
     * <p>
     * Check whether the field value exceeds the [min, max] range and return the number of errors + the total number of records.
     */
    default String generateNumericRangeValidationSql(QualityRuleEntity rule) {
        String frag = fragNumericRange(rule);
        frag = neg(frag, rule);
        return generateSql(rule, frag);
    }

    /**
     * Numeric field range verification - error details SQL
     * Rule code: NUMERIC_RANGE_VALIDATION
     * <p>
     * Returns records whose field values are not in the range [min, max].
     */
    default String generateNumericRangeValidationErrorSql(QualityRuleEntity rule) {
        String frag = fragNumericRange(rule);
        frag = neg(frag, rule);
        return generateDataSql(rule, frag);
    }

    /**
     * Numeric field range verification - normal data paging SQL
     * Rule code: NUMERIC_RANGE_VALIDATION
     * <p>
     * Returns records whose field values are within the [min, max] range, and supports paging.
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
     * Field group integrity check - error statistics SQL
     * Rule code: GROUP_FIELD_COMPLETENESS
     * <p>
     * Check whether any field in the field group is NULL, and count the number and total number of errors.
     */
    default String generateGroupFieldCompletenessSql(QualityRuleEntity rule) {
        List<String> columns = rule.getRuleColumns();
        Map<String, Object> ruleConfig = rule.getConfig();
        int fillStrategy = MapUtils.getIntValue(ruleConfig, "fillStrategy", 1);
        String frag = String.format("NOT (%s)", fragFieldCompleteness(columns, fillStrategy));
        return generateSql(rule, frag);
    }

    /**
     * Field group integrity check - error details SQL
     * Rule code: GROUP_FIELD_COMPLETENESS
     * <p>
     * Returns details of records with NULL values in the field group.
     */
    default String generateGroupFieldCompletenessErrorSql(QualityRuleEntity rule) {
        List<String> columns = rule.getRuleColumns();
        Map<String, Object> ruleConfig = rule.getConfig();
        int fillStrategy = MapUtils.getIntValue(ruleConfig, "fillStrategy", 1);
        String frag = String.format("NOT (%s)", fragFieldCompleteness(columns, fillStrategy));
        return generateDataSql(rule, frag);
    }

    /**
     * Field group integrity check - normal data paging SQL
     * Rule code: GROUP_FIELD_COMPLETENESS
     * <p>
     * Returns records in which all fields in the field group are non-empty, and paging is supported.
     */
    default String generateGroupFieldCompletenessValidDataSql(QualityRuleEntity rule, int limit, int offset) {
        List<String> columns = rule.getRuleColumns();
        Map<String, Object> ruleConfig = rule.getConfig();
        int fillStrategy = MapUtils.getIntValue(ruleConfig, "fillStrategy", 1);
        String frag = fragFieldCompleteness(columns, fillStrategy);
        return addPagination(generateDataSql(rule, frag), limit, offset);
    }



    /**
     * Generate SQL for string type verification
     * Only used for customer input data, click detection sql generation method
     *
     * @param rule
     * @param inputValue
     * @return
     */
    default String generateValidDataCheckSql(QualityRuleEntity rule, String inputValue){
        // 1. Construct the SQL expression of "input value" (prevent direct spelling of Java variables)
        String valueExpr = "'" + inputValue.replace("'", "''") + "'";

        // 2. Construct a rule verification fragment (based on input values, not fields)
        String regex = (String) rule.getConfig().get("regex");
        String frag = regex(valueExpr, regex);

        // 3. Whether to ignore NULL when processing
//        frag = neg(frag, rule);

        // 4. Final SQL (only returns 0 / 1)
        return new StringBuilder()
                .append("SELECT CASE WHEN ")
                .append(frag)
                .append(" THEN 1 ELSE 0 END AS valid_flag")
                .toString();
    }
}
