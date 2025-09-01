package tech.qiantong.qdata.spark.etl.reader;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.*;
import tech.qiantong.qdata.common.enums.TaskComponentTypeEnum;
import tech.qiantong.qdata.spark.etl.utils.LogUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.alibaba.fastjson2.JSONWriter.Feature.PrettyFormat;

/**
 * <P>
 * 用途:csv输入
 * </p>
 *
 * @author: FXB
 * @create: 2025-04-21 13:34
 **/
@Slf4j
public class CsvReader implements Reader {

    @Override
    public Dataset<Row> read(SparkSession spark, JSONObject reader,List<String> readerColumns,LogUtils.Params logParams) {
        LogUtils.writeLog(logParams, "*********************************  Initialize task context  ***********************************");
        LogUtils.writeLog(logParams, "开始csv输入节点");
        LogUtils.writeLog(logParams, "开始任务时间: " + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
        LogUtils.writeLog(logParams, "任务参数：" + reader.toJSONString(PrettyFormat));
        //参数信息
        JSONObject parameter = reader.getJSONObject("parameter");
        //字段
        List<Object> column = parameter.getJSONArray("column");
        //csv 文件路径
        String path = parameter.getString("path");

        spark.conf().set("dfs.client.use.datanode.hostname", "true");
        Dataset<Row> dataset = spark.read()
                .format("csv")
                .option("header", "true") // 如果 CSV 文件有表头
                .option("inferSchema", "true") // 自动推断数据类型
                .option("multiLine", "true")
                .option("escape", "\"")
                .load(path);
        dataset = dataset.select(column.stream().map(c -> new Column(((JSONObject) c).getString("columnName"))).toArray(Column[]::new));
        readerColumns.addAll(column.stream().map(c -> ((JSONObject) c).getString("columnName")).collect(Collectors.toList()));
        LogUtils.writeLog(logParams, "输入数据量为：" + dataset.count());
        log.info("部分数据如下>>>>>>>>>>>>>>");
        dataset.na().fill("Unknown").show(10);
        LogUtils.writeLog(logParams, "部分数据：\n" + dataset.na().fill("Unknown").showString(10, 0, false));
        return dataset;
    }

    @Override
    public String code() {
        return TaskComponentTypeEnum.EXCEL_READER.getCode();
    }
}
