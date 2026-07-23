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
 * AI model platform
 *
 * @author wang
 */
@Getter
@AllArgsConstructor
public enum AiPlatformEnum {

    // ========== Domestic Platform ==========

    TONG_YI("TongYi", "通义千问", "https://dashscope.aliyuncs.com/api/v1"), // Ali
    YI_YAN("YiYan", "文心一言", ""), // Baidu
    DEEP_SEEK("DeepSeek", "DeepSeek", "https://api.deepseek.com/v1"), // DeepSeek
    ZHI_PU("ZhiPu", "智谱", ""), // Wisdom spectrum AI
    XING_HUO("XingHuo", "星火", ""), // IFlytek
    DOU_BAO("DouBao", "豆包", ""), // Bytes
    HUN_YUAN("HunYuan", "混元", ""), // Tencent
    SILICON_FLOW("SiliconFlow", "硅基流动", ""), // Silicon based flow
    MINI_MAX("MiniMax", "MiniMax", ""), // Xiyu Technology
    MOONSHOT("Moonshot", "月之暗面", ""), // KIMI
    BAI_CHUAN("BaiChuan", "百川智能", ""), // Baichuan Intelligence

    // ========== Overseas Platform ==========

    OPENAI("OpenAI", "OpenAI", ""), // OpenAI official
    AZURE_OPENAI("AzureOpenAI", "AzureOpenAI", ""), // OpenAI Microsoft
    ANTHROPIC("Anthropic", "Anthropic", ""), // Anthropic Claude
    GEMINI("Gemini", "Gemini", ""), // Google Gemini
    OLLAMA("Ollama", "Ollama", ""),

    STABLE_DIFFUSION("StableDiffusion", "StableDiffusion", ""), // Stability AI
    MIDJOURNEY("Midjourney", "Midjourney", ""), // Midjourney
    SUNO("Suno", "Suno", ""), // Suno AI
    GROK("Grok", "Grok", ""), // Grok

    ;

    /**
     * Platform
     */
    private final String platform;
    /**
     * Platform name
     */
    private final String name;

    /**
     * openAiUrl address, ending with v1 without /
     */
    private final String openAiUrl;

    public static final String[] ARRAYS = Arrays.stream(values()).map(AiPlatformEnum::getPlatform).toArray(String[]::new);

    public static AiPlatformEnum validatePlatform(String platform) {
        for (AiPlatformEnum platformEnum : AiPlatformEnum.values()) {
            if (platformEnum.getPlatform().equals(platform)) {
                return platformEnum;
            }
        }
        throw new ServiceException("ai.error.platform.invalid", "Invalid platform", platform);
    }

    public String[] array() {
        return ARRAYS;
    }

}
