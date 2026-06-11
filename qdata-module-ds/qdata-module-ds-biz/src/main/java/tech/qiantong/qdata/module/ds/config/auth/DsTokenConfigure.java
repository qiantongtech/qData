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

package tech.qiantong.qdata.module.ds.config.auth;

import cn.dev33.satoken.interceptor.SaInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tech.qiantong.qdata.module.ds.annotation.handler.DsCheckClientTokenInterceptor;

import javax.annotation.Resource;

/**
 * 注册拦截器
 * @author Ming
 */
@Configuration
public class DsTokenConfigure implements WebMvcConfigurer {

    @Resource
    private DsCheckClientTokenInterceptor checkClientTokenInterceptor;

    /**
     * 注册拦截器，打开注解式鉴权功能
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(checkClientTokenInterceptor)
                // 指定要拦截的路径模式
                .addPathPatterns("/**");
    }
}
