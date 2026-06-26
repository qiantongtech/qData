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
