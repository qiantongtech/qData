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

package tech.qiantong.qdata.ai.core.prompt;

import org.springframework.stereotype.Component;
import tech.qiantong.qdata.ai.core.prompt.params.DimensionTable;
import tech.qiantong.qdata.ai.core.prompt.params.FactTable;

import java.util.List;

/**
 * <P>
 * 用途:事实表及维度表匹配提示词构建
 * </p>
 *
 * @author: FXB
 * @create: 2026-04-09 10:55
 **/
@Component
public class MatchPromptBuilder {

    private static final String SYSTEM_PROMPT = """
            你是专业数据仓库建模工程师。
            我将以 JSON 格式提供 1 张事实表和多张维度表,每个字段包含:字段名称、字段类型、字段注释、是否主键。
                        
            请你:
            1. 根据主键字段和字段名称语义,自动匹配事实表外键与维度表主键的关联关系。
            2. 只输出标准 JSON,不输出任何多余文字、解释、说明。
            3. 不编造字段,不虚构关联。
                        
            输入 JSON 结构:
            {
              "fact_table": {
                "tableName": "事实表名",
                "alias": "别名",
                "description": "表注释/标识",
                "columns": [
                  {
                    "name": "字段名",
                    "type": "字段类型",
                    "description": "注释"
                  }
                ],
                "primaryKeys":["主键"]
              },
              "dimension_tables": [
                {
                "tableName": "事实表名",
                "alias": "别名",
                "description": "表注释/标识",
                "columns": [
                  {
                    "name": "字段名",
                    "type": "字段类型",
                    "description": "注释"
                  }
                ],
                "primaryKeys":["主键"]
                }
              ]
            }
                        
            输出 JSON 结构:
            {
              "success":"是否匹配成功;false:否 false:是",
              "associations": [
                {
                  "dimensionTable": "维度表名",
                  "factColumnName": "事实表外键字段名",
                  "dimensionColumnName": "维度表主键字段名",
                  "matchReason": "匹配依据"
                }
              ]
            }
                        
            现在接收我的输入 JSON 并只返回结果 JSON。
            """;

