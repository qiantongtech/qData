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

import java.util.List;
import java.util.Objects;

public enum AggregationType {
    // Basic statistics
    SUM("求和", List.of("累加", "合计", "总量", "总额", "累计")),
    COUNT("计数", List.of("次数", "笔数", "条数", "记录数", "样本量")),
    DISTINCT_COUNT("去重计数", List.of("独立数", "唯一数", "去重后数量", "唯一客户数")),
    AVG("平均", List.of("均值", "平均值", "平均水平")),

    // Extreme value statistics
    MIN("最小值", List.of("最低", "最小", "最少", "底线值")),
    MAX("最大值", List.of("最高", "最大", "最多", "峰值")),

    // Advanced analytics
    MEDIAN("中位数", List.of("中值", "中间数", "50分位")),
    STDEV("标准差", List.of("波动", "稳定性", "标准偏差")),
    VAR("方差", List.of("离散度", "变异系数")),;

    private final String displayName;
    private final List<String> synonyms;

    AggregationType(String displayName, List<String> synonyms) {
        this.displayName = displayName;
        this.synonyms = synonyms;
    }

    public static AggregationType fromText(String text) {
        for (AggregationType type : values()) {
            if (containsAny(text, type.synonyms)) {
                return type;
            }
        }
        return null;
    }


    private static boolean containsAny(String text, List<String> keywords) {
        if (text == null || keywords == null || keywords.isEmpty()) {
            return false;
        }
        String lowerText = text.toLowerCase();
        return keywords.stream()
                .filter(Objects::nonNull)
                .anyMatch(k -> lowerText.contains(k.toLowerCase()));
    }
}
