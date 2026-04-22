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

package tech.qiantong.qdata.ai.core.prompt.params;

import java.util.List;
import java.util.Objects;

public enum AggregationType {
    // 基础统计
    SUM("求和", List.of("累加", "合计", "总量", "总额", "累计")),
    COUNT("计数", List.of("次数", "笔数", "条数", "记录数", "样本量")),
    DISTINCT_COUNT("去重计数", List.of("独立数", "唯一数", "去重后数量", "唯一客户数")),
    AVG("平均", List.of("均值", "平均值", "平均水平")),

    // 极值统计
    MIN("最小值", List.of("最低", "最小", "最少", "底线值")),
    MAX("最大值", List.of("最高", "最大", "最多", "峰值")),

    // 高级分析
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