    public String buildPrompt(FactTable factTable, List<DimensionTable> dimensions) {
        StringBuilder prompt = new StringBuilder();

        // 构建事实表JSON
        StringBuilder factTableJson = new StringBuilder();
        factTableJson.append("{\n");
        factTableJson.append(String.format("  \"tableName\": \"%s\",\n", escapeJson(factTable.getTableName())));
        factTableJson.append(String.format("  \"alias\": \"%s\",\n", escapeJson(factTable.getAlias())));
        factTableJson.append(String.format("  \"description\": \"%s\",\n", escapeJson(factTable.getDescription())));
        factTableJson.append("  \"columns\": [\n");

        for (int i = 0; i < factTable.getColumns().size(); i++) {
            FactTable.Column col = factTable.getColumns().get(i);
            factTableJson.append("    {\n");
            factTableJson.append(String.format("      \"name\": \"%s\",\n", escapeJson(col.getName())));
            factTableJson.append(String.format("      \"type\": \"%s\",\n", escapeJson(col.getType())));
            factTableJson.append(String.format("      \"description\": \"%s\"\n", escapeJson(col.getDescription())));
            factTableJson.append("    }");
            if (i < factTable.getColumns().size() - 1) {
                factTableJson.append(",");
            }
            factTableJson.append("\n");
        }

        factTableJson.append("  ],\n");

        // 添加主键信息
        factTableJson.append("  \"primaryKeys\": [");
        if (factTable.getPrimaryKeys() != null && !factTable.getPrimaryKeys().isEmpty()) {
            for (int i = 0; i < factTable.getPrimaryKeys().size(); i++) {
                factTableJson.append(String.format("\"%s\"", escapeJson(factTable.getPrimaryKeys().get(i))));
                if (i < factTable.getPrimaryKeys().size() - 1) {
                    factTableJson.append(", ");
                }
            }
        }
        factTableJson.append("]\n");
        factTableJson.append("}");

        // 构建维度表JSON数组
        StringBuilder dimensionTablesJson = new StringBuilder();
        dimensionTablesJson.append("[\n");

        for (int d = 0; d < dimensions.size(); d++) {
            DimensionTable dim = dimensions.get(d);
            dimensionTablesJson.append("  {\n");
            dimensionTablesJson.append(String.format("    \"tableName\": \"%s\",\n", escapeJson(dim.getTableName())));
            dimensionTablesJson.append(String.format("    \"alias\": \"%s\",\n", escapeJson(dim.getAlias())));
            dimensionTablesJson.append(String.format("    \"description\": \"%s\",\n", escapeJson(dim.getDescription())));
            dimensionTablesJson.append("    \"columns\": [\n");

            for (int i = 0; i < dim.getColumns().size(); i++) {
                DimensionTable.Column col = dim.getColumns().get(i);
                dimensionTablesJson.append("      {\n");
                dimensionTablesJson.append(String.format("        \"name\": \"%s\",\n", escapeJson(col.getName())));
                dimensionTablesJson.append(String.format("        \"type\": \"%s\",\n", escapeJson(col.getType())));
                dimensionTablesJson.append(String.format("        \"description\": \"%s\"\n", escapeJson(col.getDescription())));
                dimensionTablesJson.append("      }");
                if (i < dim.getColumns().size() - 1) {
                    dimensionTablesJson.append(",");
                }
                dimensionTablesJson.append("\n");
            }

            dimensionTablesJson.append("    ],\n");

            // 添加主键信息
            dimensionTablesJson.append("    \"primaryKeys\": [");
            if (dim.getPrimaryKeys() != null && !dim.getPrimaryKeys().isEmpty()) {
                for (int i = 0; i < dim.getPrimaryKeys().size(); i++) {
                    dimensionTablesJson.append(String.format("\"%s\"", escapeJson(dim.getPrimaryKeys().get(i))));
                    if (i < dim.getPrimaryKeys().size() - 1) {
                        dimensionTablesJson.append(", ");
                    }
                }
            }
            dimensionTablesJson.append("]\n");

            dimensionTablesJson.append("  }");
            if (d < dimensions.size() - 1) {
                dimensionTablesJson.append(",");
            }
            dimensionTablesJson.append("\n");
        }

        dimensionTablesJson.append("]");

        prompt.append(
                """
                你是专业数据仓库建模工程师。
                我将以 JSON 格式提供 1 张事实表和多张维度表,每个字段包含:字段名称、字段类型、字段注释、是否主键。
                
                请你:
                1. 根据主键字段和字段名称语义,自动匹配事实表外键与维度表主键的关联关系。
                2. 只输出标准 JSON,不输出任何多余文字、解释、说明。
                3. 不编造字段,不虚构关联。
                
                输入 JSON 结构:
                """
        );
        // 组装完整的JSON输入
        prompt.append("{\n");
        prompt.append("  \"fact_table\": ").append(factTableJson.toString().replaceAll("\n", "\n  ")).append(",\n");
        prompt.append("  \"dimension_tables\": ").append(dimensionTablesJson.toString().replaceAll("\n", "\n  ")).append("\n");
        prompt.append("}\n");

        prompt.append("""
                    输出 JSON 结构:
                {
                  "success":"是否匹配成功;false:否 false:是",
                  "associations": [
                    {
                      "dimensionTable": "维度表名",
                      "factColumnName": "事实表外键字段名",
                      "dimensionColumnName": "维度表主键字段名",
                      "matchReason": "匹配依据"
                    }
                  ]
                }
                            
                现在接收我的输入 JSON 并只返回结果 JSON。
                    """);

        return prompt.toString();
    }

    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private boolean containsAny(String text, String... keywords) {
        if (text == null || keywords.length == 0) {
            return false;
        }
        String lowerText = text.toLowerCase();
        for (String keyword : keywords) {
            if (keyword != null && lowerText.contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
