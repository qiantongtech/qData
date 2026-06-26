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

package tech.qiantong.qdata.ai.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.deepseek.DeepSeekChatOptions;
import org.springframework.ai.deepseek.api.DeepSeekApi;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.stereotype.Service;
import tech.qiantong.qdata.ai.core.enums.AiPlatformEnum;
import tech.qiantong.qdata.ai.core.service.IChatModelService;
import tech.qiantong.qdata.common.database.utils.AesEncryptUtil;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.module.ai.dal.dataobject.model.AiModelDO;
import tech.qiantong.qdata.module.ai.service.model.IAiModelService;

/**
 * springAi chatModel 服务
 *
 * @author fabian
 */
@Service
public class ChatModelServiceImpl implements IChatModelService {

    @Resource
    private IAiModelService aiModelService;

    /**
     * 获取 chatModel
     *
     * @param platForm  平台名称
     * @param baseUrl   baseUrl
     * @param apiKey    apiKey
     * @param modelName 模型名称
     * @return chatModel
     */
    @Override
    public ChatModel getChatModel(String platForm, String baseUrl, String apiKey, String modelName) {
        ChatModel chatModel;
        try {
            apiKey = AesEncryptUtil.desEncrypt(apiKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (AiPlatformEnum.validatePlatform(platForm)) {
            case OPENAI -> chatModel = this.getOpenAiChatModel(baseUrl, apiKey, modelName);
            case TONG_YI -> chatModel = this.getDashScopeChatModel(baseUrl, apiKey, modelName);
            case OLLAMA -> chatModel = this.getOllamaChatModel(baseUrl, modelName);
            case DEEP_SEEK -> chatModel = this.getDeepSeekChatModel(apiKey, modelName);
            default -> throw new ServiceException("ai.error.platform.unsupported", "暂时不支持该平台");
        }
        return chatModel;
    }

    @Override
    public ChatModel getChatModel(Long modelId) {
        AiModelDO aiModelDO = aiModelService.getAiModelById(modelId);
        return this.getChatModel(aiModelDO.getPlatform(),
                aiModelDO.getApiUrl(),
                aiModelDO.getApiKey(),
                aiModelDO.getName());
    }

    /**
     * 获取 OpenAi 聊天模型
     *
     * @param baseUrl   baseUrl（必需）
     * @param apiKey    apiKey（必需）
     * @param modelName modelName（必需）
     * @return OpenAiChatModel
     */
    private OpenAiChatModel getOpenAiChatModel(String baseUrl, String apiKey, String modelName) {
        if (StrUtil.hasBlank(baseUrl, apiKey, modelName)) {
            throw new ServiceException("ai.error.field.required", "必要字段不能为空");
        }
        return OpenAiChatModel.builder()
                .openAiApi(OpenAiApi.builder().baseUrl(baseUrl).apiKey(apiKey).build())
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(modelName)
                        .build())
                .build();
    }

    /**
     * 获取 阿里百炼 聊天模型
     *
     * @param apiKey    apiKey（必需）
     * @param modelName modelName（必需）
     * @return DashScopeChatModel
     */
    private DashScopeChatModel getDashScopeChatModel(String baseUrl, String apiKey, String modelName) {
        if (StrUtil.hasBlank(apiKey, modelName)) {
            throw new ServiceException("ai.error.field.required", "必要字段不能为空");
        }
        return DashScopeChatModel.builder()
                .dashScopeApi(DashScopeApi.builder().apiKey(apiKey).build())
                .defaultOptions(DashScopeChatOptions.builder().model(modelName).build())
                .build();
    }

    /**
     * 获取 ollama 聊天模型
     *
     * @param baseUrl   baseUrl（必需）
     * @param modelName modelName（必需）
     * @return OllamaChatModel
     */
    private OllamaChatModel getOllamaChatModel(String baseUrl, String modelName) {
        if (StrUtil.hasBlank(baseUrl, modelName)) {
            throw new ServiceException("ai.error.field.required", "必要字段不能为空");
        }
        return OllamaChatModel.builder()
                .ollamaApi(OllamaApi.builder().baseUrl(baseUrl).build())
                .defaultOptions(OllamaChatOptions.builder().model(modelName).build())
                .build();
    }

    /**
     * 获取 deepseek 聊天模型
     *
     * @param apiKey    apiKey（必需）
     * @param modelName modelName（必需）
     * @return DeepSeekChatModel
     */
    private DeepSeekChatModel getDeepSeekChatModel(String apiKey, String modelName) {
        if (StrUtil.hasBlank(apiKey, modelName)) {
            throw new ServiceException("ai.error.field.required", "必要字段不能为空");
        }
        return DeepSeekChatModel.builder()
                .deepSeekApi(DeepSeekApi.builder().apiKey(apiKey).build())
                .defaultOptions(DeepSeekChatOptions.builder().model(modelName).build())
                .build();
    }
}
