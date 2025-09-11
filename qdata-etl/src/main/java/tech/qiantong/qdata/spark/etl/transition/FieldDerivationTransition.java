package tech.qiantong.qdata.spark.etl.transition;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.spark.sql.*;
import tech.qiantong.qdata.common.enums.TaskComponentTypeEnum;
import tech.qiantong.qdata.common.enums.etl.transition.FieldDerivationTypeEnum;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.spark.etl.utils.LogUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.alibaba.fastjson2.JSONWriter.Feature.PrettyFormat;

/**
 * 字段派生器
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
        LogUtils.writeLog(logPath, "开始字段派生器节点");
        LogUtils.writeLog(logPath, "开始任务时间: " + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
        LogUtils.writeLog(logPath, "任务参数：" + transition.toJSONString(PrettyFormat));
        JSONObject parameter = transition.getJSONObject("parameter");

        //字段派生类型
        String fieldDerivationType = MapUtils.getString(parameter,"fieldDerivationType");

        // 校验
        if (StringUtils.isEmpty(fieldDerivationType)) {
            throw new IllegalArgumentException("字段派生类型不能为空！");
        }

        FieldDerivationTypeEnum typeEnum = FieldDerivationTypeEnum.fromCode(fieldDerivationType);

        switch (typeEnum) {
            case FIELD_DERIVE_CONCAT:
                // 拼接处理逻辑
                return handleConcat(parameter,dataset,logPath);
            case FIELD_DERIVE_SUBSTRING:
                // 截取处理逻辑
                return handleSubstring(parameter,dataset,logPath);
            case FIELD_DERIVE_REPLACE:
                // 替换处理逻辑
                return handleReplace();
            case FIELD_DERIVE_EXPRESSION:
                // 表达式处理逻辑
                return handleExpression();
            case FIELD_DERIVE_HASH:
                // 哈希处理逻辑
                return handleHash();
            case FIELD_DERIVE_REGEX:
                // 正则提取处理逻辑
                return handleRegex();
            case FIELD_DERIVE_CONSTANT:
                // 常量赋值处理逻辑
                return handleConstant();
            default:
                throw new IllegalArgumentException("未知的字段派生类型: " + fieldDerivationType);
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
     *   "endIndex": 8,                          // 可选，endIndex 不填表示截取到末尾
     *   "tableFields": [
     *     { "columnName": "phone" }
     *   ]
     * }
     * @return
     */
    private Dataset<Row> handleSubstring(JSONObject parameter, Dataset<Row> dataset, LogUtils.Params logParams) {
        LogUtils.writeLog(logParams, "开始字段派生-截取处理");
        LogUtils.writeLog(logParams, "开始任务时间: " + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));

        //规则配置的合并后存储的字段名字存入
        String fieldDerivationName = MapUtils.getString(parameter,"fieldDerivationName");
        //截取标识符：  1 从前开始  ｜  2 从后开始
        String direction = MapUtils.getString(parameter,"direction");

        //前缀符号
        Integer startIndex = MapUtils.getInteger(parameter,"startIndex",null);
        Integer endIndex = MapUtils.getInteger(parameter,"endIndex",null);

        //选择的字段---只存储了一个，可以直接区 0下标
        List<Map<String, Object>> tableFields = (List<Map<String, Object>>) MapUtils.getObject(parameter, "tableFields");

        //检验
        if (CollectionUtils.isEmpty(tableFields)) {
            throw new IllegalArgumentException("进行计算的字段不能为空！");
        }
        if (StringUtils.isEmpty(fieldDerivationName)) {
            throw new IllegalArgumentException("需要存储的字段名称不能为空！");
        }
        if (StringUtils.isEmpty(direction)) {
            throw new IllegalArgumentException("需要截取的类型不能为空！");
        }
        if (startIndex == null) {
            throw new IllegalArgumentException("需要截取的类型不能为空！");
        }

        String columnName = MapUtils.getString(tableFields.get(0), "columnName");
        if (StringUtils.isEmpty(columnName)) {
            throw new IllegalArgumentException("需要截取的字段名不能为空！");
        }

        Column sourceCol = functions.col(columnName).cast("string");

        Column derivedCol;

        if ("1".equalsIgnoreCase(direction)) {
            // 从前开始： substring(col, start, length)
            if (endIndex != null) {
                int length = endIndex - startIndex;
                if (length < 0) {
                    throw new IllegalArgumentException("endIndex 必须大于 startIndex");
                }
                derivedCol = functions.expr("substring(" + columnName + ", " + (startIndex + 1) + ", " + length + ")");
            } else {
                // 到结尾：length 无穷大
                derivedCol = functions.expr("substring(" + columnName + ", " + (startIndex + 1) + ")");
            }
        } else if ("2".equalsIgnoreCase(direction)) {
            // 从后开始：先计算字符串长度减去 startIndex，然后截取 length
            // 例：substr(phone, length(phone) - startIndex + 1, length)
            Column lengthCol = functions.length(sourceCol);
            Column startPos = lengthCol.minus(startIndex).plus(1);

            if (endIndex != null) {
                int length = endIndex - startIndex;
                if (length < 0) {
                    throw new IllegalArgumentException("endIndex 必须大于 startIndex");
                }
                derivedCol = functions.expr("substring(" + columnName + ", length(" + columnName + ") - " + startIndex + " + 1, " + length + ")");
            } else {
                derivedCol = functions.expr("substring(" + columnName + ", length(" + columnName + ") - " + startIndex + " + 1)");
            }
        } else {
            throw new IllegalArgumentException("不支持的方向类型：" + direction);
        }

        Dataset<Row> result = dataset.withColumn(fieldDerivationName, derivedCol);

        // 打印结果以便调试
        result.printSchema();
        result.show(10, false);

        return result;
    }

    /**
     * 拼接处理逻辑
     * {
     * "fieldDerivationType":"传递的字段名字"
     *   ,"fieldDerivationName":"规则配置的合并后存储的字段名字存入"
     *   ,"delimiter":"连接符"
     *   ,"tableFields":[
     *     {"columnName":"进行拼接的字段名称"}
     *   ]
     *   ,"fieldDerivationPrefix":"前缀符"
     *   ,"fieldDerivationSuffix":"后缀符"
     * }
     * @return
     */
    private Dataset<Row> handleConcat(JSONObject parameter, Dataset<Row> dataset, LogUtils.Params logPath) {
        LogUtils.writeLog(logPath, "开始字段派生-拼接处理");
        LogUtils.writeLog(logPath, "开始任务时间: " + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));

        //规则配置的合并后存储的字段名字存入
        String fieldDerivationName = MapUtils.getString(parameter,"fieldDerivationName");
        //链接符
        String delimiter = MapUtils.getString(parameter,"delimiter","");
        //前缀符号
        String prefix = MapUtils.getString(parameter,"fieldDerivationPrefix","");
        //后缀符号
        String suffix = MapUtils.getString(parameter,"fieldDerivationSuffix","");
        //选择的字段
        List<Map<String, Object>> tableFields = (List<Map<String, Object>>) MapUtils.getObject(parameter, "tableFields");

        //检验
        if (CollectionUtils.isEmpty(tableFields)) {
            throw new IllegalArgumentException("进行计算的字段不能为空！");
        }
        if (StringUtils.isEmpty(fieldDerivationName)) {
            throw new IllegalArgumentException("需要存储的字段名称不能为空！");
        }

        // 构造字段列表，转换为字符串，并用 "null" 占位
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
            throw new IllegalArgumentException("未获取到任何有效字段用于拼接！");
        }

        // 用 delimiter 拼接字段值
        Column concatCol = functions.concat_ws(delimiter, columns.toArray(new Column[0]));

        // 添加前缀和后缀
        concatCol = functions.concat_ws("", functions.lit(prefix), concatCol, functions.lit(suffix));

        // 添加派生字段列
        Dataset<Row> rowDataset = dataset.withColumn(fieldDerivationName, concatCol);

        // 调试日志
        System.out.println("拼接后的字段结构：");
        rowDataset.printSchema();

        System.out.println("拼接后的前10条数据：");
        rowDataset.show(10, false);

        return rowDataset;
    }
}
