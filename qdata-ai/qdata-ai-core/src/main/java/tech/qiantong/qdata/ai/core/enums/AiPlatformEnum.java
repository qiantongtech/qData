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

package tech.qiantong.qdata.ai.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
        throw new IllegalArgumentException("非法平台： " + platform);
    }

    public String[] array() {
        return ARRAYS;
    }

}
