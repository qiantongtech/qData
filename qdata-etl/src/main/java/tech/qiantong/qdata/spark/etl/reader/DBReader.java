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
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import tech.qiantong.qdata.common.enums.TaskComponentTypeEnum;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.spark.etl.utils.LogUtils;
import tech.qiantong.qdata.spark.etl.utils.RedisUtils;
import tech.qiantong.qdata.spark.etl.utils.db.DBUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.alibaba.fastjson2.JSONWriter.Feature.PrettyFormat;
import static org.apache.spark.sql.functions.desc;

/**
 * <P>
 * Purpose: database input
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
        LogUtils.writeLog(logParams, MessageUtils.messageEn("etl.reader.db.start"));
        LogUtils.writeLog(logParams, MessageUtils.messageEn("etl.task.start.time", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS")));
        LogUtils.writeLog(logParams, MessageUtils.messageEn("etl.task.parameters", reader.toJSONString(PrettyFormat)));
        //Parameter information
        JSONObject parameter = reader.getJSONObject("parameter");
        //Read conditions
        String where = parameter.getString("where");
        //Reading method 1: full amount 2: id increment 3: time range increment Default full amount
        String readModeType = parameter.getString("readModeType");
        //Field
        List<Object> column = parameter.getJSONArray("column");
        //Encapsulate read information
        Map<String, String> readerOptions = DBUtils.getDbOptions(parameter);
        //Node code
        String nodeCode = reader.getString("nodeCode");

        readerColumns.addAll(column.stream().map(c -> (String) c).collect(Collectors.toList()));

        //Read data
        Dataset<Row> dataset = spark.read()
                .format("jdbc")
                .options(readerOptions)
                .load();
        String where2 = "";
        //The field that needs to store the last data. The key in the map is the field name and the value is the cache key.
        Map<String, String> cacheColumnMap = new HashMap<>();
        Map<String, String> cacheDataMap = new HashMap<>();
        //Determine whether it is an id increment
        if (StringUtils.equals("2", readModeType)) {
            JSONObject idIncrementConfig = parameter.getJSONObject("idIncrementConfig");
            String incrementColumn = idIncrementConfig.getString("incrementColumn");
            Integer incrementStart = idIncrementConfig.getInteger("incrementStart");
            String cacheKey = ETL_READER_ID_KEY + nodeCode + ":" + incrementColumn;
            //Add to cacheDataMap
            cacheColumnMap.put(incrementColumn, cacheKey);
            if (RedisUtils.hasKey(cacheKey) && Integer.valueOf(RedisUtils.get(cacheKey)) > incrementStart) {
                incrementStart = Integer.valueOf(RedisUtils.get(cacheKey));
            }

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
                //Type 1: Fixed value 2: Time range 3: SQL expression
                String type = jsonObject.getString("type");
                //Increment field
                String incrementColumn = jsonObject.getString("incrementColumn");
                //Time operators >, =>, <, <=
                String operator = jsonObject.getString("operator");
                //Fixed value: 2023-01-01 SQL expression: sql function
                String data = jsonObject.getString("data");
                //Cursor time This field will only have a value if the type is time range
                String cursorTime = jsonObject.getString("cursorTime");

                String cacheKey = ETL_READER_DATE_KEY + nodeCode + ":" + incrementColumn;

                if (StringUtils.equals("1", type)) {
                    where2 += incrementColumn + " " + operator + " '" + data + "'";
                } else if (StringUtils.equals("3", type)) {
                    where2 += incrementColumn + " " + operator + " " + data;
                } else {
                    String now = DateUtil.format(new Date(), dateFormat);
                    //Determine whether data exists in the cache. If the data exists and is larger than the data filled in the page, the cached data will be used.
                    if (RedisUtils.hasKey(cacheKey) && DateUtil.compare(DateUtil.parse(RedisUtils.get(cacheKey)), DateUtil.parse(cursorTime)) > 0) {
                        cursorTime = RedisUtils.get(cacheKey);
                    }
                    where2 += incrementColumn + " > '" + cursorTime + "' and " + incrementColumn + " <= '" + now + "'";
                    cacheDataMap.put(cacheKey, now);
                }

                if (columnList.size() > i + 1) {
                    where2 += " " + logic + " ";
                }
            }
        }
        //Add conditions
        if (StringUtils.isNotBlank(where)) {
            if (StringUtils.isNotBlank(where2)) {
                where += " AND ( " + where2 + " )";
            }
            dataset = dataset.where(where);
        } else if (StringUtils.isNotBlank(where2)) {
            dataset = dataset.where(where2);
        }
        dataset = dataset.select(column.stream().map(c -> new Column((String) c)).toArray(Column[]::new));
        LogUtils.writeLog(logParams, MessageUtils.messageEn("etl.input.data.count", dataset.count()));
        log.info(MessageUtils.message("log.etl.sample.data"));
        dataset.na().fill("Unknown").show(10);
        LogUtils.writeLog(logParams, MessageUtils.messageEn("etl.sample.data", dataset.na().fill("Unknown").showString(10, 0, false)));
        //Determine whether the last data needs to be stored
        if (cacheColumnMap.size() > 0) {
            for (Map.Entry<String, String> entry : cacheColumnMap.entrySet()) {
                String cacheKey = entry.getValue();
                Dataset<Row> rowDataset = dataset.select(entry.getKey()).orderBy(desc(entry.getKey()));
                if (rowDataset.count() == 0) {
                    continue;
                }
                if (StringUtils.equals("2", readModeType)) {//id increment
                    String cacheValue = String.valueOf(rowDataset.first().get(0));
                    cacheDataMap.put(cacheKey, String.valueOf(Integer.parseInt(cacheValue) + 1));
                }
            }
        }
        if (cacheDataMap.size() > 0) {
            reader.put("cacheDataMap", cacheDataMap);
        }
        return dataset;
    }

    @Override
    public String code() {
        return TaskComponentTypeEnum.DB_READER.getCode();
    }


}
