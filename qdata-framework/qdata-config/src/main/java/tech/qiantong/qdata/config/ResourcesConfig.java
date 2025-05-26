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
 * 通用配置
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
        // 后台页面配置
        registry.addViewController("/index").setViewName("admin/index.html");
        registry.addViewController("/").setViewName("admin/index.html");

        // sso 登录页配置
        registry.addViewController("/sso/index.html").setViewName("sso/index.html");
        registry.addViewController("/sso/confirm.html").setViewName("sso/login.html");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        /** 本地文件上传路径 */
        registry.addResourceHandler(Constants.RESOURCE_PREFIX + "/**")
                .addResourceLocations("file:" + AniviaConfig.getProfile());

        /** 页面静态化Vue2 */
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/dist/admin/static/");

        /** 页面静态化Vue3 */
        registry.addResourceHandler("/assets/**", "/favicon.ico")
                .addResourceLocations("classpath:/dist/admin/assets/")
                .addResourceLocations("classpath:/dist/sso/assets/")
                .addResourceLocations("classpath:/dist/sso/")
        ;

        /** 页面静态化 SSO 认证登录页面 */
//        registry.addResourceHandler("/sso/v1/**").addResourceLocations("classpath:/dist/sso/");
//        registry.addResourceHandler("/sso/v1/assets/**").addResourceLocations("classpath:/dist/sso/assets/");

        /** swagger配置 */
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
                .setCacheControl(CacheControl.maxAge(5, TimeUnit.HOURS).cachePublic());;
    }

    /**
     * 自定义拦截规则
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(repeatSubmitInterceptor).addPathPatterns("/**");
    }

    /**
     * 跨域配置
     */
    @Bean
    public CorsFilter corsFilter()
    {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // 设置访问源地址
        config.addAllowedOriginPattern("*");
        // 设置访问源请求头
        config.addAllowedHeader("*");
        // 设置访问源请求方法
        config.addAllowedMethod("*");
        // 有效期 1800秒
        config.setMaxAge(1800L);
        // 添加映射路径，拦截一切请求
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        // 返回新的CorsFilter
        return new CorsFilter(source);
    }
}
