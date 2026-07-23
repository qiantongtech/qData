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

package tech.qiantong.qdata.config;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import tech.qiantong.qdata.common.constant.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;

/**
 * Custom language parser
 *
 * Priority:
 * 1. lang parameter (user manually switches, setLocale is triggered by LocaleChangeInterceptor and stored in Session)
 * 2. Accept-Language request header (automatically carried by front-end axios)
 * 3. Default Simplified Chinese
 *
 * @author qdata
 */
public class HeaderLocaleResolver implements LocaleResolver
{
    /** The key to store language attributes in Session */
    private static final String LOCALE_SESSION_ATTRIBUTE = "QDATA_LOCALE";

    @Override
    public Locale resolveLocale(HttpServletRequest request)
    {
        // ① Prioritize reading from Session (user saves after manually switching through?lang= parameter)
        HttpSession session = request.getSession(false);
        if (session != null)
        {
            Locale sessionLocale = (Locale) session.getAttribute(LOCALE_SESSION_ATTRIBUTE);
            if (sessionLocale != null)
            {
                return sessionLocale;
            }
        }

        // ② Parse from Accept-Language request header
        String acceptLanguage = request.getHeader("Accept-Language");
        if (StringUtils.hasText(acceptLanguage))
        {
            Locale headerLocale = parseAcceptLanguage(acceptLanguage);
            if (headerLocale != null)
            {
                return headerLocale;
            }
        }

        // ③ The default setting is Simplified Chinese
        return Constants.DEFAULT_LOCALE;
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale)
    {
        // Store the manually switched language in Session (called by LocaleChangeInterceptor)
        HttpSession session = request.getSession();
        if (session != null)
        {
            session.setAttribute(LOCALE_SESSION_ATTRIBUTE, locale);
        }
    }

    /**
     * Parse Locale from Accept-Language header
     * Supported formats: zh-CN,zh;q=0.9,en;q=0.8 → Take the zh-CN with the highest weight
     */
    private Locale parseAcceptLanguage(String acceptLanguage)
    {
        try
        {
            // Accept-Language format example: "zh-CN,zh;q=0.9,en;q=0.8,ja;q=0.7"
            String[] locales = acceptLanguage.split(",");
            if (locales.length > 0)
            {
                // Take the first one (highest weight)
                String primaryLang = locales[0].trim();
                // Remove the q value part (such as "zh-CN;q=0.9" → "zh-CN")
                int qIndex = primaryLang.indexOf(';');
                if (qIndex > 0)
                {
                    primaryLang = primaryLang.substring(0, qIndex);
                }
                // Convert "zh-CN" → Locale
                return Locale.forLanguageTag(primaryLang.replace('_', '-'));
            }
        }
        catch (Exception e)
        {
            // Parsing fails and null is returned.
        }
        return null;
    }
}
