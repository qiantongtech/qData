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
