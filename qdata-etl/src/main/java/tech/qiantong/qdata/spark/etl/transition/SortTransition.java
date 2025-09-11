package tech.qiantong.qdata.spark.etl.transition;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.spark.sql.*;
import tech.qiantong.qdata.common.enums.TaskComponentTypeEnum;
import tech.qiantong.qdata.spark.etl.utils.LogUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.alibaba.fastjson2.JSONWriter.Feature.PrettyFormat;
import static org.apache.spark.sql.functions.col;

/**
 * 排序转换器（类似Kettle的排序记录）
 *
 * @author
 * @date 2025/06/20
 **/
public class SortTransition implements Transition {

    /**
     * {
     * "tableFields": [
     * {"columnName": "age", "order": "asc"},
     * {"columnName": "name", "order": "desc"}
     * ]
     * }
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
        LogUtils.writeLog(logParams, "开始排序节点");
        LogUtils.writeLog(logParams, "开始任务时间: " + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
        LogUtils.writeLog(logParams, "任务参数：" + transition.toJSONString(PrettyFormat));
        JSONObject parameter = transition.getJSONObject("parameter");

        // 1. 解析排序字段
        JSONArray sortFields = parameter.getJSONArray("tableFields");
        if (sortFields == null || sortFields.isEmpty()) {
            // 没有排序字段，直接返回原数据
            return dataset;
        }

        List<Column> orderColumns = new ArrayList<>();
        for (int i = 0; i < sortFields.size(); i++) {
            JSONObject sortField = sortFields.getJSONObject(i);
            String field = sortField.getString("columnName");
            String order = sortField.getString("order");
            boolean caseSensitive = sortField.containsKey("caseSensitive") ? sortField.getBoolean("caseSensitive") : false;

            Column sortCol;
            if (!caseSensitive) {
                sortCol = functions.lower(col(field));
            } else {
                sortCol = col(field);
            }

            if ("desc".equalsIgnoreCase(order)) {
                orderColumns.add(sortCol.desc());
            } else {
                orderColumns.add(sortCol.asc());
            }
        }

        // 2. 排序
        // 排序前
        dataset.show(false);
        Dataset<Row> sorted = dataset.sort(orderColumns.toArray(new Column[0]));
        // 排序后
        sorted.show(false);
        return sorted;
    }

    @Override
    public String code() {
        return TaskComponentTypeEnum.SORT_RECORD.getCode();
    }
}
