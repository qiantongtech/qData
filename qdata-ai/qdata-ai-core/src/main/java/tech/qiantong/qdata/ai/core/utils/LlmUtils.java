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
 * LLM调用工具类
 * 提供标准化的LLM调用接口，支持流式和非流式两种模式
 * 统一处理异常和线程调度
 */
@Slf4j
public class LlmUtils {

    // ==================== 流式调用方法 ====================

    /**
     * 执行LLM流式调用
     *
     * @param chatModel LLM模型客户端
     * @param messages 消息列表
     * @return ChatResponse的Flux流
     */
    public static Flux<ChatResponse> streamLlmResponse(ChatModel chatModel, List<Message> messages) {
        return streamLlmResponse(chatModel, new Prompt(messages));
    }

    /**
     * 执行LLM流式调用（核心方法）
     *
     * @param chatModel LLM模型客户端
     * @param prompt 提示词对象
     * @return ChatResponse的Flux流
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

    // ==================== 非流式（直接）调用方法 ====================

    /**
     * 执行LLM非流式调用（直接输出）
     *
     * @param chatModel LLM模型客户端
     * @param messages 消息列表
     * @return 包含ChatResponse的Mono
     */
    public static ChatResponse callLlm(ChatModel chatModel, List<Message> messages) {
        return callLlm(chatModel, new Prompt(messages));
    }

    /**
     * 执行LLM非流式调用（直接输出）- 核心方法
     *
     * @param chatModel LLM模型客户端
     * @param prompt 提示词对象
     * @return 包含ChatResponse的Mono
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
