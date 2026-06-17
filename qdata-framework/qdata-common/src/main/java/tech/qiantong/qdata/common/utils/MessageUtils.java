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
        if (!Locale.ENGLISH.getLanguage().equals(currentLocale.getLanguage()))
        {
            msg = resolveMessage(messageSource, code, args, Locale.ENGLISH);
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
