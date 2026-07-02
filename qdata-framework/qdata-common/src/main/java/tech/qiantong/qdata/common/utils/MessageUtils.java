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

package tech.qiantong.qdata.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import tech.qiantong.qdata.common.utils.spring.SpringUtils;

import java.util.Locale;

/**
 * 获取 i18n 资源文件
 *
 * @author qdata
 */
public class MessageUtils
{
    private static final Logger log = LoggerFactory.getLogger(MessageUtils.class);

    /** 最终兜底错误消息 */
    private static final String DEFAULT_ERROR_MESSAGE = "系统异常，请联系管理员";

    /**
     * 根据消息键和参数获取消息，委托给 spring messageSource
     *
     * @param code 消息键
     * @param args 参数
     * @return 国际化翻译值
     */
    public static String message(String code, Object... args)
    {
        MessageSource messageSource = SpringUtils.getBean(MessageSource.class);
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    /**
     * 根据消息键和参数获取英文消息，忽略当前请求语言设置
     *
     * @param code 消息键
     * @param args 参数
     * @return 英文翻译值
     */
    public static String messageEn(String code, Object... args)
    {
        MessageSource messageSource = SpringUtils.getBean(MessageSource.class);
        return messageSource.getMessage(code, args, Locale.US);
    }

    /**
     * 根据消息键获取消息，支持兜底链：
     *   当前语言 → 英文 → 简体中文 → defaultMessage → 硬编码兜底
     *
     * @param code 消息键（对应 messages.properties 中的 key）
     * @param defaultMessage 兜底消息
     * @param args 格式化参数
     * @return 国际化翻译值，保证不返回 null
     */
    public static String messageWithFallback(String code, String defaultMessage, Object... args)
    {
        MessageSource messageSource = SpringUtils.getBean(MessageSource.class);
        Locale currentLocale = LocaleContextHolder.getLocale();

        // 1. 尝试当前请求语言
        String msg = resolveMessage(messageSource, code, args, currentLocale);
        if (msg != null)
        {
            return msg;
        }

        // 2. 尝试英文
        if (!Locale.US.getLanguage().equals(currentLocale.getLanguage()))
        {
            msg = resolveMessage(messageSource, code, args, Locale.US);
            if (msg != null)
            {
                return msg;
            }
        }

        // 3. 尝试简体中文
        if (!Locale.SIMPLIFIED_CHINESE.getLanguage().equals(currentLocale.getLanguage())
                || !Locale.SIMPLIFIED_CHINESE.equals(currentLocale))
        {
            msg = resolveMessage(messageSource, code, args, Locale.SIMPLIFIED_CHINESE);
            if (msg != null)
            {
                return msg;
            }
        }

        // 4. 使用传入的 defaultMessage
        if (!StringUtils.isEmpty(defaultMessage))
        {
            return defaultMessage;
        }

        // 5. 最终硬编码兜底
        log.warn("无法获取消息键'{}'的国际化翻译，使用默认错误消息", code);
        return DEFAULT_ERROR_MESSAGE;
    }

    /**
     * 从 MessageSource 解析消息，不抛出异常
     *
     * @return 解析到的消息，未找到返回 null
     */
    private static String resolveMessage(MessageSource messageSource, String code, Object[] args, Locale locale)
    {
        try
        {
            return messageSource.getMessage(code, args != null ? args : new Object[0], locale);
        }
        catch (NoSuchMessageException e)
        {
            return null;
        }
    }
}
