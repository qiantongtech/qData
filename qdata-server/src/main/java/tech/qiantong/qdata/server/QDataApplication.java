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
 * For brand customization, please contact the official team for authorization.
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
 * 如需定制品牌，请通过官方渠道申请品牌授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.server;

import org.dromara.x.file.storage.spring.EnableFileStorage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 启动程序
 *
 * @author qdata
 */
@EnableFileStorage
@ComponentScan(basePackages = {"tech.qiantong"})
@ServletComponentScan(basePackages = {"tech.qiantong"})
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
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
