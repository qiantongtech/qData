package tech.qiantong.qdata.spark.etl.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.spark.etl.reader.KafkaReader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <P>
 * 用途:
 * </p>
 *
 * @author: FXB
 * @create: 2024-04-16 15:41
 **/
public class EtlUtils {

    private static String NULL_STR = "null";

    /**
     * 处理json
     *
     * @param result json数据
     */
    public static List<Map<String, Object>> processSourceData(JSON result, List<KafkaReader.KafkaColumn> filIds) {
        JSON sourceDataJson = JSONUtil.parse(result.toString());
        //分割映射key
        for (KafkaReader.KafkaColumn filId : filIds) {
            splitRuleDataKey(filId);
        }
        //映射数据
        return transformData(filIds, sourceDataJson);
    }

    /**
     * 分割映射key
     *
     * @param filIds
     */
    public static void splitRuleDataKey(KafkaReader.KafkaColumn filIds) {
        String resultKey = StringUtils.trim(filIds.getKey());
        filIds.setSplitKeyList(new ArrayList<>(Arrays.asList(resultKey.split(Constants.ARRAY_EXPRESSION))));
        filIds.setSplitKeyListSize(filIds.getSplitKeyList().size());
    }

    /**
     * 映射数据
     *
     * @param rules          字段规则
     * @param sourceDataJson 数据
     * @return
     */
    public static List<Map<String, Object>> transformData(List<KafkaReader.KafkaColumn> rules, JSON sourceDataJson) {
//        List<KafkaReader.KafkaColumn> sortedRules =
//                rules.stream().sorted(
//                        Comparator.comparing(KafkaColumn::getSplitKeyListSize))
//                        .collect(Collectors.toList());
        //根据分割次数(层级)排序
        rules.sort(Comparator.comparing(KafkaReader.KafkaColumn::getSplitKeyListSize));
        //根据分割次数(层级)排序
        List<KafkaReader.KafkaColumn> sortedRules = rules;
        //根据映射规则解析数据
        return analyticalData(sourceDataJson, sortedRules);
    }

    public static List<Map<String, Object>> analyticalData(JSON sourceDataJson, List<KafkaReader.KafkaColumn> processRules) {
        //根据层级分组
        Map<Integer, List<KafkaReader.KafkaColumn>> levelGroup =
                processRules.stream()
                        .collect(Collectors.groupingBy(KafkaReader.KafkaColumn::getSplitKeyListSize));
        //根据层级升序排列
        LinkedHashMap<Integer, List<KafkaReader.KafkaColumn>> levelGroupSortedMappings =
                levelGroup.entrySet().stream()
                        .sorted(Map.Entry.comparingByKey())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        List<Map<String, Object>> dataSets = new ArrayList<>();
        //读取数据
        getData(levelGroupSortedMappings, sourceDataJson, new HashMap<>(processRules.size()), dataSets);
        return dataSets;
    }

    /**
     * 读取数据
     *
     * @param levelGroupSortedRules
     * @param sourceData
     * @param data
     * @param dataSets
     */
    public static void getData(Map<Integer, List<KafkaReader.KafkaColumn>> levelGroupSortedRules,
                               Object sourceData, Map<String, Object> data,
                               List<Map<String, Object>> dataSets) {
        //是否已读完
        if (levelGroupSortedRules.isEmpty()) {
            dataSets.add(data);
            return;
        }
        //按key值从少到多 从上层到下层 循环读取
        Integer levelKey = levelGroupSortedRules.keySet().stream().findFirst().get();
        List<KafkaReader.KafkaColumn> rules = levelGroupSortedRules.get(levelKey);

        // 循环当前级别下
        JSON json = JSONUtil.parse(sourceData);

        //判断是否触底
        if (rules.get(0).getSplitKeyList().size() == 1) {

            //已触底 获取当前级别下映射值 并放入map
            rules.forEach(i -> data.put(i.getName(), getValueByRulesNotJsonNull(json, i)));
            //干掉当前读取完的级别组
            levelGroupSortedRules.remove(levelKey);

            getData(levelGroupSortedRules, sourceData, data, dataSets);
        } else {
            //未触底
            //获取当前key
            String expression = rules.get(0).getSplitKeyList().get(0);
            //根据当前key获取jsonArray
            JSONArray jsonArray = JSONUtil.parseArray(json.getByPath(expression));
            //干掉已读的key
            levelGroupSortedRules.forEach((k, v) ->
                    v.forEach(i -> i.getSplitKeyList().remove(0)));
            //循环获取到的jsonArray
            jsonArray.forEach(i -> {
                //递归 深度拷贝 分发调用
                getData(deepClone(levelGroupSortedRules), i, new HashMap<>(data), dataSets);
            });
        }
    }

