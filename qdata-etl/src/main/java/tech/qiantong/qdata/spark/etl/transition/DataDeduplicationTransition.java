package tech.qiantong.qdata.spark.etl.transition;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import tech.qiantong.qdata.common.enums.TaskComponentTypeEnum;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.spark.etl.utils.LogUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.alibaba.fastjson2.JSONWriter.Feature.PrettyFormat;
import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.lower;

/**
 * Data deduplication
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
        LogUtils.writeLog(logParams, MessageUtils.messageEn("etl.transition.dedup.start"));
        LogUtils.writeLog(logParams, MessageUtils.messageEn("etl.task.start.time", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS")));
        LogUtils.writeLog(logParams, MessageUtils.messageEn("etl.task.parameters", transition.toJSONString(PrettyFormat)));
        JSONObject parameter = transition.getJSONObject("parameter");

        //Selected fields
        List<Map<String, Object>> tableFields = (List<Map<String, Object>>) MapUtils.getObject(parameter, "tableFields");

        //Inspect
        if (CollectionUtils.isEmpty(tableFields)) {
            throw new IllegalArgumentException(MessageUtils.messageEn("etl.error.dedup.fields.empty"));
        }

        // Convert all fields to lowercase columns + add as temporary columns
        for (Map<String, Object> field : tableFields) {
            String columnName = MapUtils.getString(field, "columnName");
            String ignoreCase = MapUtils.getString(field, "ignoreCase", "1");

            if ("2".equals(ignoreCase)) {
                dataset = dataset.withColumn("_tmp_" + columnName, lower(col(columnName)));
            } else {
                dataset = dataset.withColumn("_tmp_" + columnName, col(columnName));
            }
        }

        // Construct an array of temporary field names
        List<String> tmpFields = tableFields.stream()
                .map(f -> "_tmp_" + MapUtils.getString(f, "columnName"))
                .collect(Collectors.toList());

        // Remove duplicates
        Dataset<Row> rowDataset = dataset.dropDuplicates(tmpFields.toArray(new String[0]));

        // Delete temporary fields
        for (String tmp : tmpFields) {
            rowDataset = rowDataset.drop(tmp);
        }

        // Debug log
        System.out.println("Field schema after deduplication: ");
        rowDataset.printSchema();

        System.out.println("First 10 rows after deduplication:");
        rowDataset.show(10, false);

        return rowDataset;
    }
}
