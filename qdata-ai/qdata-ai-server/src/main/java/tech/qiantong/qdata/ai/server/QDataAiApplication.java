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

package tech.qiantong.qdata.ai.server;

import org.dromara.x.file.storage.spring.EnableFileStorage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import tech.qiantong.qdata.common.httpClient.DsRequestUtils;
import tech.qiantong.qdata.config.FilterConfig;
import tech.qiantong.qdata.config.RabbitConfig;
import tech.qiantong.qdata.module.system.controller.admin.auth.AuthController;
import tech.qiantong.qdata.module.system.controller.admin.common.CommonController;
import tech.qiantong.qdata.module.system.controller.admin.example.websocket.WebSocketServer;
import tech.qiantong.qdata.security.config.SecurityConfig;
import tech.qiantong.qdata.security.filter.JwtAuthenticationTokenFilter;

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
                        SecurityConfig.class,
                        JwtAuthenticationTokenFilter.class
                }),
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "tech\\.qiantong\\.qdata\\.module\\.system\\.controller\\.admin\\..*")
        })
public class QDataAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(QDataAiApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  数据中台-chat-bi启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                "    _            _         _        \n" +
                "   / \\    _ __  (_)__   __(_)  __ _ \n" +
                "  / _ \\  | '_ \\ | |\\ \\ / /| | / _` |\n" +
                " / ___ \\ | | | || | \\ V / | || (_| |\n" +
                "/_/   \\_\\|_| |_||_|  \\_/  |_| \\__,_|");
    }

}
