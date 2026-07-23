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
// * Swagger 2 API configuration
// *
// * @author qdata
// */
//@Configuration
//public class SwaggerConfig
//{
//    /** System base configuration */
//    @Autowired
//    private AniviaConfig qdataConfig;
//
//    /** Whether Swagger is enabled */
//    @Value("${swagger.enabled}")
//    private boolean enabled;
//
//    /** Configures the common request prefix */
//    @Value("${swagger.pathMapping}")
//    private String pathMapping;
//
//    /**
//     * Creates the API documentation configuration.
//     */
//    @Bean
//    public Docket createRestApi()
//    {
//        return new Docket(DocumentationType.OAS_30)
//                // Whether Swagger is enabled
//                .enable(enabled)
//                // Creates the basic API information displayed on the documentation page.
//                .apiInfo(apiInfo())
//                // Configures which APIs are exposed in Swagger.
//                .select()
//                // Scans all annotated APIs for greater flexibility.
//                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
//                // Scans Swagger annotations in the specified package.
//                // .apis(RequestHandlerSelectors.basePackage("tech.qiantong.project.tool.swagger"))
//                // Scans all APIs with .apis(RequestHandlerSelectors.any()).
//                .paths(PathSelectors.any())
//                .build()
//                /* Configures the security scheme so Swagger can send an access token. */
//                .securitySchemes(securitySchemes())
//                .securityContexts(securityContexts())
//                .pathMapping(pathMapping);
//    }
//
//    /**
//     * Security scheme that passes the token through the Authorization request header.
//     */
//    private List<SecurityScheme> securitySchemes()
//    {
//        List<SecurityScheme> apiKeyList = new ArrayList<SecurityScheme>();
//        apiKeyList.add(new ApiKey("Authorization", "Authorization", In.HEADER.toValue()));
//        return apiKeyList;
//    }
//
//    /**
//     * Security context.
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
//     * Default security reference.
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
//     * Adds API metadata.
//     */
//    private ApiInfo apiInfo()
//    {
//        // Customizes API information with ApiInfoBuilder.
//        return new ApiInfoBuilder()
//                // Sets the title.
//                .title("qData Open Source Edition API Documentation")
//                // Description
//                .description("Manages personnel information for companies in the group, including the XXX and XXX modules...")
//                // Author information
//                .contact(new Contact(qdataConfig.getName(), null, null))
//                // Version
//                .version("Version: " + qdataConfig.getVersion())
//                .build();
//    }
//}
