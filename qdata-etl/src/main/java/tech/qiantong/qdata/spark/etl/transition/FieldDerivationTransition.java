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
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.spark.sql.*;
import tech.qiantong.qdata.common.enums.TaskComponentTypeEnum;
import tech.qiantong.qdata.common.enums.etl.transition.FieldDerivationTypeEnum;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.spark.etl.utils.LogUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.alibaba.fastjson2.JSONWriter.Feature.PrettyFormat;

/**
 * Field Derivator
 */
public class FieldDerivationTransition implements Transition {

    @Override
    public String code() {
        return TaskComponentTypeEnum.FIELD_DERIVATION.getCode();
    }

    /**
     *
     * @param spark
     * @param dataset
     * @param transition
     * @param logPath
     * @return
     */
    @Override
    public Dataset<Row> transition(SparkSession spark, Dataset<Row> dataset, JSONObject transition, LogUtils.Params logPath) {
        LogUtils.writeLog(logPath, "*********************************  Initialize task context  ***********************************");
        LogUtils.writeLog(logPath, "Starting field derivation node");
        LogUtils.writeLog(logPath, "Task start time: " + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
        LogUtils.writeLog(logPath, "Task parameters: " + transition.toJSONString(PrettyFormat));
        JSONObject parameter = transition.getJSONObject("parameter");

        //Field derived type
        String fieldDerivationType = MapUtils.getString(parameter,"fieldDerivationType");

        // Verification
        if (StringUtils.isEmpty(fieldDerivationType)) {
            throw new IllegalArgumentException(MessageUtils.messageWithFallback(
                    "etl.error.field.derivation.type.required", "Field derivation type is required"));
        }

        FieldDerivationTypeEnum typeEnum = FieldDerivationTypeEnum.fromCode(fieldDerivationType);

        switch (typeEnum) {
            case FIELD_DERIVE_CONCAT:
                // Splicing processing logic
                return handleConcat(parameter,dataset,logPath);
            case FIELD_DERIVE_SUBSTRING:
                // Interception processing logic
                return handleSubstring(parameter,dataset,logPath);
            case FIELD_DERIVE_REPLACE:
                // Replace processing logic
                return handleReplace();
            case FIELD_DERIVE_EXPRESSION:
                // Expression processing logic
                return handleExpression();
            case FIELD_DERIVE_HASH:
                // Hash processing logic
                return handleHash();
            case FIELD_DERIVE_REGEX:
                // Regular extraction processing logic
                return handleRegex();
            case FIELD_DERIVE_CONSTANT:
                // Constant assignment processing logic
                return handleConstant();
            default:
                throw new IllegalArgumentException(MessageUtils.messageWithFallback(
                        "etl.error.field.derivation.type.unknown", "Unknown field derivation type: {0}", fieldDerivationType));
        }
    }

    private Dataset<Row> handleConstant() {
        return null;
    }

    private Dataset<Row> handleRegex() {
        return null;
    }

    private Dataset<Row> handleHash() {
        return null;
    }

    private Dataset<Row> handleExpression() {
        return null;
    }

    private Dataset<Row> handleReplace() {
        return null;
    }

    /**
     *
     * {
     *   "fieldDerivationType": "FIELD_DERIVE_SUBSTRING",
     *   "fieldDerivationName": "phone_suffix",
     *   "direction": "FROM_END",                // FROM_START | FROM_END
     *   "startIndex": 4,
     * "endIndex": 8, // Optional, leaving endIndex blank means cutting to the end
     *   "tableFields": [
     *     { "columnName": "phone" }
     *   ]
     * }
     * @return
     */
    private Dataset<Row> handleSubstring(JSONObject parameter, Dataset<Row> dataset, LogUtils.Params logParams) {
        LogUtils.writeLog(logParams, "Starting field derivation substring processing");
        LogUtils.writeLog(logParams, "Task start time: " + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));

        //The field names stored after merging the rule configuration are stored in
        String fieldDerivationName = MapUtils.getString(parameter,"fieldDerivationName");
        //Interception identifier: 1 starts from the front | 2 starts from the back
        String direction = MapUtils.getString(parameter,"direction");

        //Prefix symbol
        Integer startIndex = MapUtils.getInteger(parameter,"startIndex",null);
        Integer endIndex = MapUtils.getInteger(parameter,"endIndex",null);

        //The selected field---only one is stored, you can directly enter the 0 subscript
        List<Map<String, Object>> tableFields = (List<Map<String, Object>>) MapUtils.getObject(parameter, "tableFields");

        //Inspect
        if (CollectionUtils.isEmpty(tableFields)) {
            throw new IllegalArgumentException(MessageUtils.messageWithFallback(
                    "etl.error.calculation.field.required", "A field for calculation is required"));
        }
        if (StringUtils.isEmpty(fieldDerivationName)) {
            throw new IllegalArgumentException(MessageUtils.messageWithFallback(
                    "etl.error.storage.field.name.required", "The field name to store is required"));
        }
        if (StringUtils.isEmpty(direction)) {
            throw new IllegalArgumentException(MessageUtils.messageWithFallback(
                    "etl.error.substring.type.required", "Substring type is required"));
        }
        if (startIndex == null) {
            throw new IllegalArgumentException(MessageUtils.messageWithFallback(
                    "etl.error.substring.type.required", "Substring type is required"));
        }

        String columnName = MapUtils.getString(tableFields.get(0), "columnName");
        if (StringUtils.isEmpty(columnName)) {
            throw new IllegalArgumentException(MessageUtils.messageWithFallback(
                    "etl.error.substring.field.required", "The field to substring is required"));
        }

        Column sourceCol = functions.col(columnName).cast("string");

        Column derivedCol;

        if ("1".equalsIgnoreCase(direction)) {
            // Start from before: substring(col, start, length)
            if (endIndex != null) {
                int length = endIndex - startIndex;
                if (length < 0) {
            throw new IllegalArgumentException(MessageUtils.messageWithFallback(
                    "etl.error.substring.index.invalid", "endIndex must be greater than startIndex"));
                }
                derivedCol = functions.expr("substring(" + columnName + ", " + (startIndex + 1) + ", " + length + ")");
            } else {
                // To the end: length is infinity
                derivedCol = functions.expr("substring(" + columnName + ", " + (startIndex + 1) + ")");
            }
        } else if ("2".equalsIgnoreCase(direction)) {
            // Starting from the end: first calculate the string length minus startIndex, then intercept length
            // Example: substr(phone, length(phone) - startIndex + 1, length)
            Column lengthCol = functions.length(sourceCol);
            Column startPos = lengthCol.minus(startIndex).plus(1);

            if (endIndex != null) {
                int length = endIndex - startIndex;
                if (length < 0) {
            throw new IllegalArgumentException(MessageUtils.messageWithFallback(
                    "etl.error.substring.index.invalid", "endIndex must be greater than startIndex"));
                }
                derivedCol = functions.expr("substring(" + columnName + ", length(" + columnName + ") - " + startIndex + " + 1, " + length + ")");
            } else {
                derivedCol = functions.expr("substring(" + columnName + ", length(" + columnName + ") - " + startIndex + " + 1)");
            }
        } else {
                throw new IllegalArgumentException(MessageUtils.messageWithFallback(
                        "etl.error.direction.unsupported", "Unsupported direction type: {0}", direction));
        }

        Dataset<Row> result = dataset.withColumn(fieldDerivationName, derivedCol);

        // Print results for debugging
        result.printSchema();
        result.show(10, false);

        return result;
    }

