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
 */

package tech.qiantong.qdata.ai.core.service;

import org.springframework.ai.chat.model.ChatModel;

/**
 * springAi chatModel 服务
 *
 * @author fabian
 */
public interface IChatModelService {
    /**
     * 获取 chatModel
     *
     * @param platForm  平台名称
     * @param baseUrl   baseUrl
     * @param apiKey    apiKey
     * @param modelName 模型名称
     * @return chatModel
     */
    ChatModel getChatModel(String platForm, String baseUrl, String apiKey, String modelName);

    /**
     * 获取 chatModel
     *
     * @param modelId 模型id
     * @return chatModel
     */
    ChatModel getChatModel(Long modelId);
}
