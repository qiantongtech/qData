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

package tech.qiantong.qdata.server;

import org.dromara.x.file.storage.spring.EnableFileStorage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

/**
 * Start the program
 *
 * @author qdata
 */
@EnableFileStorage
@ComponentScan(basePackages = {"tech.qiantong"})
@ServletComponentScan(basePackages = {"tech.qiantong"})
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@EnableNeo4jRepositories(basePackages = "tech.qiantong.qdata.neo4j.repository")
@EntityScan(basePackages = "tech.qiantong.qdata.neo4j.node")   // Node/relationship
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class QDataApplication
{
    public static final String BRAND_BLUE = "\u001B[38;2;29;80;163m";
    public static final String RESET = "\u001B[0m";

    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(QDataApplication.class, args);

        System.out.println(
                BRAND_BLUE +
                        "     (♥◠‿◠)ﾉﾞ  qData 千数平台启动成功！  ლ(´ڡ`ლ)ﾞ\n" +
                        "═════════════════════════════════════════════════════\n" +
                        "           ____            _            \n" +
                        "    __ _  |  _ \\    __ _  | |_    __ _  \n" +
                        "   / _` | | | | |  / _` | | __|  / _` | \n" +
                        "  | (_| | | |_| | | (_| | | |_  | (_| | \n" +
                        "   \\__, | |____/   \\__,_|  \\__|  \\__,_| \n" +
                        "      |_|                                \n" +
                        "═════════════════════════════════════════════════════\n" +
                        "     国 产 环 境  ·  稳 定  ·  高 效  ·  安 全" +
                        RESET
        );
    }
}
