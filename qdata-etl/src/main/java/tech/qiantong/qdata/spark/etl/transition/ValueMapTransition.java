package tech.qiantong.qdata.spark.etl.transition;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.spark.sql.*;
import tech.qiantong.qdata.common.enums.TaskComponentTypeEnum;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.spark.etl.utils.LogUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.alibaba.fastjson2.JSONWriter.Feature.PrettyFormat;

/**
 * Value mapping
 */
public class ValueMapTransition implements Transition {

    @Override
    public String code() {
        return TaskComponentTypeEnum.VALUE_MAP.getCode();
    }

    /**
     * @param spark
     * @param dataset
     * @param transition
     * @param logParams
     * @return
     */
    @Override
    public Dataset<Row> transition(SparkSession spark, Dataset<Row> dataset, JSONObject transition, LogUtils.Params logParams) {
        LogUtils.writeLog(logParams, "*********************************  Initialize task context  ***********************************");
        LogUtils.writeLog(logParams, "Starting value mapping node");
        LogUtils.writeLog(logParams, "Task start time: " + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
        LogUtils.writeLog(logParams, "Task parameters: " + transition.toJSONString(PrettyFormat));
        JSONObject parameter = transition.getJSONObject("parameter");


        String inputField = parameter.getString("inputField");
        String outputField = parameter.getString("outputField");
        String defaultValue = parameter.getString("defaultValue");
        JSONArray tableFields = parameter.getJSONArray("tableFields");

        // Verify the legality of parameters
        if (StringUtils.isEmpty(inputField)) {
            throw new IllegalArgumentException(MessageUtils.messageWithFallback(
                    "etl.error.value.map.source.field.required", "The source field name is required"));
        }
        if (StringUtils.isEmpty(outputField)) {
            throw new IllegalArgumentException(MessageUtils.messageWithFallback(
                    "etl.error.value.map.target.field.required", "The target field is required"));
        }
//        if (StringUtils.isEmpty(defaultValue)) {
// throw new IllegalArgumentException("The default value when not matching cannot be empty!");
//        }
        if (tableFields == null || tableFields.isEmpty()) {
            throw new IllegalArgumentException(MessageUtils.messageWithFallback(
                    "etl.error.value.map.list.invalid", "The value mapping list must be a non-empty array"));
        }


        // Construct mapping table Map<original value, mapped value>
        Map<String, String> mappingMap = new HashMap<>();
        for (int i = 0; i < tableFields.size(); i++) {
            JSONObject mapItem = tableFields.getJSONObject(i);
            mappingMap.put(mapItem.getString("source"), mapItem.getString("target"));
        }
        LogUtils.writeLog(logParams, "Task parameters: " + transition.toJSONString(PrettyFormat));

        // Constructing when...otherwise expressions
        Column mappedColumn = null;
        for (Map.Entry<String, String> entry : mappingMap.entrySet()) {
            Column condition = functions.col(inputField).equalTo(entry.getKey());
            Column result = functions.lit(entry.getValue());
            mappedColumn = mappedColumn == null ? functions.when(condition, result)
                    : mappedColumn.when(condition, result);
        }

        // Set default value
        if (defaultValue != null) {
            mappedColumn = mappedColumn.otherwise(functions.lit(defaultValue));
        } else {
            mappedColumn = mappedColumn.otherwise(functions.col(inputField));
        }

        // Add mapping column
        Dataset<Row> result;
        if (inputField.equals(outputField)) {
            result = dataset.withColumn(inputField, mappedColumn);
        } else {
            result = dataset.withColumn(outputField, mappedColumn);
        }


        result.printSchema();
        result.show(10, false);

        return result;
    }
}
