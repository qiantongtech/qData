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
 * 启动程序
 *
 * @author qdata
 */
@EnableFileStorage
@ComponentScan(basePackages = {"tech.qiantong"})
@ServletComponentScan(basePackages = {"tech.qiantong"})
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@EnableNeo4jRepositories(basePackages = "tech.qiantong.qdata.neo4j.repository")
@EntityScan(basePackages = "tech.qiantong.qdata.neo4j.node")   // 节点/关系实
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
