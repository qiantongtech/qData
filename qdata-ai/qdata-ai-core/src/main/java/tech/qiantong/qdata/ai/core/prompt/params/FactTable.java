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

package tech.qiantong.qdata.ai.core.prompt.params;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * <P>
 * 用途: 事实表结构
 * </p>
 *
 * @author: FXB
 * @create: 2026-04-07 09:48
 **/
@Data
@Builder
public class FactTable {
    private String tableName;
    //数据库名
    private String databaseName;
    //模式名
    private String schemaName;
    private String alias;
    private String description;
    private List<Column> columns;
    private List<String> primaryKeys;
    private String timeColumn; // 时间维度字段

    @Data
    @Builder
    public static class Column {
        //字段名称
        private String name;
        //字段类型
        private String type;
        //字段注释
        private String description;
        private String aggregation; // SUM, COUNT, AVG等
//        private boolean isMeasure; // 是否度量值
    }
}
