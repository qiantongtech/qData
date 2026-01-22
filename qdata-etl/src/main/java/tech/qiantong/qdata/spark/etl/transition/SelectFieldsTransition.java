package tech.qiantong.qdata.spark.etl.transition;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataTypes;
import tech.qiantong.qdata.common.enums.TaskComponentTypeEnum;
import tech.qiantong.qdata.spark.etl.utils.LogUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.alibaba.fastjson2.JSONWriter.Feature.PrettyFormat;

/**
 * 字段
 */
public class SelectFieldsTransition implements Transition {

    @Override
    public String code() {
        return TaskComponentTypeEnum.SELECT_FIELDS.getCode();
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
        LogUtils.writeLog(logParams, "开始设置字段值节点");
        LogUtils.writeLog(logParams, "开始任务时间: " + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
        LogUtils.writeLog(logParams, "任务参数：" + transition.toJSONString(PrettyFormat));

        JSONObject parameter = transition.getJSONObject("parameter");

        JSONArray tableFields = parameter.getJSONArray("tableFields");
        if (tableFields == null || tableFields.isEmpty()) {
            throw new IllegalArgumentException("字段设置列表不能为空且必须为非空数组！");
        }

        JSONArray removeFields = parameter.getJSONArray("removeFields");

        // 原始列集合，便于做存在性校验与 keepOthers
        Set<String> originalCols = new HashSet<>(Arrays.asList(dataset.columns()));
        Set<String> outputNames = new HashSet<>();

        Dataset<Row> result = dataset;

        // 1. 字段选择 + 重命名 + 类型转换
        List<Column> selectedColumns = new ArrayList<>();
        Set<String> renameTargets = new HashSet<>();

        for (int i = 0; i < tableFields.size(); i++) {
            JSONObject field = tableFields.getJSONObject(i);
            String inputField = field.getString("inputField");
            String outputField = field.getString("outputField");
            String type = field.getString("type");
            Integer length = field.getInteger("length");      // 长度，精度
            Integer scale = field.getInteger("precision");   // 长度，精度

            if (outputField == null || outputField.trim().isEmpty()) {
                throw new IllegalArgumentException("输出字段不能为空");
            }
            if (outputNames.contains(outputField)) {
                throw new IllegalArgumentException("输出字段重复: " + outputField);
            }


            Column col = functions.col(inputField);

            // 类型转化
            if (type != null) {
                switch (type.toLowerCase()) {
                    case "string":
                        col = col.cast(DataTypes.StringType);
                        break;
                    case "int":
                    case "integer":
                        col = col.cast(DataTypes.IntegerType);
                        break;
                    case "long":
                        col = col.cast(DataTypes.LongType);
                        break;
                    case "double":
                        col = col.cast(DataTypes.DoubleType);
                        break;
                    case "boolean":
                        col = col.cast(DataTypes.BooleanType);
                        break;
                    case "date":
                        col = col.cast(DataTypes.DateType);
                        break;
                    case "timestamp":
                        col = col.cast(DataTypes.TimestampType);
                        break;
                    default:
                        // 不处理未知类型
                }
            }

            // 精度、长度 处理
            if (length != null || scale != null) {
                switch (type.toLowerCase()) {
                    case "string":
                        if (length != null && length > 0) {
                            col = functions.substring(col, 1, length);
                        }
                        // string 不处理 scale
                        break;
                    case "double":
                        if (scale != null && scale >= 0) {
                            col = functions.bround(col, scale);
                        }
                        // double 不处理 length
                        break;
                    case "int":
                    case "integer":
                        col = col.cast(DataTypes.IntegerType);
                        if (length != null && length > 0) {
                            // 简单范围校验（如 length=3 表示允许 -999 ~ 999）
                            int maxVal = (int) Math.pow(10, length) - 1;
                            int minVal = -maxVal;
                            col = functions.when(col.gt(maxVal), maxVal)
                                    .when(col.lt(minVal), minVal)
                                    .otherwise(col);
                        }
                        break;
                    case "long":
                        col = col.cast(DataTypes.LongType);
                        if (length != null && length > 0) {
                            long maxVal = (long) Math.pow(10, length) - 1;
                            long minVal = -maxVal;
                            col = functions.when(col.gt(maxVal), maxVal)
                                    .when(col.lt(minVal), minVal)
                                    .otherwise(col);
                        }
                        break;

                    // 其他数值/日期/布尔类型通常不做 length/scale 约束
                    default:
                        // no-op
                }
            }


            if (!inputField.equals(outputField)) {
                col = col.alias(outputField);
            }

            selectedColumns.add(col);
            renameTargets.add(outputField);
        }

        // 2. 执行 select 保留未声明的其他原始列 + 追加选定列（可能重名，后者会覆盖）
        List<String> otherCols = originalCols.stream()
                .filter(c -> !outputNames.contains(c))
                .collect(Collectors.toList());
        result = dataset.selectExpr(otherCols.toArray(new String[0]));
        for (int i = 0; i < selectedColumns.size(); i++) {
            Column c = selectedColumns.get(i);
            String outName = ((org.apache.spark.sql.catalyst.expressions.Alias) c.expr()).name();
            result = result.withColumn(outName, c);
        }

        // 3. 删除字段
        if (removeFields != null && !removeFields.isEmpty()) {
            for (int i = 0; i < removeFields.size(); i++) {
                String fieldName = removeFields.getJSONObject(i).getString("inputField");
                if (fieldName != null && result.columns() != null && Arrays.asList(result.columns()).contains(fieldName)) {
                    result = result.drop(fieldName);
                }
            }
        }

        result.printSchema();
        result.show(10, false);

        return result;
    }
}
