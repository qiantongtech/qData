package tech.qiantong.qdata.spark.etl.transition;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.spark.sql.*;
import tech.qiantong.qdata.common.enums.TaskComponentTypeEnum;
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
        LogUtils.writeLog(logParams, "开始值映射节点");
        LogUtils.writeLog(logParams, "开始任务时间: " + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
        LogUtils.writeLog(logParams, "任务参数：" + transition.toJSONString(PrettyFormat));
        JSONObject parameter = transition.getJSONObject("parameter");


        String inputField = parameter.getString("inputField");
        String outputField = parameter.getString("outputField");
        String defaultValue = parameter.getString("defaultValue");
        JSONArray tableFields = parameter.getJSONArray("tableFields");

        // Verify the legality of parameters
        if (StringUtils.isEmpty(inputField)) {
            throw new IllegalArgumentException("使用的字段名称不能为空！");
        }
        if (StringUtils.isEmpty(outputField)) {
            throw new IllegalArgumentException("目标字段不能为空！");
        }
//        if (StringUtils.isEmpty(defaultValue)) {
// throw new IllegalArgumentException("The default value when not matching cannot be empty!");
//        }
        if (tableFields == null || tableFields.isEmpty()) {
            throw new IllegalArgumentException("值映射列表不能为空且必须为非空数组！");
        }


        // Construct mapping table Map<original value, mapped value>
        Map<String, String> mappingMap = new HashMap<>();
        for (int i = 0; i < tableFields.size(); i++) {
            JSONObject mapItem = tableFields.getJSONObject(i);
            mappingMap.put(mapItem.getString("source"), mapItem.getString("target"));
        }
        LogUtils.writeLog(logParams, "任务参数：" + transition.toJSONString(PrettyFormat));

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
