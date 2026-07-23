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

package tech.qiantong.qdata.spark.etl.writer;

import com.alibaba.fastjson2.JSONObject;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import tech.qiantong.qdata.spark.etl.utils.LogUtils;

/**
 * <P>
 * Purpose: write data
 * </p>
 *
 * @author: FXB
 * @create: 2025-04-25 09:34
 **/
public interface Writer {

    Boolean writer(JSONObject config,Dataset<Row> dataset, JSONObject writer, LogUtils.Params logParams);

    String code();
}
