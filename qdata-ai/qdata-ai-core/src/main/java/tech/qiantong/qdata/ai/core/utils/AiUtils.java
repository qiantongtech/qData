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
