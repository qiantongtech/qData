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

package tech.qiantong.qdata.module.ds.config.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import tech.qiantong.qdata.module.ds.handler.MappingHandlerMapping;
import tech.qiantong.qdata.module.ds.handler.RequestHandler;
import tech.qiantong.qdata.module.ds.handler.RequestInterceptor;
import tech.qiantong.qdata.module.ds.service.api.impl.ApiMappingEngine;

@Configuration
public class ApiMappingConfig {

    @Bean
    public MappingHandlerMapping mappingHandlerMapping(RequestMappingHandlerMapping requestMappingHandlerMapping,
                                                       ApiMappingEngine apiMappingEngine,
                                                       RedisTemplate redisTemplate,
                                                       ObjectMapper objectMapper) {
        MappingHandlerMapping mappingHandlerMapping = new MappingHandlerMapping();
        mappingHandlerMapping.setHandler(requestHandler(apiMappingEngine, redisTemplate, objectMapper));
        mappingHandlerMapping.setRequestMappingHandlerMapping(requestMappingHandlerMapping);
        return mappingHandlerMapping;
    }

    @Bean
    public RequestHandler requestHandler(ApiMappingEngine apiMappingEngine, RedisTemplate redisTemplate, ObjectMapper objectMapper) {
        RequestHandler handler = new RequestHandler();
        handler.setApiMappingEngine(apiMappingEngine);
        handler.setObjectMapper(objectMapper);
        handler.setRequestInterceptor(new RequestInterceptor(redisTemplate));
        return handler;
    }
}
