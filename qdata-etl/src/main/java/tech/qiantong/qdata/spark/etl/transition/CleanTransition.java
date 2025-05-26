package tech.qiantong.qdata.spark.etl.transition;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.types.StructType;
import tech.qiantong.qdata.spark.etl.utils.LogUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.alibaba.fastjson2.JSONWriter.Feature.PrettyFormat;
import static org.apache.spark.sql.functions.*;

/**
 * <P>
 * 用途:清洗-转换
 * </p>
 *
 * @author: FXB
 * @create: 2025-04-22 13:39
 **/
public class CleanTransition {

    public static Dataset<Row> transition(Dataset<Row> dataset, JSONObject transition, String logPath) {
        LogUtils.writeLog(logPath, "*********************************  Initialize task context  ***********************************");
        LogUtils.writeLog(logPath, "开始转换节点");
        LogUtils.writeLog(logPath, "开始任务时间: " + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
        LogUtils.writeLog(logPath, "任务参数：" + transition.toJSONString(PrettyFormat));

        JSONObject parameter = transition.getJSONObject("parameter");
        // 获取需要处理的列名
        List<Map<String, Object>> columnList = (List<Map<String, Object>>) parameter.get("cleanRuleList");

        // 对每个列应用数据处理
        for (Map<String, Object> columnConfig : columnList) {
            String columnName = (String) columnConfig.get("columns");
            List<Map<String, Object>> cleanRules = (List<Map<String, Object>>) columnConfig.get("cleanRules");

            // 应用数据处理规则
            for (Map<String, Object> rule : cleanRules) {
                JSONObject ruleConfig = null;
                if (rule.containsKey("data") && rule.get("data") != null) {
                    ruleConfig = JSONObject.parseObject(String.valueOf(rule.get("data")));
                }
                switch (Integer.valueOf(rule.get("ruleId").toString())) {
                    case 8:
                        dataset = convertCase(dataset, columnName, ruleConfig);
                        break;
                    case 9:
                        dataset = replaceNull(dataset, columnName, ruleConfig);
                        break;
                    case 10:
                        dataset = trimSpace(dataset, columnName);
                        break;
                    case 11:
                        dataset = applyDefaultValue(dataset, columnName, ruleConfig);
                        break;
                    case 12:
                        dataset = computeFieldValue(dataset, columnName, ruleConfig);
                        break;
                    case 13:
                        dataset = normalizeDictValue(dataset, columnName, ruleConfig);
                        break;
                    case 14:
                        dataset = applyRegexReplace(dataset, columnName, ruleConfig);
                        break;
                }
            }
        }
        return dataset;
    }

    /**
     * 通过正则表达式对字段值进行替换
     *
     * @param dataset
     * @param columnName
     * @param ruleConfig
     * @return
     */
    private static Dataset<Row> applyRegexReplace(Dataset<Row> dataset, String columnName, JSONObject ruleConfig) {
        JSONObject config = ruleConfig.getJSONObject("regexValidate");
        String pattern = config.getString("pattern");         // 正则表达式
        String replacement = config.getString("replacement"); // 替换内容
        String resultType = config.getString("resultType"); // 替换type：1-替换成功的，2-替换失败的

        String replaceExpr = String.format("regexp_replace(%s, '%s', '%s')", columnName, pattern, replacement);
        Column originalCol = functions.col(columnName);
        Column replacedCol = functions.expr(replaceExpr);

        if ("1".equals(resultType)) {
            // 替换成功的，保留替换成功的行
            return dataset.withColumn(columnName, replacedCol)
                    .filter(originalCol.notEqual(replacedCol));
        } else if ("2".equals(resultType)) {
            // 替换失败的，保留未替换行
            return dataset.withColumn(columnName, replacedCol)
                    .filter(originalCol.equalTo(replacedCol));
        } else {
            // 默认全部替换
            return dataset.withColumn(columnName, replacedCol);
        }
    }

    /**
     * 将字段值根据字典转换为标准值
     *
     * @param dataset
     * @param columnName
     * @param ruleConfig
     * @return
     */
    private static Dataset<Row> normalizeDictValue(Dataset<Row> dataset, String columnName, JSONObject ruleConfig) {
        JSONArray dictValueList = ruleConfig.getJSONArray("dictValueList");

        // 构建 when...otherwise 的表达式
        Column mappedColumn = null;
        for (int i = 0; i < dictValueList.size(); i++) {
            JSONObject dict = dictValueList.getJSONObject(i);
            String originalValue = dict.getString("originalValue");
            String dictValue = dict.getString("dictValue");

            Column condition = functions.col(columnName).equalTo(originalValue);
            Column result = functions.lit(dictValue);

            if (mappedColumn == null) {
                mappedColumn = functions.when(condition, result);
            } else {
                mappedColumn = mappedColumn.when(condition, result);
            }
        }

        // 所有都不匹配则返回原值
        mappedColumn = mappedColumn.otherwise(functions.col(columnName));

        // 替换列
        return dataset.withColumn(columnName, mappedColumn);
    }

    /**
     * 根据规则进行数值字段的计算
     *
     * @param dataset
     * @param columnName 计算后存储的字段
     * @param ruleConfig
     * @return
     */
    private static Dataset<Row> computeFieldValue(Dataset<Row> dataset, String columnName, JSONObject ruleConfig) {
        JSONArray tokens = ruleConfig.getJSONArray("tokens");
        StructType schema = dataset.schema();
        Set<String> columnNames = Arrays.stream(schema.fieldNames()).collect(Collectors.toSet());

        StringBuilder exprBuilder = new StringBuilder();

        for (int i = 0; i < tokens.size(); i++) {
            JSONObject token = tokens.getJSONObject(i);
            String type = token.getString("type");

            switch (type) {
                case "leftParen":
                    exprBuilder.append("(");
                    break;
                case "rightParen":
                    exprBuilder.append(")");
                    break;
                case "field":
                    String fieldName = token.getString("value");
                    String fieldType = token.getString("valueType"); // 每个字段自己的类型

                    if (columnNames.contains(fieldName)) {
                        if ("string".equalsIgnoreCase(fieldType)) {
                            exprBuilder.append("coalesce(cast(`").append(fieldName).append("` as string), '')");
                        } else {
                            exprBuilder.append("coalesce(`").append(fieldName).append("`, 0)");
                        }
                    } else {
                        exprBuilder.append("string".equalsIgnoreCase(fieldType) ? "''" : "0");
                    }
                    break;
                case "constant":
                    String constantValue = token.getString("value");
                    String constType = token.getString("valueType");
                    if ("string".equalsIgnoreCase(constType)) {
                        exprBuilder.append("'").append(constantValue.replace("'", "\\'")).append("'");
                    } else {
                        exprBuilder.append(constantValue);
                    }
                    break;
                case "operator":
                    exprBuilder.append(" ").append(token.getString("value")).append(" ");
                    break;
                default:
                    throw new IllegalArgumentException("不支持的 token 类型: " + type);
            }
        }

        return dataset.withColumn(columnName, functions.expr(exprBuilder.toString()));
    }

    /**
     * 为空字段填入默认值
     *
     * @param dataset
     * @param columnName
     * @param ruleConfig
     * @return
     */
    private static Dataset<Row> applyDefaultValue(Dataset<Row> dataset, String columnName, JSONObject ruleConfig) {
        JSONObject config = ruleConfig.getJSONObject("defaultValueFill");
        return dataset.withColumn(
                columnName,
                lit(config.getString("value"))
        );
    }

    /**
     * 字符串大小写转换
     *
     * @param dataset    输入数据集
     * @param columnName 需要转换的列名
     * @param ruleConfig 规则配置
     *                   {
     *                   "toggleCase": {
     *                   "type": "1"//1:大写转换，2:小写转换
     *                   }
     *                   }
     * @return 处理后的数据集
     */
    public static Dataset<Row> convertCase(Dataset<Row> dataset, String columnName, JSONObject ruleConfig) {
        JSONObject config = ruleConfig.getJSONObject("toggleCase");
        if (StringUtils.equals("1", config.getString("type"))) {
            return dataset.withColumn(columnName, upper(col(columnName)));
        } else {
            return dataset.withColumn(columnName, lower(col(columnName)));
        }
    }


    /**
     * 移除字符串首尾空格
     *
     * @param dataset    输入数据集
     * @param columnName 需要处理的列名
     * @return 处理后的数据集
     */
    public static Dataset<Row> trimSpace(Dataset<Row> dataset, String columnName) {
        return dataset.withColumn(columnName, trim(col(columnName)));
    }

    /**
     * 空值替换
     *
     * @param dataset    输入数据集
     * @param columnName 需要处理的列名
     * @param ruleConfig 规则配置
     *                   {
     *                   "nullReplace": {
     *                   "value": "1"//替换内容
     *                   }
     *                   }
     * @return 处理后的数据集
     */
    public static Dataset<Row> replaceNull(Dataset<Row> dataset, String columnName, JSONObject ruleConfig) {
        JSONObject config = ruleConfig.getJSONObject("nullReplace");
        return dataset.withColumn(columnName,
                coalesce(col(columnName), lit(config.getString("value"))));
    }

}
