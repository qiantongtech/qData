package tech.qiantong.qdata.spark.etl.transition;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataTypes;
import tech.qiantong.qdata.common.enums.TaskComponentTypeEnum;
import tech.qiantong.qdata.spark.etl.utils.LogUtils;
import tech.qiantong.qdata.spark.etl.utils.ValueParserUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static com.alibaba.fastjson2.JSONWriter.Feature.PrettyFormat;

/**
 * 增加常量
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
        LogUtils.writeLog(logParams, "开始增加常量节点");
        LogUtils.writeLog(logParams, "开始任务时间: " + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
        LogUtils.writeLog(logParams, "任务参数：" + transition.toJSONString(PrettyFormat));
        JSONObject parameter = transition.getJSONObject("parameter");


        JSONArray tableFields = parameter.getJSONArray("tableFields");
        if (tableFields == null || tableFields.isEmpty()) {
            throw new IllegalArgumentException("常量字段列表不能为空且必须为非空数组！");
        }

        // 获取已有列名集合，防止重复添加
        Set<String> existingColumns = new HashSet<>();
        for (String col : dataset.columns()) {
            existingColumns.add(col);
        }
        Dataset<Row> result = dataset;

        for (int i = 0; i < tableFields.size(); i++) {
            JSONObject field = tableFields.getJSONObject(i);
            // 字段名称
            String name = field.getString("name");
            // 字段类型
            String type = field.getString("type");
            // 默认值
            String defaultValue = field.getString("defaultValue");
            // 是否为空字符串
            boolean emptyString = field.getBooleanValue("emptyString");

            LogUtils.writeLog(logParams, "name：" + name);
            LogUtils.writeLog(logParams, "type：" + type);
            LogUtils.writeLog(logParams, "defaultValue：" + defaultValue);
            LogUtils.writeLog(logParams, "emptyString：" + emptyString);


            // ❗️判断字段是否已存在，存在则跳过
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
                        newCol = functions.lit(defaultValue); // 默认按字符串处理
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
