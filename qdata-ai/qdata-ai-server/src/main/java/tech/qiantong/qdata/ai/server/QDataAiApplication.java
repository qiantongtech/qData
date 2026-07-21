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

package tech.qiantong.qdata.ai.server;

import org.dromara.x.file.storage.spring.EnableFileStorage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import tech.qiantong.qdata.common.httpClient.DsRequestUtils;
import tech.qiantong.qdata.config.FilterConfig;
import tech.qiantong.qdata.config.I18nConfig;
import tech.qiantong.qdata.config.RabbitConfig;
import tech.qiantong.qdata.module.system.controller.admin.auth.AuthController;
import tech.qiantong.qdata.module.system.controller.admin.common.CommonController;
import tech.qiantong.qdata.module.system.controller.admin.example.websocket.WebSocketServer;
import tech.qiantong.qdata.security.config.SecurityConfig;
import tech.qiantong.qdata.security.filter.JwtAuthenticationTokenFilter;

@EnableNeo4jRepositories(basePackages = "tech.qiantong.qdata.neo4j.repository")
@EntityScan(basePackages = "tech.qiantong.qdata.neo4j.node")   // Node/relationship
@EnableFileStorage
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"tech.qiantong"},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {FilterConfig.class,
                        DsRequestUtils.class,
                        AuthController.class,
                        CommonController.class,
                        WebSocketServer.class,
                        RabbitConfig.class,
                        I18nConfig.class,
                        SecurityConfig.class,
                        JwtAuthenticationTokenFilter.class
                }),
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "tech\\.qiantong\\.qdata\\.module\\.system\\.controller\\.admin\\..*")
        })
public class QDataAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(QDataAiApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  Data Platform Chat BI started successfully   ლ(´ڡ`ლ)ﾞ  \n" +
                "    _            _         _        \n" +
                "   / \\    _ __  (_)__   __(_)  __ _ \n" +
                "  / _ \\  | '_ \\ | |\\ \\ / /| | / _` |\n" +
                " / ___ \\ | | | || | \\ V / | || (_| |\n" +
                "/_/   \\_\\|_| |_||_|  \\_/  |_| \\__,_|");
    }

}
