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
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.context.request.RequestContextHolder;
import tech.qiantong.qdata.common.utils.spring.SpringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * i18n resource file utility
 *
 * @author qdata
 */
public class MessageUtils
{
    private static final Logger log = LoggerFactory.getLogger(MessageUtils.class);

    /** Final fallback error message */
    private static final String DEFAULT_ERROR_MESSAGE = "系统异常，请联系管理员";

    /**
     * Fallback used by standalone executors which intentionally do not start a
     * Spring application context (for example, the Spark ETL executable).
     */
    private static final MessageSource STANDALONE_MESSAGE_SOURCE = createStandaloneMessageSource();

    /**
     * Get message by key and parameters, delegating to Spring MessageSource
     *
     * @param code message key
     * @param args parameters
     * @return i18n translated value
     */
    public static String message(String code, Object... args)
    {
        MessageSource messageSource = getMessageSource();
        Locale locale = messageSource == STANDALONE_MESSAGE_SOURCE
                ? Locale.US : getCurrentLocaleOrEnglishDefault();
        return messageSource.getMessage(code, args, locale);
    }

    /**
     * Get English message by key and parameters, ignoring the current request locale
     *
     * @param code message key
     * @param args parameters
     * @return English translated value
     */
    public static String messageEn(String code, Object... args)
    {
        MessageSource messageSource = getMessageSource();
        return messageSource.getMessage(code, args, Locale.US);
    }

    /**
     * Get message by key with fallback chain:
     *   current locale -> English -> Simplified Chinese -> defaultMessage -> hardcoded fallback
     *
     * @param code message key (corresponding to key in messages.properties)
     * @param defaultMessage fallback message
     * @param args format parameters
     * @return i18n translated value, guaranteed non-null
     */
    public static String messageWithFallback(String code, String defaultMessage, Object... args)
    {
        MessageSource messageSource = getMessageSource();
        Locale currentLocale = messageSource == STANDALONE_MESSAGE_SOURCE
                ? Locale.US : getCurrentLocaleOrEnglishDefault();

        // 1. Try current request locale
        String msg = resolveMessage(messageSource, code, args, currentLocale);
        if (msg != null)
        {
            return msg;
        }

        // 2. Try English
        if (!Locale.US.getLanguage().equals(currentLocale.getLanguage()))
        {
            msg = resolveMessage(messageSource, code, args, Locale.US);
            if (msg != null)
            {
                return msg;
            }
        }

        // 3. Try Simplified Chinese
        if (!Locale.SIMPLIFIED_CHINESE.getLanguage().equals(currentLocale.getLanguage())
                || !Locale.SIMPLIFIED_CHINESE.equals(currentLocale))
        {
            msg = resolveMessage(messageSource, code, args, Locale.SIMPLIFIED_CHINESE);
            if (msg != null)
            {
                return msg;
            }
        }

        // 4. Use the provided defaultMessage
        if (!StringUtils.isEmpty(defaultMessage))
        {
            return defaultMessage;
        }

        // 5. Final hardcoded fallback
        log.warn("Cannot resolve the localized translation for message key '{}'; using the default error message", code);
        return DEFAULT_ERROR_MESSAGE;
    }





    /**
     * Get English message by key with fallback chain:
     *   English -> defaultMessage -> hardcoded fallback
     *
     * Differs from messageEn: does not throw an exception when English translation is missing, uses fallback logic instead.
     *
     * @param code message key (corresponding to key in messages.properties)
     * @param defaultMessage fallback message, returned when English translation is not found
     * @param args format parameters
     * @return English translated value, guaranteed non-null
     */
    public static String messageEnWithFallback(String code, String defaultMessage, Object... args)
    {

        MessageSource messageSource = getMessageSource();

        // 1. Try English
        String msg = resolveMessage(messageSource, code, args, Locale.US);
        if (msg != null)
        {
            return msg;
        }

        // 2. Use the provided defaultMessage
        if (!StringUtils.isEmpty(defaultMessage))
        {
            return defaultMessage;
        }

        // 3. Final hardcoded fallback
        log.warn("Cannot resolve the English translation for message key '{}'; using the default error message", code);
        return DEFAULT_ERROR_MESSAGE;
    }

    /**
     * Resolve message from MessageSource without throwing exceptions
     *
     * @return resolved message, or null if not found
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

    private static MessageSource getMessageSource()
    {
        try
        {
            return SpringUtils.getBean(MessageSource.class);
        }
        catch (RuntimeException e)
        {
            log.debug("Spring MessageSource is unavailable; using the standalone resource bundle", e);
            return STANDALONE_MESSAGE_SOURCE;
        }
    }

    private static MessageSource createStandaloneMessageSource()
    {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        List<String> basenames = new ArrayList<>();
        addBasenameIfPresent(basenames, "i18n/messages");
        addBasenameIfPresent(basenames, "i18n/etl-messages");
        messageSource.setBasenames(basenames.toArray(new String[0]));
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setFallbackToSystemLocale(false);
        return messageSource;
    }

    private static void addBasenameIfPresent(List<String> basenames, String basename)
    {
        String resource = basename + ".properties";
        if (MessageUtils.class.getClassLoader().getResource(resource) != null)
        {
            basenames.add(basename);
        }
    }

    /**
     * Use the request locale only when the browser provides Accept-Language.
     * If there is no web request or the header is missing, default to English.
     */
    private static Locale getCurrentLocaleOrEnglishDefault()
    {
        Object attributes = RequestContextHolder.getRequestAttributes();
        if (attributes == null)
        {
            return Locale.US;
        }

        if (StringUtils.isEmpty(getRequestHeader(attributes, "Accept-Language")))
        {
            return Locale.US;
        }
        return LocaleContextHolder.getLocale();
    }

    /**
     * Read a request header without linking against javax.servlet or jakarta.servlet.
     *
     * Spring Framework 5 and 6 expose ServletRequestAttributes#getRequest with
     * different return types. Invoking it reflectively keeps this shared module
     * binary-compatible with both servlet namespaces.
     */
    private static String getRequestHeader(Object attributes, String headerName)
    {
        try
        {
            Method getRequest = attributes.getClass().getMethod("getRequest");
            Object request = getRequest.invoke(attributes);
            if (request == null)
            {
                return null;
            }

            Method getHeader = request.getClass().getMethod("getHeader", String.class);
            Object headerValue = getHeader.invoke(request, headerName);
            return headerValue instanceof String ? (String) headerValue : null;
        }
        catch (ReflectiveOperationException | SecurityException e)
        {
            log.trace("Unable to read request header '{}'; using the English locale fallback", headerName, e);
            return null;
        }
    }
}
