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
