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

package tech.qiantong.qdata.config;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import tech.qiantong.qdata.common.constant.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;

/**
 * 自定义语言解析器
 *
 * 优先级：
 *   1. lang 参数（用户手动切换，由 LocaleChangeInterceptor 触发 setLocale 存入 Session）
 *   2. Accept-Language 请求头（前端 axios 自动携带）
 *   3. 默认简体中文
 *
 * @author qdata
 */
public class HeaderLocaleResolver implements LocaleResolver
{
    /** Session 中存储语言属性的 key */
    private static final String LOCALE_SESSION_ATTRIBUTE = "QDATA_LOCALE";

    @Override
    public Locale resolveLocale(HttpServletRequest request)
    {
        // ① 优先从 Session 读取（用户通过 ?lang= 参数手动切换后存入）
        HttpSession session = request.getSession(false);
        if (session != null)
        {
            Locale sessionLocale = (Locale) session.getAttribute(LOCALE_SESSION_ATTRIBUTE);
            if (sessionLocale != null)
            {
                return sessionLocale;
            }
        }

        // ② 从 Accept-Language 请求头解析
        String acceptLanguage = request.getHeader("Accept-Language");
        if (StringUtils.hasText(acceptLanguage))
        {
            Locale headerLocale = parseAcceptLanguage(acceptLanguage);
            if (headerLocale != null)
            {
                return headerLocale;
            }
        }

        // ③ 兜底默认简体中文
        return Constants.DEFAULT_LOCALE;
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale)
    {
        // 将手动切换的语言存入 Session（由 LocaleChangeInterceptor 调用）
        HttpSession session = request.getSession();
        if (session != null)
        {
            session.setAttribute(LOCALE_SESSION_ATTRIBUTE, locale);
        }
    }

    /**
     * 从 Accept-Language 头解析 Locale
     * 支持格式：zh-CN,zh;q=0.9,en;q=0.8 → 取权重最高的 zh-CN
     */
    private Locale parseAcceptLanguage(String acceptLanguage)
    {
        try
        {
            // Accept-Language 格式示例: "zh-CN,zh;q=0.9,en;q=0.8,ja;q=0.7"
            String[] locales = acceptLanguage.split(",");
            if (locales.length > 0)
            {
                // 取第一个（权重最高）
                String primaryLang = locales[0].trim();
                // 去掉 q 值部分（如 "zh-CN;q=0.9" → "zh-CN"）
                int qIndex = primaryLang.indexOf(';');
                if (qIndex > 0)
                {
                    primaryLang = primaryLang.substring(0, qIndex);
                }
                // 转换 "zh-CN" → Locale
                return Locale.forLanguageTag(primaryLang.replace('_', '-'));
            }
        }
        catch (Exception e)
        {
            // 解析失败，返回 null 走兜底
        }
        return null;
    }
}
