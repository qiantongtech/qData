/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please contact the official team for authorization.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

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
