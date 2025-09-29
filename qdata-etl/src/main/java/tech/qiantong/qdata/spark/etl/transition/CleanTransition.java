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
import tech.qiantong.qdata.spark.etl.utils.LogUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
public class CleanTransition implements Transition {

    /**
     * 新
     * @param spark
     * @param dataset
     * @param transition
     * @param logParams
     * @return
     */
    @Override
    public  Dataset<Row> transition(SparkSession spark,Dataset<Row> dataset, JSONObject transition, LogUtils.Params logParams) {
        LogUtils.writeLog(logParams, "*********************************  Initialize task context  ***********************************");
        LogUtils.writeLog(logParams, "开始清洗节点");
        LogUtils.writeLog(logParams, "开始任务时间: " + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
        LogUtils.writeLog(logParams, "任务参数：" + transition.toJSONString(PrettyFormat));

        JSONObject parameter = transition.getJSONObject("parameter");
        // 获取需要处理的列名
        List<Map<String, Object>> tableFieldList = (List<Map<String, Object>>) parameter.get("tableFields");
        String where = parameter.getString("where");
        if(StringUtils.isNotEmpty(where)){
            dataset = safeFilter(dataset, where, logParams);
        }

        if (tableFieldList == null || tableFieldList.isEmpty()) {
            LogUtils.writeLog(logParams, "未配置任何规则，直接返回数据集。");
            return dataset;
        }

        // 对每个列应用数据处理
        for (Map<String, Object> rule : tableFieldList) {
            String ruleCode = MapUtils.getString(rule, "ruleCode");
            String ruleType = MapUtils.getString(rule, "ruleType");
            JSONObject ruleConfig = JSONObject.parseObject((String) rule.get("ruleConfig"));
//            JSONObject ruleConfig = (JSONObject)rule.get("ruleConfig");
            String whereClause = MapUtils.getString(rule, "whereClause");
            if(StringUtils.isNotEmpty(whereClause)){
                dataset = safeFilter(dataset, whereClause, logParams);
            }

            // 执行前检查字段是否存在
            if (!checkColumnsExist(dataset, ruleConfig)) {
                LogUtils.writeLog(logParams, String.format("跳过规则 %s（字段不存在）", ruleCode));
                continue;
            }

            switch (ruleType) {
                case "WITHIN_BOUNDARY": // 数值边界调整
                    dataset = applyNumericBoundary(dataset, ruleConfig);
                    break;
                case "REMOVE_EMPTY_COMBINATION": // 组合字段为空删除
                    dataset = applyDeleteIfAllNull(dataset, ruleConfig);
                case "ADD_PREFIX_SUFFIX": // 字段前/后缀统一
                    dataset = applyPrefixSuffix(dataset, ruleConfig);
                    break;
                case "MENU_CUSTOM": // 枚举值映射标准化
                    dataset = normalizeEnumMapping(dataset, ruleConfig);
                    break;
                case "KEEP_LATEST_OR_FIRST": // 按组合字段去重（保留最新或首条）
                    dataset = deduplicateByFieldsKeepFirst(dataset, ruleConfig);
                    break;
                default:
                    LogUtils.writeLog(logParams, "未知规则：" + ruleCode);
            }
        }
        return dataset;
    }

    @Override
    public String code() {
        return TaskComponentTypeEnum.SPARK_CLEAN.getCode();
    }

    private Dataset<Row> deduplicateByFieldsKeepFirst(Dataset<Row> ds, JSONObject cfg) {
        // 1) 读取配置
        List<String> allCols = Optional.ofNullable(cfg.getJSONArray("columns"))
                .map(a -> a.toJavaList(String.class))
                .orElse(Collections.emptyList());
        if (allCols.isEmpty()) {
            throw new IllegalArgumentException("规则029配置缺失：columns 不能为空");
        }

        JSONArray sv = cfg == null ? null : cfg.getJSONArray("stringValue");
        List<JSONObject> sortRules = sv == null ? Collections.emptyList() : sv.toJavaList(JSONObject.class);

        // 2) 无排序规则：直接 dropDuplicates
        if (sortRules.isEmpty()) {
            // 仅按分组键去重，保留任意一条（通常是首条），最简也最高效
            return ds.dropDuplicates(allCols.toArray(new String[0]));
        }

        // 3) 有排序规则：构建分组键 = columns - 排序字段（防止把排序键也分组进去）
        Set<String> sortCols = sortRules.stream()
                .map(o -> o.getString("columns"))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        List<String> groupCols = allCols.stream()
                .filter(c -> !sortCols.contains(c))
                .collect(Collectors.toList());
        if (groupCols.isEmpty()) {
            // 全被剔除了就用原 columns 兜底
            groupCols = allCols;
        }

        // 4) 构建窗口：按 sort 升序表示优先级；type: 0=desc(最新)，1=asc(最早)
        Column[] partCols = groupCols.stream().map(functions::col).toArray(Column[]::new);
        WindowSpec spec = Window.partitionBy(partCols);

        sortRules.sort(Comparator.comparingInt(o -> o.getIntValue("sort"))); // 安全取整，避免NPE
        List<Column> orders = new ArrayList<>();
        for (JSONObject r : sortRules) {
            String c = r.getString("columns");
            int t = r.getIntValue("type"); // "0"/"1" 也能解析
            orders.add(t == 0 ? functions.col(c).desc() : functions.col(c).asc());
        }
        spec = spec.orderBy(orders.toArray(new Column[0]));

        // 5) 取第一条
        Dataset<Row> withRN = ds.withColumn("__rn", functions.row_number().over(spec));
        return withRN.filter(functions.col("__rn").equalTo(1)).drop("__rn");
    }

    private Dataset<Row> normalizeEnumMapping(Dataset<Row> dataset, JSONObject cfg) {
        String col = cfg.getJSONArray("columns").getString(0);
        List<JSONObject> list = cfg.getList("stringValue", JSONObject.class);
//        String handleType = cfg.getString("handleType"); // "1-加前缀" / "2-加后缀"

        if (StringUtils.isBlank(col) || list == null || list.size() < 1) {
            throw new IllegalArgumentException("规则024配置缺失：columns/stringValue 不能为空");
        }
        // 构建 when...otherwise 的表达式
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

        // 所有都不匹配则返回原值
        mappedColumn = mappedColumn.otherwise(functions.col(col));

        // 替换列
        return dataset.withColumn(col, mappedColumn);
    }

    private Dataset<Row> applyPrefixSuffix(Dataset<Row> dataset, JSONObject cfg) {
        String colName = cfg.getJSONArray("columns").getString(0);
        String stringValue = cfg.getString("stringValue");
        String handleType = cfg.getString("handleType"); // "1" 前缀；"2" 后缀；

        if (StringUtils.isBlank(colName) || StringUtils.isBlank(stringValue) || StringUtils.isBlank(handleType)) {
            throw new IllegalArgumentException("规则107配置缺失：columns/stringValue/handleType 不能为空");
        }

        if (!("1".equals(handleType) || "2".equals(handleType) || "3".equals(handleType) || "4".equals(handleType))) {
            throw new IllegalArgumentException("规则107 handleType 非法：" + handleType);
        }

        Column c = col(colName).cast("string");
        String sv = stringValue;

        //以下是不看大小写，如果一样就不拼接
//        String svLower = sv.toLowerCase();
//        if (StringUtils.equals("1", handleType)) {
//            // 前缀
//            dataset = dataset.withColumn(
//                    colName,
//                    when(c.isNull(), lit(null))
//                            .when(lower(c).like(svLower + "%"), c) // 用 like 判断前缀
//                            .otherwise(concat(lit(sv), c))
//            );
//        } else {
//            // 后缀
//            dataset = dataset.withColumn(
//                    colName,
//                    when(c.isNull(), lit(null))
//                            .when(lower(c).like("%" + svLower), c) // 用 like 判断后缀
//                            .otherwise(concat(c, lit(sv)))
//            );
//        }

        //以下是检验发小写，如果前缀大写，库里小写，依旧拼接
        switch (handleType) {
            case "1": // 加前缀
                dataset = dataset.withColumn(
                        colName,
                        when(c.isNull(), lit(null))
                                .when(c.startsWith(sv), c) // 区分大小写判断已有前缀
                                .otherwise(concat(lit(sv), c))
                );
                break;

            case "2": // 加后缀
                dataset = dataset.withColumn(
                        colName,
                        when(c.isNull(), lit(null))
                                .when(c.endsWith(sv), c) // 区分大小写判断已有后缀
                                .otherwise(concat(c, lit(sv)))
                );
                break;

            case "3": // 去除匹配上的前缀
                dataset = dataset.withColumn(
                        colName,
                        when(c.isNull(), lit(null))
                                .when(c.startsWith(sv), overlay(c, lit(""), lit(1), lit(sv.length()))) // 删掉开头 sv.length() 个字符
                                .otherwise(c)
                );
                break;

            case "4": // 去除匹配上的后缀
                dataset = dataset.withColumn(
                        colName,
                        when(c.isNull(), lit(null))
                                .when(c.endsWith(sv),
                                        overlay(
                                                c,
                                                lit(""),
                                                length(c).minus(lit(sv.length())).plus(lit(1)), // 起始位置：len - svLen + 1
                                                lit(sv.length())                               // 删除长度：svLen
                                        ))// 从头截取到后缀前
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

        // 获取所有列名（转小写方便比较）
        Set<String> columnSet = Arrays.stream(dataset.columns())
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        // 简单提取 where 中出现的字段名（按空格、符号拆分）
        String[] tokens = where.replaceAll("[()><=!,]", " ")
                .trim()
                .split("\\s+");
        for (String token : tokens) {
            if (token.matches("^[a-zA-Z_][a-zA-Z0-9_]*$") && !columnSet.contains(token.toLowerCase())) {
                String msg = "过滤条件字段不存在: " + token;
                LogUtils.writeLog(logParams, msg);
                throw new IllegalArgumentException(msg);
            }
        }

        try {
            dataset.selectExpr("*").filter(where).limit(1).count();
        } catch (Exception e) {
            String msg = "过滤条件解析失败: " + where + "，错误信息：" + e.getMessage();
            LogUtils.writeLog(logParams, msg);
            throw new IllegalArgumentException(msg, e);
        }

        // 通过校验后执行
        return dataset.filter(where);
    }


    /**
     * 检查字段是否存在
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
     * 数值边界调整
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
     * 组合字段为空删除
     */
    private Dataset<Row> applyDeleteIfAllNull(Dataset<Row> dataset, JSONObject cfg) {
        List<String> cols = cfg.getJSONArray("columns").toJavaList(String.class);
        if (cols == null || cols.isEmpty()) return dataset;

        // 列存在性校验
        Set<String> exists = new HashSet<>(Arrays.asList(dataset.columns()));
        List<String> missing = cols.stream().filter(c -> !exists.contains(c)).collect(Collectors.toList());
        if (!missing.isEmpty()) {
            throw new IllegalArgumentException("组合判空配置包含不存在的列：" + missing);
        }

        StructType schema = dataset.schema();
        Column allNullCond = functions.lit(true);

        for (String c : cols) {
            DataType t = schema.apply(c).dataType();

            // NULL 判定
            Column isNull = functions.col(c).isNull();

            // 空白串判定（先统一转 string 再 trim）
            Column isBlank = functions.trim(functions.col(c).cast("string")).equalTo("");

            // 数值 NaN 判定
            Column isEmpty = isNull.or(isBlank);
            if (t.sameType(DataTypes.FloatType) || t.sameType(DataTypes.DoubleType)) {
                isEmpty = isEmpty.or(functions.isnan(functions.col(c)));
            }

            allNullCond = allNullCond.and(isEmpty);
        }

        return dataset.filter(functions.not(allNullCond));
    }

    public static Dataset<Row> transitionOld(Dataset<Row> dataset, JSONObject transition, LogUtils.Params logParams) {
        LogUtils.writeLog(logParams, "*********************************  Initialize task context  ***********************************");
        LogUtils.writeLog(logParams, "版本重构，历史版本不再做支撑，可查看最新逻辑重新配置");
        return dataset;
    }

}
