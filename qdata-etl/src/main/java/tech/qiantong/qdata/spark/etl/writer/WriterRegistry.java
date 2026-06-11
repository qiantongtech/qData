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

import tech.qiantong.qdata.common.enums.TaskComponentTypeEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * <P>
 * 用途:输出组件注册
 * </p>
 *
 * @author: FXB
 * @create: 2025-04-25 09:35
 **/
public class WriterRegistry {
    private final Map<String, Writer> readerMap = new HashMap<>();

    public WriterRegistry() {
        this.readerMap.put(TaskComponentTypeEnum.DB_WRITER.getCode(), new DBWriter());
    }
    public Writer getWriter(String code) {
        return this.readerMap.get(code);
    }
}
