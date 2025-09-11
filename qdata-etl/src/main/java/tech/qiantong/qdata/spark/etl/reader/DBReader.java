package tech.qiantong.qdata.spark.etl.reader;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import tech.qiantong.qdata.common.enums.TaskComponentTypeEnum;
import tech.qiantong.qdata.spark.etl.utils.LogUtils;
import tech.qiantong.qdata.spark.etl.utils.db.DBUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.alibaba.fastjson2.JSONWriter.Feature.PrettyFormat;

/**
 * <P>
 * 用途:数据库输入
 * </p>
 *
 * @author: FXB
 * @create: 2025-04-21 13:33
 **/
@Slf4j
public class DBReader implements Reader {

    @Override
    public Dataset<Row> read(SparkSession spark, JSONObject reader, List<String> readerColumns, LogUtils.Params logParams) {
        LogUtils.writeLog(logParams, "*********************************  Initialize task context  ***********************************");
        LogUtils.writeLog(logParams, "开始数据库输入节点");
        LogUtils.writeLog(logParams, "开始任务时间: " + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
        LogUtils.writeLog(logParams, "任务参数：" + reader.toJSONString(PrettyFormat));
        //参数信息
        JSONObject parameter = reader.getJSONObject("parameter");
        //读取条件
        String where = parameter.getString("where");
        //读取方式 1:全量 2:id增量 3:时间范围增量 默认全量
        String readModeType = parameter.getString("readModeType");
        //字段
        List<Object> column = parameter.getJSONArray("column");
        //封装读取信息
        Map<String, String> readerOptions = DBUtils.getDbOptions(parameter);

        readerColumns.addAll(column.stream().map(c -> (String) c).collect(Collectors.toList()));

        //读取数据
        Dataset<Row> dataset = spark.read()
                .format("jdbc")
                .options(readerOptions)
                .load();
        String where2 = "";
        //判断是否是id增量
        if (StringUtils.equals("2", readModeType)) {
            JSONObject idIncrementConfig = parameter.getJSONObject("idIncrementConfig");
            String incrementColumn = idIncrementConfig.getString("incrementColumn");
            Integer incrementStart = idIncrementConfig.getInteger("incrementStart");
            where2 = incrementColumn + " >= " + incrementStart;
        }
        if (StringUtils.equals("3", readModeType)) {
            JSONObject dateIncrementConfig = parameter.getJSONObject("dateIncrementConfig");
            String logic = dateIncrementConfig.getString("logic");
            String dateFormat = dateIncrementConfig.getString("dateFormat");

            List<JSONObject> columnList = dateIncrementConfig.getJSONArray("column").stream().map(e -> {
                return (JSONObject) e;
            }).collect(Collectors.toList());
            for (int i = 0; i < columnList.size(); i++) {
                JSONObject jsonObject = columnList.get(i);
                ////类型  1:固定值 2:自动(当前时间) 3:SQL表达式
                String type = jsonObject.getString("type");
                //增量字段
                String incrementColumn = jsonObject.getString("incrementColumn");
                //时间 运算符 > 、=>、< 、<=
                String operator = jsonObject.getString("operator");
                //固定值：为 2023-01-01  SQL表达式：为sql函数
                String data = jsonObject.getString("data");
                if (StringUtils.equals("1", type)) {
                    data = DateUtil.format(new Date(), dateFormat);
                }
                where2 += incrementColumn + " " + operator + " " + data;

                if (columnList.size() > i) {
                    where2 += logic;
                }
            }
        }
        //添加条件
        if (StringUtils.isNotBlank(where)) {
            if (StringUtils.isNotBlank(where2)) {
                where += " AND ( " + where2 + " )";
            }
            dataset = dataset.where(where);
        }
        dataset = dataset.select(column.stream().map(c -> new Column((String) c)).toArray(Column[]::new));
        LogUtils.writeLog(logParams, "输入数据量为：" + dataset.count());
        log.info("部分数据如下>>>>>>>>>>>>>>");
        dataset.na().fill("Unknown").show(10);
        LogUtils.writeLog(logParams, "部分数据：\n" + dataset.na().fill("Unknown").showString(10, 0, false));
        return dataset;
    }

    @Override
    public String code() {
        return TaskComponentTypeEnum.DB_READER.getCode();
    }


}