    public static Object getValueByRulesNotJsonNull(JSON json, KafkaReader.KafkaColumn rules) {
        Object value = getValueByRules(json, rules);
        return ObjectUtil.isEmpty(value) || NULL_STR.equals(value.toString()) ? null : value;
    }

    public static Object getValueByRules(JSON json, KafkaReader.KafkaColumn rule) {
        Object value;
        String dataKey = rule.getSplitKeyList().get(0);
        if (dataKey.contains(Constants.SUM_EXPRESSION)
                || dataKey.contains(Constants.MAX_EXPRESSION)
                || dataKey.contains(Constants.MIN_EXPRESSION)
                || dataKey.contains(Constants.SIZE_EXPRESSION)) {
            return getNumberValue(json, dataKey);
        }

        if (dataKey.contains(Constants.CASE_EXPRESSION)) {
            Object caseValue = getCaseValue(json, dataKey);
            return caseValue;
        }
        value = json.getByPath(dataKey);
        boolean hasNonValue = ObjectUtil.isEmpty(value) || NULL_STR.equals(value.toString());
        return value;
    }

    public static Object getCaseValue(JSON json, String key) {
        int index = key.indexOf(Constants.CASE_EXPRESSION);
        Object resultValue = json.getByPath(key.substring(0, index));
        if (ObjectUtil.isEmpty(resultValue)) {
            return resultValue;
        }
        String caseExpressionSting = key.substring(index + Constants.CASE_EXPRESSION.length());
        String value = "";
        for (String i : caseExpressionSting.split(",")) {
            List<String> caseExpression = Arrays.asList(i.split(":"));
            if (resultValue.toString().equals(caseExpression.get(0))) {
                value = (caseExpression.get(1));
            }
        }
        return value;
    }


    public static Object getNumberValue(JSON json, String key) {
        String numberExpression = "";
        if (key.contains(Constants.SUM_EXPRESSION)) {
            numberExpression = Constants.SUM_EXPRESSION;
        }
        if (key.contains(Constants.MAX_EXPRESSION)) {
            numberExpression = Constants.MAX_EXPRESSION;
        }
        if (key.contains(Constants.MIN_EXPRESSION)) {
            numberExpression = Constants.MIN_EXPRESSION;
        }
        if (key.contains(Constants.SIZE_EXPRESSION)) {
            numberExpression = Constants.SIZE_EXPRESSION;
        }
        List<String> keys = Arrays.asList(key.split("\\" + numberExpression));
        if (keys.size() > 2) {
            throw new RuntimeException("表达式仅支持单层循环计算");
        }
        Object jsonArrayObject = json.getByPath(keys.get(0));
        JSONArray jsonArray = JSONUtil.parseArray(jsonArrayObject);
        if (numberExpression.equals(Constants.SIZE_EXPRESSION)) {
            return jsonArray.size();
        }
        BigDecimal value = BigDecimal.ZERO;
        for (Object i : jsonArray) {
            Object number = JSONUtil.parse(i).getByPath(keys.get(1));
            BigDecimal bigDecimal = ObjectUtil.isEmpty(number) ? BigDecimal.ZERO
                    : new BigDecimal(number.toString());
            if (numberExpression.equals(Constants.SUM_EXPRESSION)) {
                value = value.add(bigDecimal);
            }
            if (numberExpression.equals(Constants.MAX_EXPRESSION)) {
                value = value.compareTo(bigDecimal) > 0 ? value : bigDecimal;
            }
            if (numberExpression.equals(Constants.MIN_EXPRESSION)) {
                value = value.compareTo(bigDecimal) < 0 ? value : bigDecimal;
            }
        }
        return value;
    }


    /**
     * 深拷贝Map
     */
    @SuppressWarnings("unchecked")
    public static <T> Map<Integer, List<KafkaReader.KafkaColumn>> deepClone(Map<Integer, List<KafkaReader.KafkaColumn>> obj) {
        T clonedObj = null;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            oos.writeObject(obj);
            oos.close();

            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(inputStream);
            clonedObj = (T) ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (Map<Integer, List<KafkaReader.KafkaColumn>>) clonedObj;
    }


}
