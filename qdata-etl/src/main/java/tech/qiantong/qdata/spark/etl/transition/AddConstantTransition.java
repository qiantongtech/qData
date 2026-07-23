package tech.qiantong.qdata.spark.etl.transition;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataTypes;
import tech.qiantong.qdata.common.enums.TaskComponentTypeEnum;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.spark.etl.utils.LogUtils;
import tech.qiantong.qdata.spark.etl.utils.ValueParserUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static com.alibaba.fastjson2.JSONWriter.Feature.PrettyFormat;

/**
 * Add constant
 */
public class AddConstantTransition implements Transition {

    @Override
    public String code() {
        return TaskComponentTypeEnum.ADD_CONSTANT.getCode();
    }

    /**
     *
     * @param spark
     * @param dataset
     * @param transition
     * @param logParams
     * @return
     */
    @Override
    public Dataset<Row> transition(SparkSession spark, Dataset<Row> dataset, JSONObject transition, LogUtils.Params logParams) {
        LogUtils.writeLog(logParams, "*********************************  Initialize task context  ***********************************");
        LogUtils.writeLog(logParams, MessageUtils.messageEn("etl.transition.constant.start"));
        LogUtils.writeLog(logParams, MessageUtils.messageEn("etl.task.start.time", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS")));
        LogUtils.writeLog(logParams, MessageUtils.messageEn("etl.task.parameters", transition.toJSONString(PrettyFormat)));
        JSONObject parameter = transition.getJSONObject("parameter");


        JSONArray tableFields = parameter.getJSONArray("tableFields");
        if (tableFields == null || tableFields.isEmpty()) {
            throw new IllegalArgumentException(MessageUtils.messageEn("etl.error.fields.empty"));
        }

        // Get a collection of existing column names to prevent repeated additions
        Set<String> existingColumns = new HashSet<>();
        for (String col : dataset.columns()) {
            existingColumns.add(col);
        }
        Dataset<Row> result = dataset;

        for (int i = 0; i < tableFields.size(); i++) {
            JSONObject field = tableFields.getJSONObject(i);
            // Field name
            String name = field.getString("name");
            // Field type
            String type = field.getString("type");
            // Default value
            String defaultValue = field.getString("defaultValue");
            // Whether it is an empty string
            boolean emptyString = field.getBooleanValue("emptyString");

            LogUtils.writeLog(logParams, "name：" + name);
            LogUtils.writeLog(logParams, "type：" + type);
            LogUtils.writeLog(logParams, "defaultValue：" + defaultValue);
            LogUtils.writeLog(logParams, "emptyString：" + emptyString);


            // ❗️Determine whether the field already exists, skip it if it exists
            if (existingColumns.contains(name)) {
                continue;
            }

            Column newCol;
            if (emptyString) {
                newCol = functions.lit("");
            } else {
                switch (type.toLowerCase()) {
                    case "string":
                        newCol = functions.lit(defaultValue);
                        break;
                    case "int":
                    case "integer":
                        newCol = functions.lit(ValueParserUtils.parseInt(defaultValue));
                        break;
                    case "long":
                        newCol = functions.lit(ValueParserUtils.parseLong(defaultValue));
                        break;
                    case "double":
                        newCol = functions.lit(ValueParserUtils.parseDouble(defaultValue));
                        break;
                    case "boolean":
                        newCol = functions.lit(ValueParserUtils.parseBoolean(defaultValue));
                        break;
                    case "date":
                        String dateStr = defaultValue.isEmpty()
                                ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                                : defaultValue;
                        newCol = functions.lit(dateStr).cast(DataTypes.TimestampType);
                        break;
                    default:
                        newCol = functions.lit(defaultValue); // Processed by string by default
                        break;
                }
            }
            result = result.withColumn(name, newCol);
        }

        result.printSchema();
        result.show(10, false);

        return result;
    }
}
