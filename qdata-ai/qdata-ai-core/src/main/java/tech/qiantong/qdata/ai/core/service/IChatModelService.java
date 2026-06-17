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
