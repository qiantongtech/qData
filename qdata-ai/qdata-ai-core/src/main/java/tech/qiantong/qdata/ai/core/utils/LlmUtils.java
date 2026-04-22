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

package tech.qiantong.qdata.ai.core.utils;

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
            throw new RuntimeException("LLM调用失败", e);
        }
    }
}
