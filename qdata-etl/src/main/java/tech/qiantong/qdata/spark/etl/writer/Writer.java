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
 */

package tech.qiantong.qdata.spark.etl.writer;

import com.alibaba.fastjson2.JSONObject;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import tech.qiantong.qdata.spark.etl.utils.LogUtils;

/**
 * <P>
 * 用途:写数据
 * </p>
 *
 * @author: FXB
 * @create: 2025-04-25 09:34
 **/
public interface Writer {

    Boolean writer(JSONObject config,Dataset<Row> dataset, JSONObject writer, LogUtils.Params logParams);

    String code();
}
