package tech.qiantong.qdata.spark.etl.transition;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import tech.qiantong.qdata.common.enums.TaskComponentTypeEnum;
import tech.qiantong.qdata.spark.etl.utils.LogUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.alibaba.fastjson2.JSONWriter.Feature.PrettyFormat;
import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.lower;

/**
 * 数据去重
 */
public class DataDeduplicationTransition implements Transition {

    @Override
    public String code() {
        return TaskComponentTypeEnum.DATA_DEDUPLICATION.getCode();
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
        LogUtils.writeLog(logParams, "开始数据去重节点");
        LogUtils.writeLog(logParams, "开始任务时间: " + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
        LogUtils.writeLog(logParams, "任务参数：" + transition.toJSONString(PrettyFormat));
        JSONObject parameter = transition.getJSONObject("parameter");

        //选择的字段
        List<Map<String, Object>> tableFields = (List<Map<String, Object>>) MapUtils.getObject(parameter, "tableFields");

        //检验
        if (CollectionUtils.isEmpty(tableFields)) {
            throw new IllegalArgumentException("进行计算的字段不能为空！");
        }

        // 所有字段转小写列 + 添加为临时列
        for (Map<String, Object> field : tableFields) {
            String columnName = MapUtils.getString(field, "columnName");
            String ignoreCase = MapUtils.getString(field, "ignoreCase", "1");

            if ("2".equals(ignoreCase)) {
                dataset = dataset.withColumn("_tmp_" + columnName, lower(col(columnName)));
            } else {
                dataset = dataset.withColumn("_tmp_" + columnName, col(columnName));
            }
        }

        // 构造临时字段名数组
        List<String> tmpFields = tableFields.stream()
                .map(f -> "_tmp_" + MapUtils.getString(f, "columnName"))
                .collect(Collectors.toList());

        // 去重
        Dataset<Row> rowDataset = dataset.dropDuplicates(tmpFields.toArray(new String[0]));

        // 删除临时字段
        for (String tmp : tmpFields) {
            rowDataset = rowDataset.drop(tmp);
        }

        // 调试日志
        System.out.println("数据去重后的字段结构：");
        rowDataset.printSchema();

        System.out.println("数据去重后的前10条数据：");
        rowDataset.show(10, false);

        return rowDataset;
    }
}
