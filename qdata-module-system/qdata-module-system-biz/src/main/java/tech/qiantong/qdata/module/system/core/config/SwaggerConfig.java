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

//package tech.qiantong.module.system.core.config;
//
//import java.util.ArrayList;
//import java.util.List;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import tech.qiantong.qdata.config.common.AniviaConfig;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.models.auth.In;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.ApiKey;
//import springfox.documentation.service.AuthorizationScope;
//import springfox.documentation.service.Contact;
//import springfox.documentation.service.SecurityReference;
//import springfox.documentation.service.SecurityScheme;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spi.service.contexts.SecurityContext;
//import springfox.documentation.spring.web.plugins.Docket;
//
///**
// * Swagger2的接口配置
// *
// * @author qdata
// */
//@Configuration
//public class SwaggerConfig
//{
//    /** 系统基础配置 */
//    @Autowired
//    private AniviaConfig qdataConfig;
//
//    /** 是否开启swagger */
//    @Value("${swagger.enabled}")
//    private boolean enabled;
//
//    /** 设置请求的统一前缀 */
//    @Value("${swagger.pathMapping}")
//    private String pathMapping;
//
//    /**
//     * 创建API
//     */
//    @Bean
//    public Docket createRestApi()
//    {
//        return new Docket(DocumentationType.OAS_30)
//                // 是否启用Swagger
//                .enable(enabled)
//                // 用来创建该API的基本信息，展示在文档的页面中（自定义展示的信息）
//                .apiInfo(apiInfo())
//                // 设置哪些接口暴露给Swagger展示
//                .select()
//                // 扫描所有有注解的api，用这种方式更灵活
//                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
//                // 扫描指定包中的swagger注解
//                // .apis(RequestHandlerSelectors.basePackage("tech.qiantong.project.tool.swagger"))
//                // 扫描所有 .apis(RequestHandlerSelectors.any())
//                .paths(PathSelectors.any())
//                .build()
//                /* 设置安全模式，swagger可以设置访问token */
//                .securitySchemes(securitySchemes())
//                .securityContexts(securityContexts())
//                .pathMapping(pathMapping);
//    }
//
//    /**
//     * 安全模式，这里指定token通过Authorization头请求头传递
//     */
//    private List<SecurityScheme> securitySchemes()
//    {
//        List<SecurityScheme> apiKeyList = new ArrayList<SecurityScheme>();
//        apiKeyList.add(new ApiKey("Authorization", "Authorization", In.HEADER.toValue()));
//        return apiKeyList;
//    }
//
//    /**
//     * 安全上下文
//     */
//    private List<SecurityContext> securityContexts()
//    {
//        List<SecurityContext> securityContexts = new ArrayList<>();
//        securityContexts.add(
//                SecurityContext.builder()
//                        .securityReferences(defaultAuth())
//                        .operationSelector(o -> o.requestMappingPattern().matches("/.*"))
//                        .build());
//        return securityContexts;
//    }
//
//    /**
//     * 默认的安全上引用
//     */
//    private List<SecurityReference> defaultAuth()
//    {
//        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
//        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
//        authorizationScopes[0] = authorizationScope;
//        List<SecurityReference> securityReferences = new ArrayList<>();
//        securityReferences.add(new SecurityReference("Authorization", authorizationScopes));
//        return securityReferences;
//    }
//
//    /**
//     * 添加摘要信息
//     */
//    private ApiInfo apiInfo()
//    {
//        // 用ApiInfoBuilder进行定制
//        return new ApiInfoBuilder()
//                // 设置标题
//                .title("标题：冰凤框架后台管理系统_接口文档")
//                // 描述
//                .description("描述：用于管理集团旗下公司的人员信息,具体包括XXX,XXX模块...")
//                // 作者信息
//                .contact(new Contact(qdataConfig.getName(), null, null))
//                // 版本
//                .version("版本号:" + qdataConfig.getVersion())
//                .build();
//    }
//}