    /**
     * Splicing processing logic
     * {
     * "fieldDerivationType":"The passed field name"
     * ,"fieldDerivationName":"Save the field name stored after merging the rule configuration"
     * ,"delimiter":"connector"
     *   ,"tableFields":[
     * {"columnName":"Field name for splicing"}
     *   ]
     * ,"fieldDerivationPrefix":"prefix"
     * ,"fieldDerivationSuffix":"Suffix"
     * }
     * @return
     */
    private Dataset<Row> handleConcat(JSONObject parameter, Dataset<Row> dataset, LogUtils.Params logPath) {
        LogUtils.writeLog(logPath, "Starting field derivation concatenation processing");
        LogUtils.writeLog(logPath, "Task start time: " + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));

        //The field names stored after merging the rule configuration are stored in
        String fieldDerivationName = MapUtils.getString(parameter,"fieldDerivationName");
        //Linker
        String delimiter = MapUtils.getString(parameter,"delimiter","");
        //Prefix symbol
        String prefix = MapUtils.getString(parameter,"fieldDerivationPrefix","");
        //Suffix symbol
        String suffix = MapUtils.getString(parameter,"fieldDerivationSuffix","");
        //Selected fields
        List<Map<String, Object>> tableFields = (List<Map<String, Object>>) MapUtils.getObject(parameter, "tableFields");

        //Inspect
        if (CollectionUtils.isEmpty(tableFields)) {
            throw new IllegalArgumentException(MessageUtils.messageWithFallback(
                    "etl.error.calculation.field.required", "A field for calculation is required"));
        }
        if (StringUtils.isEmpty(fieldDerivationName)) {
            throw new IllegalArgumentException(MessageUtils.messageWithFallback(
                    "etl.error.storage.field.name.required", "The field name to store is required"));
        }

        // Construct a field list, convert it to a string, and use "null" as a placeholder
        List<Column> columns = tableFields.stream()
                .map(field -> {
                    String colName = MapUtils.getString(field, "columnName");
                    return StringUtils.isNotEmpty(colName)
                            ? functions.coalesce(functions.col(colName).cast("string"), functions.lit("null"))
                            : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (columns.isEmpty()) {
            throw new IllegalArgumentException(MessageUtils.messageWithFallback(
                    "etl.error.concat.fields.empty", "No valid fields are available for concatenation"));
        }

        // Use delimiter to splice field values
        Column concatCol = functions.concat_ws(delimiter, columns.toArray(new Column[0]));

        // Add prefixes and suffixes
        concatCol = functions.concat_ws("", functions.lit(prefix), concatCol, functions.lit(suffix));

        // Add derived field column
        Dataset<Row> rowDataset = dataset.withColumn(fieldDerivationName, concatCol);

        // Debug log
        System.out.println("Field schema after concatenation: ");
        rowDataset.printSchema();

        System.out.println("First 10 rows after concatenation: ");
        rowDataset.show(10, false);

        return rowDataset;
    }
}
