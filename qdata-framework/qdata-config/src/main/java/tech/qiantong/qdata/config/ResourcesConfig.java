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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.CacheControl;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tech.qiantong.qdata.common.config.AniviaConfig;
import tech.qiantong.qdata.common.constant.Constants;
import tech.qiantong.qdata.config.interceptor.RepeatSubmitInterceptor;

import java.util.concurrent.TimeUnit;

/**
 * Common configuration
 *
 * @author qdata
 */
@Configuration
public class ResourcesConfig implements WebMvcConfigurer
{
    @Autowired
    private RepeatSubmitInterceptor repeatSubmitInterceptor;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Backend page configuration
        registry.addViewController("/index").setViewName("admin/index.html");
        registry.addViewController("/").setViewName("admin/index.html");

        // sso login page configuration
        registry.addViewController("/sso/index.html").setViewName("sso/index.html");
        registry.addViewController("/sso/confirm.html").setViewName("sso/login.html");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        /** Local file upload path */
        registry.addResourceHandler(Constants.RESOURCE_PREFIX + "/**")
                .addResourceLocations("file:" + AniviaConfig.getProfile());

        /** Page static Vue2 */
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/dist/admin/static/");

        /** Page static Vue3 */
        registry.addResourceHandler("/assets/**", "/favicon.ico")
                .addResourceLocations("classpath:/dist/admin/assets/")
                .addResourceLocations("classpath:/dist/sso/assets/")
                .addResourceLocations("classpath:/dist/sso/")
        ;

        /** Page static SSO authentication login page */
//        registry.addResourceHandler("/sso/v1/**").addResourceLocations("classpath:/dist/sso/");
//        registry.addResourceHandler("/sso/v1/assets/**").addResourceLocations("classpath:/dist/sso/assets/");

        /** swagger configuration */
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
                .setCacheControl(CacheControl.maxAge(5, TimeUnit.HOURS).cachePublic());;
    }

    /**
     * Custom interception rules
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(repeatSubmitInterceptor).addPathPatterns("/**");
    }

    /**
     * Cross-domain configuration
     */
    @Bean
    public CorsFilter corsFilter()
    {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // Set access source address
        config.addAllowedOriginPattern("*");
        // Set access source request header
        config.addAllowedHeader("*");
        // Set access source request method
        config.addAllowedMethod("*");
        // Validity period 1800 seconds
        config.setMaxAge(1800L);
        // Add a mapping path to intercept all requests
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        // Return new CorsFilter
        return new CorsFilter(source);
    }
}
