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
 * Commonly used AI tools
 * @author wang
 * @date 2025/12/22 09:45
 **/
public class AiUtils {

    /**
     * Delete Deep Thoughts block (Default Deep Thoughts tag)
     * @param content original content
     * @return processed content
     */
    public static String deleteDeepThinkingBlock(String content) {
        // Default Deep Thought block tags
        String startTag = "<think";
        String endTag = "</think>";

        return deleteDeepThinkingBlock(content, startTag, endTag);
    }

    /**
     * Delete deep thinking block (custom label)
     * @param content original content
     * @param startTag start tag
     * @param endTag end tag
     * @return processed content
     */
    public static String deleteDeepThinkingBlock(String content, String startTag, String endTag) {
        if (content == null) {
            return "";
        }

        int startIndex = content.indexOf(startTag);

        if (startIndex == -1) {
            // If there is no deep thinking block, return directly to the original text
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
     * Get ChatResponse content
     * @param response response
     * @return content
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
     * Get Deep Thought Block Content
     * @param response response
     * @return deep thinking block content
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
