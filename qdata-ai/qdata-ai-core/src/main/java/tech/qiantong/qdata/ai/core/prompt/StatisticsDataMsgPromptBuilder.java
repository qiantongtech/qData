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

package tech.qiantong.qdata.ai.core.prompt;

import org.springframework.stereotype.Component;
import tech.qiantong.qdata.common.utils.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * <P>
 * Purpose: Statistical data analysis prompt word construction
 * </p>
 *
 * @author: FXB
 * @create: 2026-04-10 16:33
 **/
@Component
public class StatisticsDataMsgPromptBuilder {

    private static final String SYSTEM_PROMPT = """
            你是一位资深的数据分析专家和商业智能顾问。基于提供的统计数据，进行深入的业务分析和洞察挖掘。

            分析规则：
            1. 只返回JSON格式，不要任何额外的解释文本
            2. 分析必须基于实际数据，避免主观臆断
            3. 识别关键趋势、异常值和业务洞察
            4. 提供可操作的建议和结论
            5. 使用清晰、专业的商业语言
            6. 关注数据背后的业务含义
            7. 突出重要发现和变化
            8. 如有必要，提供对比分析

            输出JSON格式：
            {
                "summary": "数据概览总结",
                "keyFindings": ["关键发现1", "关键发现2", "关键发现3"],
                "trends": ["趋势分析1", "趋势分析2"],
                "anomalies": ["异常点1", "异常点2"],
                "insights": ["业务洞察1", "业务洞察2"],
                "recommendations": ["建议1", "建议2", "建议3"],
                "conclusion": "最终结论"
            }
            """;

    public String buildPrompt(
            List<String> selectColumns,
            List<String> selectColumnDescriptions,
            String dimension,
            List<String> measures,
            String timeGrain,
            List<Map<String, Object>> dataRows,
            Integer maxRows
    ) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("""
                你是一位资深的数据分析专家和商业智能顾问。基于提供的统计数据，进行深入的业务分析和洞察挖掘。

                分析要求：
                    1. 只返回JSON格式，不要任何额外的解释文本
                    2. 分析必须基于实际数据，避免主观臆断
                    3. 识别关键趋势、异常值和业务洞察
                    4. 提供可操作的建议和结论
                    5. 使用清晰、专业的商业语言
                    6. 关注数据背后的业务含义
                    7. 突出重要发现和变化
                    8. 如有必要，提供对比分析
                """);

        // 1. Data structure description
        prompt.append("\n【数据结构】\n");
        if (StringUtils.isNotBlank(dimension)) {
            prompt.append(String.format("维度字段: %s\n", dimension));
        }
        if (StringUtils.isNotBlank(timeGrain)) {
            prompt.append(String.format("时间粒度: %s\n", timeGrain));
        }
        if (measures != null && !measures.isEmpty()) {
            prompt.append("度量字段: ").append(String.join(", ", measures)).append("\n");
        }

        // 2. Field description
        if (selectColumns != null && !selectColumns.isEmpty()
                && selectColumnDescriptions != null && !selectColumnDescriptions.isEmpty()) {
            prompt.append("\n【字段说明】\n");
            for (int i = 0; i < Math.min(selectColumns.size(), selectColumnDescriptions.size()); i++) {
                prompt.append(String.format("- %s: %s\n",
                        selectColumns.get(i),
                        selectColumnDescriptions.get(i)));
            }
        }

        // 3. Data sample
        if (dataRows != null && !dataRows.isEmpty()) {
            prompt.append("\n【统计数据】\n");

            int displayRows = (maxRows != null && maxRows > 0)
                    ? Math.min(dataRows.size(), maxRows)
                    : Math.min(dataRows.size(), 50);

            prompt.append(String.format("总记录数: %d\n", dataRows.size()));
            prompt.append(String.format("展示前 %d 条数据:\n", displayRows));

            // Construct a mapping from field names to descriptions
            Map<String, String> columnDescriptionMap = new java.util.HashMap<>();
            if (selectColumns != null && selectColumnDescriptions != null) {
                for (int i = 0; i < Math.min(selectColumns.size(), selectColumnDescriptions.size()); i++) {
                    columnDescriptionMap.put(selectColumns.get(i), selectColumnDescriptions.get(i));
                }
            }

            for (int i = 0; i < displayRows; i++) {
                Map<String, Object> row = dataRows.get(i);
                prompt.append(String.format("记录 %d: ", i + 1));

                List<String> parts = new java.util.ArrayList<>();
                for (Map.Entry<String, Object> entry : row.entrySet()) {
                    String fieldName = entry.getKey();
                    String displayName = columnDescriptionMap.getOrDefault(fieldName, fieldName);
                    String value = entry.getValue() != null ? entry.getValue().toString() : "null";
                    parts.add(String.format("%s=%s", displayName, value));
                }
                prompt.append(String.join(", ", parts)).append("\n");
            }

            if (dataRows.size() > displayRows) {
                prompt.append(String.format("... 还有 %d 条数据未显示\n", dataRows.size() - displayRows));
            }
        }

        // 4. Output format requirements
        prompt.append("\n\n请基于以上数据生成JSON格式的分析报告：");
        prompt.append("""

                输出JSON格式：
                {
                    "summary": "数据概览总结（100字以内）",
                    "keyFindings": ["关键发现1", "关键发现2", "关键发现3"],
                    "trends": ["趋势分析1", "趋势分析2"],
                    "anomalies": ["异常点1", "异常点2"],
                    "insights": ["业务洞察1", "业务洞察2"],
                    "recommendations": ["建议1", "建议2", "建议3"],
                    "conclusion": "最终结论（50字以内）"
                }
                """);
        return prompt.toString();
    }

}
