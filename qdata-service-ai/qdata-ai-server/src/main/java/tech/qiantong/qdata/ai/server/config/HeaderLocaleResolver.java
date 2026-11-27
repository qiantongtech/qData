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
 * Business License: https://docs.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.ai.server.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

public class HeaderLocaleResolver implements LocaleResolver {

    private static final String LOCALE_SESSION_ATTRIBUTE = "QDATA_LOCALE";

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Locale sessionLocale = (Locale) session.getAttribute(LOCALE_SESSION_ATTRIBUTE);
            if (sessionLocale != null) {
                return sessionLocale;
            }
        }

        String acceptLanguage = request.getHeader("Accept-Language");
        if (StringUtils.hasText(acceptLanguage)) {
            Locale headerLocale = parseAcceptLanguage(acceptLanguage);
            if (headerLocale != null) {
                return headerLocale;
            }
        }

        return Locale.SIMPLIFIED_CHINESE;
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        HttpSession session = request.getSession();
        if (session != null) {
            session.setAttribute(LOCALE_SESSION_ATTRIBUTE, locale);
        }
    }

    private Locale parseAcceptLanguage(String acceptLanguage) {
        try {
            String[] locales = acceptLanguage.split(",");
            if (locales.length > 0) {
                String primaryLang = locales[0].trim();
                int qIndex = primaryLang.indexOf(';');
                if (qIndex > 0) {
                    primaryLang = primaryLang.substring(0, qIndex);
                }
                return Locale.forLanguageTag(primaryLang.replace('_', '-'));
            }
        } catch (Exception ignored) {
        }
        return null;
    }
}
