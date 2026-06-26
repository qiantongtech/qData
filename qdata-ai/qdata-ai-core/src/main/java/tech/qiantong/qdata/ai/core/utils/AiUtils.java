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

package tech.qiantong.qdata.ai.core.utils;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.deepseek.DeepSeekAssistantMessage;

/**
 * AI常用工具类
 * @author wang
 * @date 2025/12/22 09:45
 **/
public class AiUtils {

    /**
     * 删除深度思考块 (默认深度思考标签)
     * @param content 原文内容
     * @return 处理后的内容
     */
    public static String deleteDeepThinkingBlock(String content) {
        // 默认的深度思考块标签
        String startTag = "<think";
        String endTag = "</think>";

        return deleteDeepThinkingBlock(content, startTag, endTag);
    }

    /**
     * 删除深度思考块 (自定义标签)
     * @param content 原文内容
     * @param startTag 开始标签
     * @param endTag 结束标签
     * @return 处理后的内容
     */
    public static String deleteDeepThinkingBlock(String content, String startTag, String endTag) {
        if (content == null) {
            return "";
        }

        int startIndex = content.indexOf(startTag);

        if (startIndex == -1) {
            // 如果没有深度思考块，则直接返回原文
            return content;
        }

        String afterStart = content.substring(startIndex);
        int endIndex = afterStart.indexOf(endTag);

        String remainingContent = content.substring(0, startIndex);
        if (endIndex != -1) {
            int end = endIndex + endTag.length();
            remainingContent += afterStart.substring(end);
        }

        return remainingContent;
    }

    /**
     * 获取 ChatResponse 内容
     * @param response 响应
     * @return 内容
     */
    @SuppressWarnings("ConstantValue")
    public static String getChatResponseContent(ChatResponse response) {
        if (response == null
                || response.getResult() == null
                || response.getResult().getOutput() == null) {
            return null;
        }
        return response.getResult().getOutput().getText();
    }

    /**
     * 获取深度思考块内容
     * @param response 响应
     * @return 深度思考块内容
     */
    @SuppressWarnings("ConstantValue")
    public static String getChatResponseReasoningContent(ChatResponse response) {
        if (response == null
                || response.getResult() == null
                || response.getResult().getOutput() == null) {
            return null;
        }
        if (response.getResult().getOutput() instanceof DeepSeekAssistantMessage) {
            return ((DeepSeekAssistantMessage) (response.getResult().getOutput())).getReasoningContent();
        }
        return null;
    }
}
