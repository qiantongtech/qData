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

package tech.qiantong.qdata.ai.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tech.qiantong.qdata.common.exception.ServiceException;

import java.util.Arrays;

/**
 * AI 模型平台
 *
 * @author wang
 */
@Getter
@AllArgsConstructor
public enum AiPlatformEnum {

    // ========== 国内平台 ==========

    TONG_YI("TongYi", "通义千问", "https://dashscope.aliyuncs.com/api/v1"), // 阿里
    YI_YAN("YiYan", "文心一言", ""), // 百度
    DEEP_SEEK("DeepSeek", "DeepSeek", "https://api.deepseek.com/v1"), // DeepSeek
    ZHI_PU("ZhiPu", "智谱", ""), // 智谱 AI
    XING_HUO("XingHuo", "星火", ""), // 讯飞
    DOU_BAO("DouBao", "豆包", ""), // 字节
    HUN_YUAN("HunYuan", "混元", ""), // 腾讯
    SILICON_FLOW("SiliconFlow", "硅基流动", ""), // 硅基流动
    MINI_MAX("MiniMax", "MiniMax", ""), // 稀宇科技
    MOONSHOT("Moonshot", "月之暗面", ""), // KIMI
    BAI_CHUAN("BaiChuan", "百川智能", ""), // 百川智能

    // ========== 国外平台 ==========

    OPENAI("OpenAI", "OpenAI", ""), // OpenAI 官方
    AZURE_OPENAI("AzureOpenAI", "AzureOpenAI", ""), // OpenAI 微软
    ANTHROPIC("Anthropic", "Anthropic", ""), // Anthropic Claude
    GEMINI("Gemini", "Gemini", ""), // 谷歌 Gemini
    OLLAMA("Ollama", "Ollama", ""),

    STABLE_DIFFUSION("StableDiffusion", "StableDiffusion", ""), // Stability AI
    MIDJOURNEY("Midjourney", "Midjourney", ""), // Midjourney
    SUNO("Suno", "Suno", ""), // Suno AI
    GROK("Grok", "Grok", ""), // Grok

    ;

    /**
     * 平台
     */
    private final String platform;
    /**
     * 平台名
     */
    private final String name;

    /**
     * openAiUrl 地址，以 v1 结尾，不带 /
     */
    private final String openAiUrl;

    public static final String[] ARRAYS = Arrays.stream(values()).map(AiPlatformEnum::getPlatform).toArray(String[]::new);

    public static AiPlatformEnum validatePlatform(String platform) {
        for (AiPlatformEnum platformEnum : AiPlatformEnum.values()) {
            if (platformEnum.getPlatform().equals(platform)) {
                return platformEnum;
            }
        }
        throw new ServiceException("ai.error.platform.invalid", "非法平台： " + platform, platform);
    }

    public String[] array() {
        return ARRAYS;
    }

}
