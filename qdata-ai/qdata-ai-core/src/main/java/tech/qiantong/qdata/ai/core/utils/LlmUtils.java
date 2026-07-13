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
import tech.qiantong.qdata.common.exception.ServiceException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * LLM calling tool class
 * Provides a standardized LLM calling interface, supporting both streaming and non-streaming modes
 * Unified handling of exceptions and thread scheduling
 */
@Slf4j
public class LlmUtils {

    // ==================== Streaming calling method ====================

    /**
     * Perform LLM streaming calls
     *
     * @param chatModel LLM model client
     * @param messages message list
     * @return Flux stream of ChatResponse
     */
    public static Flux<ChatResponse> streamLlmResponse(ChatModel chatModel, List<Message> messages) {
        return streamLlmResponse(chatModel, new Prompt(messages));
    }

    /**
     * Execute LLM streaming call (core method)
     *
     * @param chatModel LLM model client
     * @param prompt prompt word object
     * @return Flux stream of ChatResponse
     */
    public static Flux<ChatResponse> streamLlmResponse(ChatModel chatModel, Prompt prompt) {
        return chatModel.stream(prompt)
                .doOnSubscribe(subscription -> {
                    log.debug("LLM流式调用已订阅");
                })
                .doOnNext(response -> {
                    if (log.isTraceEnabled()) {
                        log.trace("收到LLM响应片段");
                    }
                })
                .doOnComplete(() -> {
                    log.debug("LLM流式调用完成");
                })
                .doOnError(error -> {
                    log.error("LLM流式调用出错", error);
                });
    }

    // ==================== Non-streaming (direct) calling method ====================

    /**
     * Perform LLM non-streaming calls (direct output)
     *
     * @param chatModel LLM model client
     * @param messages message list
     * @return Mono containing ChatResponse
     */
    public static ChatResponse callLlm(ChatModel chatModel, List<Message> messages) {
        return callLlm(chatModel, new Prompt(messages));
    }

    /**
     * Execute LLM non-streaming call (direct output) - core method
     *
     * @param chatModel LLM model client
     * @param prompt prompt word object
     * @return Mono containing ChatResponse
     */
    public static ChatResponse callLlm(ChatModel chatModel, Prompt prompt) {
        try {
            log.debug("开始同步LLM调用");
            ChatResponse response = chatModel.call(prompt);
            log.debug("同步LLM调用完成");
            return response;
        } catch (Exception e) {
            log.error("同步LLM调用出错", e);
            throw new ServiceException("ai.error.llm.call.fail", "LLM调用失败");
        }
    }
}
