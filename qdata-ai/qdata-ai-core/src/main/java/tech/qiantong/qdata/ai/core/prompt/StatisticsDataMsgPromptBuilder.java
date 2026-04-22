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
import tech.qiantong.qdata.common.utils.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * <P>
 * 用途:统计数据分析提示词构建
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

        // 1. 数据结构说明
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

        // 2. 字段说明
        if (selectColumns != null && !selectColumns.isEmpty()
                && selectColumnDescriptions != null && !selectColumnDescriptions.isEmpty()) {
            prompt.append("\n【字段说明】\n");
            for (int i = 0; i < Math.min(selectColumns.size(), selectColumnDescriptions.size()); i++) {
                prompt.append(String.format("- %s: %s\n",
                        selectColumns.get(i),
                        selectColumnDescriptions.get(i)));
            }
        }

        // 3. 数据样本
        if (dataRows != null && !dataRows.isEmpty()) {
            prompt.append("\n【统计数据】\n");

            int displayRows = (maxRows != null && maxRows > 0)
                    ? Math.min(dataRows.size(), maxRows)
                    : Math.min(dataRows.size(), 50);

            prompt.append(String.format("总记录数: %d\n", dataRows.size()));
            prompt.append(String.format("展示前 %d 条数据:\n", displayRows));

            // 构建字段名到描述的映射
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

        // 4. 输出格式要求
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
