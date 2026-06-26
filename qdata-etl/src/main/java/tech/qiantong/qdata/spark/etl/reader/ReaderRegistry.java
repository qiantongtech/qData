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

import tech.qiantong.qdata.common.enums.TaskComponentTypeEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * <P>
 * 用途:输入组件注册
 * </p>
 *
 * @author: FXB
 * @create: 2025-04-21 13:38
 **/
public class ReaderRegistry {

    private final Map<String, Reader> readerMap = new HashMap<>();

    public ReaderRegistry() {
        this.readerMap.put(TaskComponentTypeEnum.DB_READER.getCode(), new DBReader());
        this.readerMap.put(TaskComponentTypeEnum.EXCEL_READER.getCode(), new ExcelReader());
        this.readerMap.put(TaskComponentTypeEnum.CSV_READER.getCode(), new CsvReader());
    }

    public Reader getReader(String code) {
        return this.readerMap.get(code);
    }
}
