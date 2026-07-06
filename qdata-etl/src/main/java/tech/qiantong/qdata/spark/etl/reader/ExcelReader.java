/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.spark.etl.reader;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import tech.qiantong.qdata.common.enums.TaskComponentTypeEnum;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.spark.etl.utils.LogUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.alibaba.fastjson2.JSONWriter.Feature.PrettyFormat;

/**
 * <P>
 * 用途:Excel输入
 * </p>
 *
 * @author: FXB
 * @create: 2025-04-21 13:34
 **/
@Slf4j
public class ExcelReader implements Reader {
    @Override
    public Dataset<Row> read(SparkSession spark, JSONObject reader, List<String> readerColumns, LogUtils.Params logParams) {
        LogUtils.writeLog(logParams, "*********************************  Initialize task context  ***********************************");
        LogUtils.writeLog(logParams, MessageUtils.messageEn("etl.reader.excel.start"));
        LogUtils.writeLog(logParams, MessageUtils.messageEn("etl.task.start.time", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS")));
        LogUtils.writeLog(logParams, MessageUtils.messageEn("etl.task.parameters", reader.toJSONString(PrettyFormat)));
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
        LogUtils.writeLog(logParams, MessageUtils.messageEn("etl.input.data.count", dataset.count()));
        log.info(MessageUtils.message("log.etl.sample.data"));
        dataset.na().fill("Unknown").show(10);
        LogUtils.writeLog(logParams, MessageUtils.messageEn("etl.sample.data", dataset.na().fill("Unknown").showString(10, 0, false)));
        return dataset;
    }

    @Override
    public String code() {
        return TaskComponentTypeEnum.EXCEL_READER.getCode();
    }

    private List<Row> readData(Sheet sheet, Integer startData, JSONArray column) {
        List<Row> rows = new ArrayList<>();
        //读取数据
        for (int i = startData - 1; i <= sheet.getLastRowNum(); i++) {
            org.apache.poi.ss.usermodel.Row row = sheet.getRow(i);
            List<Object> rowData = new ArrayList<>();

            for (int c = 0; c < column.size(); c++) {
                Cell cell = row.getCell(c);
                JSONObject jsonObject = (JSONObject) column.get(c);
                //字段类型
                String type = jsonObject.getString("type");
                //格式化
                String format = jsonObject.getString("format");
                if (cell == null) {
                    rowData.add(null);
                    continue;
                }
                switch (type) {
                    case "long":
                        rowData.add(Long.parseLong(cell.toString()));
                        break;
                    case "string":
                        rowData.add(cell.toString());
                        break;
                    case "double":
                        rowData.add(Double.parseDouble(cell.toString()));
                        break;
                    case "date":
                        rowData.add(DateUtil.parse(cell.toString(), format));
                        break;
                    case "bool":
                        rowData.add(cell.getBooleanCellValue());
                        break;
                    case "bytes":
                        rowData.add(cell.getStringCellValue().getBytes());
                        break;
                }

            }
            rows.add(RowFactory.create(rowData.toArray())); // 转换为Spark的Row类型
        }
        return rows;
    }

    /**
     * 生成sparksql所需字段
     *
     * @param column
     * @return
     */
    private List<StructField> createStructType(JSONArray column) {
        log.info("column:{}", column);
        List<StructField> fields = new ArrayList<>(); // 定义字段结构，例如：fields.add(DataTypes.createStructField("column1", DataTypes.StringType, false));
        for (Object obj : column) {
            JSONObject jsonObject = (JSONObject) obj;
            //字段类型
            String type = jsonObject.getString("type");
            //字段名称
            String value = jsonObject.getString("value");
            if (StringUtils.isBlank(value)) {
                value = jsonObject.getString("columnName");
            }

            DataType dataType = null;
            switch (type) {
                case "long":
                    dataType = DataTypes.LongType;
                    break;
                case "string":
                    dataType = DataTypes.StringType;
                    break;
                case "date":
                    dataType = DataTypes.DateType;
                    break;
                case "bool":
                    dataType = DataTypes.BooleanType;
                    break;
                case "bytes":
                    dataType = DataTypes.ByteType;
                    break;
                case "double":
                    dataType = DataTypes.DoubleType;
                    break;
            }
            // 定义字段结构，例如：
            fields.add(DataTypes.createStructField(value, dataType, true));
        }
        return fields;
    }
}
