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

package tech.qiantong.qdata.spark.etl.transition;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.sql.*;
import org.apache.spark.sql.expressions.Window;
import org.apache.spark.sql.expressions.WindowSpec;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import tech.qiantong.qdata.common.enums.TaskComponentTypeEnum;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.spark.etl.utils.LogUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.alibaba.fastjson2.JSONWriter.Feature.PrettyFormat;
import static org.apache.spark.sql.functions.*;

/**
 * <P>
 * Purpose: cleaning-conversion
 * </p>
 *
 * @author: FXB
 * @create: 2025-04-22 13:39
 **/
public class CleanTransition implements Transition {

    /**
     * New
     * @param spark
     * @param dataset
     * @param transition
     * @param logParams
     * @return
     */
    @Override
    public  Dataset<Row> transition(SparkSession spark,Dataset<Row> dataset, JSONObject transition, LogUtils.Params logParams) {
        LogUtils.writeLog(logParams, "*********************************  Initialize task context  ***********************************");
        LogUtils.writeLog(logParams, MessageUtils.messageEn("etl.transition.clean.start"));
        LogUtils.writeLog(logParams, MessageUtils.messageEn("etl.task.start.time", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS")));
        LogUtils.writeLog(logParams, MessageUtils.messageEn("etl.task.parameters", transition.toJSONString(PrettyFormat)));

        JSONObject parameter = transition.getJSONObject("parameter");
        // Get the column names that need to be processed
        List<Map<String, Object>> tableFieldList = (List<Map<String, Object>>) parameter.get("tableFields");
        String where = parameter.getString("where");
        if(StringUtils.isNotEmpty(where)){
            dataset = safeFilter(dataset, where, logParams);
        }

        if (tableFieldList == null || tableFieldList.isEmpty()) {
            LogUtils.writeLog(logParams, MessageUtils.messageEn("etl.transition.no.rules"));
            return dataset;
        }

        // Apply data processing to each column
        for (Map<String, Object> rule : tableFieldList) {
            String ruleCode = MapUtils.getString(rule, "ruleCode");
            String ruleType = MapUtils.getString(rule, "ruleType");
            JSONObject ruleConfig = JSONObject.parseObject((String) rule.get("ruleConfig"));
//            JSONObject ruleConfig = (JSONObject)rule.get("ruleConfig");
            String whereClause = MapUtils.getString(rule, "whereClause");
            if(StringUtils.isNotEmpty(whereClause)){
                dataset = safeFilter(dataset, whereClause, logParams);
            }

            // Check whether the field exists before executing
            if (!checkColumnsExist(dataset, ruleConfig)) {
                LogUtils.writeLog(logParams, MessageUtils.messageEn("etl.rule.skip", ruleCode));
                continue;
            }

            switch (ruleType) {
                case "WITHIN_BOUNDARY": // Numerical boundary adjustment
                    dataset = applyNumericBoundary(dataset, ruleConfig);
                    break;
                case "REMOVE_WHITESPACE": // Remove spaces from string
                    dataset = applyTrim(dataset, ruleConfig);
                    break;
                case "SIMPLE_REPLACE": // Regular expression replacement
                    dataset = applyRegexReplace(dataset, ruleConfig);
                    break;
                case "REMOVE_EMPTY_COMBINATION": // Delete the combined field if it is empty
                    dataset = applyDeleteIfAllNull(dataset, ruleConfig);
                    break;
                case "TO_UPPERCASE": // Convert field values to uppercase
                    dataset = applyUpperCase(dataset, ruleConfig);
                    break;
                case "TO_LOWERCASE": // Convert field values to lowercase
                    dataset = applyLowerCase(dataset, ruleConfig);
                    break;
                case "ADD_PREFIX_SUFFIX": // Unify field prefix/suffix
                    dataset = applyPrefixSuffix(dataset, ruleConfig);
                    break;
                case "MENU_CUSTOM": // Enumeration value mapping normalization
                    dataset = normalizeEnumMapping(dataset, ruleConfig);
                    break;
                case "KEEP_LATEST_OR_FIRST": // Deduplication by combined fields (keep the latest or first item)
                    dataset = deduplicateByFieldsKeepFirst(dataset, ruleConfig);
                    break;
                case "CHECK_EXPIRATION": // Clean up expired records
                    dataset = purgeStaleEntries(dataset, ruleConfig);
                    break;
                case "FIX_TO_PRECISION": // Uniform decimal places
                    dataset = formatDecimalPlaces(dataset, ruleConfig);
                    break;
                case "DATE_FORMAT_STD": // Unified date format
                    dataset = applyDateFormatStd(dataset, ruleConfig);
                    break;
                case "STRING_SUBSTR": // Character interception
                    dataset = applyStringSubstr(dataset, ruleConfig);
                    break;
                default:
                    LogUtils.writeLog(logParams, MessageUtils.messageEn("etl.unknown.rule", ruleCode));
            }
        }
        return dataset;
    }

    @Override
    public String code() {
        return TaskComponentTypeEnum.SPARK_CLEAN.getCode();
    }

    /**
     * STRING_SUBSTR - character interception
     * When the field length exceeds the specified length, it is truncated from the beginning or end according to the configuration.
     *
     * cfg example:
     * {
     *   "columns": ["description"],
     *   "maxLength": 100,
     * "direction": "1" // Optional: 1 (from the beginning), 2 (from the end), 1
     * }
     */
    private Dataset<Row> applyStringSubstr(Dataset<Row> dataset, JSONObject cfg) {
        String colName = cfg.getJSONArray("columns").getString(0);
        Integer maxLength = cfg.getInteger("maxLength");
        String direction = cfg.getString("direction"); // 1 or 2
        if (direction == null || StringUtils.isBlank(direction)) {
            direction = "1";
        }

        Column col = functions.col(colName).cast("string");

        Column newCol;
        if ("2".equalsIgnoreCase(direction)) {
            // Cut maxLength characters from the end
            newCol = functions.when(functions.length(col).gt(maxLength),
                            functions.expr("substring(" + colName + ", length(" + colName + ")-" + (maxLength - 1) + ", " + maxLength + ")"))
                    .otherwise(col);
        } else {
            // Cut from the beginning (default)
            newCol = functions.when(functions.length(col).gt(maxLength),
                            functions.expr("substring(" + colName + ", 1, " + maxLength + ")"))
                    .otherwise(col);
        }

        return dataset.withColumn(colName, newCol);
    }

    /**
     * Remove spaces from string
     */
    private Dataset<Row> applyTrim(Dataset<Row> dataset, JSONObject cfg) {
        String col = cfg.getJSONArray("columns").getString(0);
        int handleType = cfg.getIntValue("handleType");
        if (handleType == 1) {
            return dataset.withColumn(col, functions.trim(dataset.col(col)));
        } else if (handleType == 2) {
            return dataset.withColumn(col, functions.regexp_replace(dataset.col(col), "\\s+", ""));
        }
        return dataset;
    }

    private Dataset<Row> formatDecimalPlaces(Dataset<Row> dataset, JSONObject cfg) {
        String col = cfg.getJSONArray("columns").getString(0);
        Integer stringValue = cfg.getInteger("stringValue");

        Column formattedColumn = functions.round(col(col), stringValue);
        // If decimalPlaces is 0, convert to integer type
        if (stringValue == 0) {
            formattedColumn = formattedColumn.cast("int");
        }
        dataset = dataset.withColumn(col, formattedColumn);

        return dataset;
    }

    private Dataset<Row> purgeStaleEntries(Dataset<Row> dataset, JSONObject cfg) {
        String col = cfg.getJSONArray("columns").getString(0);
        Integer dataRangeType = cfg.getInteger("dataRangeType");
        String dataRangeValue = cfg.getString("dataRangeValue");
        LocalDate currentDate = LocalDate.now();
        // Identifies greater than, equal to
        Boolean flag = true;
        // Format date as string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // 0: fixed time range, 1: specific date
        Integer dataRange = cfg.getInteger("dataRange");
        if (dataRange == 0) {
            flag = true;
            LocalDate date30DaysAgo = null;
            // Calculate the date 30 days, months and years ago
            if (dataRangeType == 1) {
                date30DaysAgo = currentDate.minusDays(cfg.getInteger("dataRangeValue"));
            }else if (dataRangeType == 2) {
                date30DaysAgo = currentDate.minusMonths(cfg.getInteger("dataRangeValue"));
            } else if (dataRangeType == 3) {
                date30DaysAgo = currentDate.minusYears(cfg.getInteger("dataRangeValue"));
            }
            dataRangeValue = date30DaysAgo.format(formatter);
        } else {
            if (dataRangeType == 1) {
                flag = true;
            } else {
                flag = false;
            }
        }
        // Expiration processing method
        return handleExpiredRecords(dataset , cfg , dataRangeValue , col , flag);
    }

    private Dataset<Row> handleExpiredRecords(Dataset<Row> dataset, JSONObject cfg, String dataRangeValue, String col , Boolean flag) {
        Integer handleType = cfg.getInteger("handleType");
        String handleColumns = cfg.getString("handleColumns");
        String handleValue = cfg.getString("handleValue");

        Column condition;
        // Convert dataRangeValue date objects to the specified format for comparison
        Column dateColumn = functions.date_format(functions.col(col), "yyyy-MM-dd");

        // Determine whether the date is before or after
        if (flag) {
            condition = dateColumn.lt(dataRangeValue);
        } else {
            condition = dateColumn.gt(dataRangeValue);
        }

        Column result = functions.lit(handleValue);

        // 0: Expiration processing method, 1: Delete records
        if (handleType == 0) {
            dataset = dataset.withColumn(handleColumns, functions.when(condition, result).otherwise(functions.col(handleColumns)));
        } else {
            // By default, data that meets the conditions are retained, and the not method is inverted.
            dataset = dataset.filter(functions.not(condition));
        }
        return dataset;
    }

    private Dataset<Row> deduplicateByFieldsKeepFirst(Dataset<Row> ds, JSONObject cfg) {
        // 1) Read configuration
        List<String> allCols = Optional.ofNullable(cfg.getJSONArray("columns"))
                .map(a -> a.toJavaList(String.class))
                .orElse(Collections.emptyList());
        if (allCols.isEmpty()) {
            throw new IllegalArgumentException(MessageUtils.messageEn("etl.error.rule.config.missing", "029", "columns"));
        }

        JSONArray sv = cfg == null ? null : cfg.getJSONArray("stringValue");
        List<JSONObject> sortRules = sv == null ? Collections.emptyList() : sv.toJavaList(JSONObject.class);

        // 2) No sorting rules: dropDuplicates directly
        if (sortRules.isEmpty()) {
            // Only remove duplicates by group key and keep any one (usually the first one), which is the simplest and most efficient
            return ds.dropDuplicates(allCols.toArray(new String[0]));
        }

        // 3) There are sorting rules: Build grouping key = columns - sorting field (to prevent the sorting key from also being grouped)
        Set<String> sortCols = sortRules.stream()
                .map(o -> o.getString("columns"))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        List<String> groupCols = allCols.stream()
                .filter(c -> !sortCols.contains(c))
                .collect(Collectors.toList());
        if (groupCols.isEmpty()) {
            // If all are removed, use the original columns to get the bottom of things.
            groupCols = allCols;
        }

        // 4) Build window: Sort in ascending order to indicate priority; type: 0=desc (latest), 1=asc (earliest)
        Column[] partCols = groupCols.stream().map(functions::col).toArray(Column[]::new);
        WindowSpec spec = Window.partitionBy(partCols);

        sortRules.sort(Comparator.comparingInt(o -> o.getIntValue("sort"))); // Safe rounding to avoid NPE
        List<Column> orders = new ArrayList<>();
        for (JSONObject r : sortRules) {
            String c = r.getString("columns");
            int t = r.getIntValue("type"); // "0"/"1" can also be parsed
            orders.add(t == 0 ? functions.col(c).desc() : functions.col(c).asc());
        }
        spec = spec.orderBy(orders.toArray(new Column[0]));

        // 5) Take the first one
        Dataset<Row> withRN = ds.withColumn("__rn", functions.row_number().over(spec));
        return withRN.filter(functions.col("__rn").equalTo(1)).drop("__rn");
    }

    private Dataset<Row> normalizeEnumMapping(Dataset<Row> dataset, JSONObject cfg) {
        String col = cfg.getJSONArray("columns").getString(0);
        List<JSONObject> list = cfg.getList("stringValue", JSONObject.class);
// String handleType = cfg.getString("handleType"); // "1-Add prefix" / "2-Add suffix"

        if (StringUtils.isBlank(col) || list == null || list.size() < 1) {
            throw new IllegalArgumentException(MessageUtils.messageEn("etl.error.rule.config.missing", "024", "columns/stringValue"));
        }
        // Construct expressions for when...otherwise
        Column mappedColumn = null;
        for (int i = 0; i < list.size(); i++) {
            JSONObject dict = list.get(i);
            String originalValue = dict.getString("value");
            String dictValue = dict.getString("name");

            Column condition = functions.col(col).equalTo(originalValue);
            Column result = functions.lit(dictValue);

            if (mappedColumn == null) {
                mappedColumn = functions.when(condition, result);
            } else {
                mappedColumn = mappedColumn.when(condition, result);
            }
        }

        // If nothing matches, the original value is returned.
        mappedColumn = mappedColumn.otherwise(functions.col(col));

        // Replace column
        return dataset.withColumn(col, mappedColumn);
    }

    private Dataset<Row> applyPrefixSuffix(Dataset<Row> dataset, JSONObject cfg) {
        String colName = cfg.getJSONArray("columns").getString(0);
        String stringValue = cfg.getString("stringValue");
        String handleType = cfg.getString("handleType"); // "1" prefix; "2" suffix;

        if (StringUtils.isBlank(colName) || StringUtils.isBlank(stringValue) || StringUtils.isBlank(handleType)) {
            throw new IllegalArgumentException(MessageUtils.messageEn("etl.error.rule.config.missing", "107", "columns/stringValue/handleType"));
        }

        if (!("1".equals(handleType) || "2".equals(handleType) || "3".equals(handleType) || "4".equals(handleType))) {
            throw new IllegalArgumentException(MessageUtils.messageEn("etl.error.rule.handle.type.invalid", "107", handleType));
        }

        Column c = col(colName).cast("string");
        String sv = stringValue;

        //The following is not case-sensitive. If they are the same, they will not be spliced.
//        String svLower = sv.toLowerCase();
//        if (StringUtils.equals("1", handleType)) {
// // prefix
//            dataset = dataset.withColumn(
//                    colName,
//                    when(c.isNull(), lit(null))
// .when(lower(c).like(svLower + "%"), c) // Use like to determine the prefix
//                            .otherwise(concat(lit(sv), c))
//            );
//        } else {
// // suffix
//            dataset = dataset.withColumn(
//                    colName,
//                    when(c.isNull(), lit(null))
// .when(lower(c).like("%" + svLower), c) // Use like to determine the suffix
//                            .otherwise(concat(c, lit(sv)))
//            );
//        }

        //The following is a test to send lowercase letters. If the prefix is uppercase and the database name is lowercase, it will still be spliced.
        switch (handleType) {
            case "1": // Add prefix
                dataset = dataset.withColumn(
                        colName,
                        when(c.isNull(), lit(null))
                                .when(c.startsWith(sv), c) // Case-sensitive detection of existing prefixes
                                .otherwise(concat(lit(sv), c))
                );
                break;

            case "2": // Add suffix
                dataset = dataset.withColumn(
                        colName,
                        when(c.isNull(), lit(null))
                                .when(c.endsWith(sv), c) // Case-sensitive detection of existing suffixes
                                .otherwise(concat(c, lit(sv)))
                );
                break;

            case "3": // Remove prefix from match
                dataset = dataset.withColumn(
                        colName,
                        when(c.isNull(), lit(null))
                                .when(c.startsWith(sv), overlay(c, lit(""), lit(1), lit(sv.length()))) // Delete the first sv.length() characters
                                .otherwise(c)
                );
                break;

            case "4": // Remove suffix from match
                dataset = dataset.withColumn(
                        colName,
                        when(c.isNull(), lit(null))
                                .when(c.endsWith(sv),
                                        overlay(
                                                c,
                                                lit(""),
                                                length(c).minus(lit(sv.length())).plus(lit(1)), // Starting position: len - svLen + 1
                                                lit(sv.length())                               // Delete length: svLen
                                        ))// Cut from the beginning to before the suffix
                                .otherwise(c)
                );
                break;
        }

        return dataset;
    }

    private Dataset<Row> safeFilter(Dataset<Row> dataset, String where, LogUtils.Params logParams) {
        if (StringUtils.isBlank(where)) {
            return dataset;
        }

        // Get all column names (converted to lowercase for easier comparison)
        Set<String> columnSet = Arrays.stream(dataset.columns())
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        // Simply extract the field names appearing in where (split by spaces and symbols)
        String[] tokens = where.replaceAll("[()><=!,]", " ")
                .trim()
                .split("\\s+");
        for (String token : tokens) {
            if (token.matches("^[a-zA-Z_][a-zA-Z0-9_]*$") && !columnSet.contains(token.toLowerCase())) {
                String msg = MessageUtils.messageEn("etl.error.filter.field.not.exists", token);
                LogUtils.writeLog(logParams, msg);
                throw new IllegalArgumentException(msg);
            }
        }

        try {
            dataset.selectExpr("*").filter(where).limit(1).count();
        } catch (Exception e) {
            String msg = MessageUtils.messageEn("etl.error.filter.parse.failed", where, e.getMessage());
            LogUtils.writeLog(logParams, msg);
            throw new IllegalArgumentException(msg, e);
        }

        // Execute after passing verification
        return dataset.filter(where);
    }


    /**
     * Check if field exists
     */
    private boolean checkColumnsExist(Dataset<Row> dataset, JSONObject ruleConfig) {
        List<String> allColumns = Arrays.asList(dataset.columns());
        List<String> targetCols = new ArrayList<>();

        if (ruleConfig.containsKey("columns")) {
            targetCols.addAll(ruleConfig.getJSONArray("columns").toJavaList(String.class));
        } else if (ruleConfig.containsKey("columnName")) {
            targetCols.add(ruleConfig.getString("columnName"));
        }

        return allColumns.containsAll(targetCols);
    }

    /**
     * Numerical boundary adjustment
     */
    private Dataset<Row> applyNumericBoundary(Dataset<Row> dataset, JSONObject cfg) {
        String col = cfg.getJSONArray("columns").getString(0);
        double min = cfg.getDoubleValue("min");
        double max = cfg.getDoubleValue("max");
        int handleType = cfg.getIntValue("handleType");

        Column target = dataset.col(col);
        switch (handleType) {
            case 1:
                return dataset.withColumn(col,
                        functions.when(target.lt(min), min)
                                .when(target.gt(max), max)
                                .otherwise(target)
                );
            case 2:
                return dataset.withColumn(col,
                        functions.when(target.lt(min).or(target.gt(max)), min).otherwise(target)
                );
            case 3:
                return dataset.withColumn(col,
                        functions.when(target.lt(min).or(target.gt(max)), max).otherwise(target)
                );
            default:
                return dataset;
        }
    }

    /**
     * Regular expression replacement
     */
    private Dataset<Row> applyRegexReplace(Dataset<Row> dataset, JSONObject cfg) {
        String col = cfg.getJSONArray("columns").getString(0);
        String regex = cfg.getString("regex");
        String replacement = cfg.getString("replacement");
        return dataset.withColumn(col, functions.regexp_replace(dataset.col(col), regex, replacement));
    }

    /**
     * Delete the combined field if it is empty
     */
    private Dataset<Row> applyDeleteIfAllNull(Dataset<Row> dataset, JSONObject cfg) {
        List<String> cols = cfg.getJSONArray("columns").toJavaList(String.class);
        if (cols == null || cols.isEmpty()) return dataset;

        // Column existence check
        Set<String> exists = new HashSet<>(Arrays.asList(dataset.columns()));
        List<String> missing = cols.stream().filter(c -> !exists.contains(c)).collect(Collectors.toList());
        if (!missing.isEmpty()) {
            throw new IllegalArgumentException(MessageUtils.messageEn("etl.error.combine.null.columns", missing));
        }

        StructType schema = dataset.schema();
        Column allNullCond = functions.lit(true);

        for (String c : cols) {
            DataType t = schema.apply(c).dataType();

            // NULL judgment
            Column isNull = functions.col(c).isNull();

            // Blank string judgment (convert to string first and then trim)
            Column isBlank = functions.trim(functions.col(c).cast("string")).equalTo("");

            // Value NaN judgment
            Column isEmpty = isNull.or(isBlank);
            if (t.sameType(DataTypes.FloatType) || t.sameType(DataTypes.DoubleType)) {
                isEmpty = isEmpty.or(functions.isnan(functions.col(c)));
            }

            allNullCond = allNullCond.and(isEmpty);
        }

        return dataset.filter(functions.not(allNullCond));
    }

    /**
     * Convert field values to uppercase
     */
    private Dataset<Row> applyUpperCase(Dataset<Row> dataset, JSONObject cfg) {
        String col = cfg.getJSONArray("columns").getString(0);
        return dataset.withColumn(col, functions.upper(dataset.col(col)));
    }

    /**
     * Convert field values to lowercase
     */
    private Dataset<Row> applyLowerCase(Dataset<Row> dataset, JSONObject cfg) {
        String col = cfg.getJSONArray("columns").getString(0);
        return dataset.withColumn(col, functions.lower(dataset.col(col)));
    }

    /**
     * DATE_FORMAT_STD - unified date format (supports "timestamp" in inputFormats)
     * - Normal mode: use to_timestamp(asStr, pattern)
     * - Timestamp mode: "timestamp" is treated as a Unix timestamp; automatically compatible with 10 digits (seconds) and 13 digits (milliseconds)
     *
     * cfg example:
     * {
     *   "columns": ["bizDate"],
     *   "targetFormat": "yyyy-MM-dd",
     *   "inputFormats": ["yyyyMMdd","yyyy-MM-dd","yyyy/MM/dd","yyyy.MM.dd","yyyy-MM-dd HH:mm:ss","timestamp"]
     * }
     */
    private Dataset<Row> applyDateFormatStd(Dataset<Row> dataset, JSONObject cfg) {

        System.out.println("序列填充后的字段结构：");
        dataset.printSchema();

        System.out.println("前10条数据：");
        dataset.show(10, false);

        String colName = cfg.getJSONArray("columns").getString(0);
        String targetFmt = cfg.getString("targetFormat"); // Only takes effect when the output is string
        JSONArray arr = cfg.getJSONArray("inputFormats");

        Column col = functions.col(colName);
        DataType dt = dataset.schema().apply(colName).dataType();

        // === 1) Get asTs uniformly: parse only when needed ===
        Column asTs;

        // 1.1 The source column is already a date/time stamp: use it directly to avoid secondary time zone conversion
        if (dt.sameType(DataTypes.DateType)) {
            asTs = col.cast("timestamp");
        } else if (dt.sameType(DataTypes.TimestampType)) {
            asTs = col; // Already timestamp
        }
        // 1.2 The source column is a string: use inputFormats to try to parse
        else if (dt.sameType(DataTypes.StringType)) {
            Column asStr = col.cast("string");
            Column parsed = null;
            for (int i = 0; i < (arr == null ? 0 : arr.size()); i++) {
                String pattern = arr.getString(i);
                Column p;
                if ("timestamp".equalsIgnoreCase(pattern)) {
                    // Numeric timestamp string: 10-digit seconds, 13-digit milliseconds, other lengths are considered invalid
                    Column digits = functions.regexp_replace(asStr, "[^0-9]", "");
                    Column len = functions.length(digits);
                    Column seconds = functions.when(len.equalTo(13), digits.cast("double").divide(1000.0))
                            .when(len.equalTo(10), digits.cast("double"))
                            .otherwise(functions.lit((Double) null));
                    p = functions.to_timestamp(functions.from_unixtime(seconds));
                } else {
                    p = functions.to_timestamp(asStr, pattern);
                }
                parsed = (parsed == null) ? p : functions.coalesce(parsed, p);
            }
            asTs = parsed; // Null on all failures
        }
        // 1.3 The source column is numeric: judge seconds/milliseconds according to the threshold
        else if (dt.equals(DataTypes.IntegerType) || dt.equals(DataTypes.LongType)
                || dt.equals(DataTypes.ShortType)   || dt.equals(DataTypes.FloatType)
                || dt.equals(DataTypes.DoubleType)  || dt.typeName().startsWith("decimal")) {
            Column num = col.cast("double");
            // >=1e12 is regarded as milliseconds; between [1e9,1e12) is regarded as seconds; otherwise it is invalid
            Column seconds = functions.when(num.geq(1_000_000_000_000.0), num.divide(1000.0))
                    .when(num.geq(1_000_000_000.0),      num)
                    .otherwise(functions.lit((Double) null));
            asTs = functions.to_timestamp(functions.from_unixtime(seconds));
        }
        // 1.4 Other types: give up parsing
        else {
            asTs = functions.lit(null);
        }

        // === 2) Determine the output type: forceTargetType first, followed by the original column type, default string ===
        String targetType;
        if (targetFmt != null && !targetFmt.trim().isEmpty()) {
            targetType = targetFmt.trim().toLowerCase();
        } else if (dt.sameType(DataTypes.DateType)) {
            targetType = "date";
        } else if (dt.sameType(DataTypes.TimestampType)) {
            targetType = "timestamp";
        } else if (dt.sameType(DataTypes.StringType)) {
            targetType = "string";
        } else {
            targetType = "string";
        }

        // === 3) Implementation: Replace only if parsing is successful, otherwise keep the original value ===
        Column outCol;
        switch (targetType) {
            case "date":
                outCol = functions.when(asTs.isNotNull(), asTs.cast("date"))
                        .otherwise(col);
                break;
            case "timestamp":
                outCol = functions.when(asTs.isNotNull(), asTs.cast("timestamp"))
                        .otherwise(col);
                break;
            default: // "string"
                Column formatted = (targetFmt == null || targetFmt.isEmpty())
                        ? functions.date_format(asTs, "yyyy-MM-dd HH:mm:ss")
                        : functions.date_format(asTs, targetFmt);
                outCol = functions.when(asTs.isNotNull(), formatted)
                        .otherwise(col.cast("string")); // Unify into string
                break;
        }
        dataset = dataset.withColumn(colName, outCol);

        System.out.println("序列填充后的字段结构：");
        dataset.printSchema();

        System.out.println("前10条数据：");
        dataset.show(10, false);

        return dataset;
    }


    public static Dataset<Row> transitionOld(Dataset<Row> dataset, JSONObject transition, LogUtils.Params logParams) {
        LogUtils.writeLog(logParams, "*********************************  Initialize task context  ***********************************");
        LogUtils.writeLog(logParams, MessageUtils.messageEn("etl.transition.old.version"));
        return dataset;
    }
}
