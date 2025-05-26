package tech.qiantong.qdata.module.ds.handler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 ** 确保动态注册的请求映射优先于 @RequestMapping("services/**")
 */
@Configuration
public class WebConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public RequestMappingHandlerMapping customRequestMappingHandlerMapping() {
        return new RequestMappingHandlerMapping();
    }
}
