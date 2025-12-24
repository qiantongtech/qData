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
 * For brand customization, please apply for brand customization authorization via official channels.
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
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
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
        //节点编码
        String nodeCode = reader.getString("nodeCode");

        readerColumns.addAll(column.stream().map(c -> (String) c).collect(Collectors.toList()));

        //读取数据
        Dataset<Row> dataset = spark.read()
                .format("jdbc")
                .options(readerOptions)
                .load();
        String where2 = "";
        //需要存储最后数据的字段 map中key为字段名称 value为缓存key
        Map<String, String> cacheColumnMap = new HashMap<>();
        Map<String, String> cacheDataMap = new HashMap<>();
        //判断是否是id增量
        if (StringUtils.equals("2", readModeType)) {
            JSONObject idIncrementConfig = parameter.getJSONObject("idIncrementConfig");
            String incrementColumn = idIncrementConfig.getString("incrementColumn");
            Integer incrementStart = idIncrementConfig.getInteger("incrementStart");
            String cacheKey = ETL_READER_ID_KEY + nodeCode + ":" + incrementColumn;
            //添加到cacheDataMap中
            cacheColumnMap.put(incrementColumn, cacheKey);
            if (RedisUtils.hasKey(cacheKey) && Integer.valueOf(RedisUtils.get(cacheKey)) > incrementStart) {
                incrementStart = Integer.valueOf(RedisUtils.get(cacheKey));
                incrementStart++;
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
                //类型  1:固定值 2:时间范围 3:SQL表达式
                String type = jsonObject.getString("type");
                //增量字段
                String incrementColumn = jsonObject.getString("incrementColumn");
                //时间 运算符 > 、=>、< 、<=
                String operator = jsonObject.getString("operator");
                //固定值：为 2023-01-01  SQL表达式：为sql函数
                String data = jsonObject.getString("data");
                //游标时间 只有类型为时间范围时该字段才会有值
                String cursorTime = jsonObject.getString("cursorTime");

                String cacheKey = ETL_READER_DATE_KEY + nodeCode + ":" + incrementColumn;

                if (StringUtils.equals("1", type)) {
                    where2 += incrementColumn + " " + operator + " '" + data + "'";
                } else if (StringUtils.equals("3", type)) {
                    where2 += incrementColumn + " " + operator + " " + data;
                } else {
                    String now = DateUtil.format(new Date(), dateFormat);
                    //判断缓存中是否存在数据，存在且大于页面填写的数据则使用缓存数据
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
        //添加条件
        if (StringUtils.isNotBlank(where)) {
            if (StringUtils.isNotBlank(where2)) {
                where += " AND ( " + where2 + " )";
            }
            dataset = dataset.where(where);
        } else if (StringUtils.isNotBlank(where2)) {
            dataset = dataset.where(where2);
        }
        dataset = dataset.select(column.stream().map(c -> new Column((String) c)).toArray(Column[]::new));
        LogUtils.writeLog(logParams, "输入数据量为：" + dataset.count());
        log.info("部分数据如下>>>>>>>>>>>>>>");
        dataset.na().fill("Unknown").show(10);
        LogUtils.writeLog(logParams, "部分数据：\n" + dataset.na().fill("Unknown").showString(10, 0, false));
        //判断是否需要存储最后的数据
        if (cacheColumnMap.size() > 0) {
            for (Map.Entry<String, String> entry : cacheColumnMap.entrySet()) {
                String cacheKey = entry.getValue();
                Dataset<Row> rowDataset = dataset.select(entry.getKey()).orderBy(desc(entry.getKey()));
                if (rowDataset.count() == 0) {
                    continue;
                }
                if (StringUtils.equals("2", readModeType)) {//id增量
                    String cacheValue = String.valueOf(rowDataset.first().get(0));
                    cacheDataMap.put(cacheKey, cacheValue);
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
