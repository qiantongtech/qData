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

package tech.qiantong.qdata.quality;

import org.dromara.x.file.storage.spring.EnableFileStorage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import tech.qiantong.qdata.common.httpClient.DsRequestUtils;

/**
 * <P>
 * Purpose: Data quality startup class
 * </p>
 *
 * @author: FXB
 * @create: 2025-07-15 15:02
 **/
@EnableFileStorage
@ComponentScan(basePackages = {"tech.qiantong"}, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {DsRequestUtils.class})
})
@ServletComponentScan(basePackages = {"tech.qiantong"})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, SecurityAutoConfiguration.class,
        //Temporarily join. If you encounter an error from Mongo, you can enable it and ignore it.
//        MongoAutoConfiguration.class,
//        MongoDataAutoConfiguration.class
})
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class QualityApplication {
    public static void main(String[] args) {
        SpringApplication.run(QualityApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  Data Quality started successfully   ლ(´ڡ`ლ)ﾞ  \n" +
                "    _            _         _        \n" +
                "   / \\    _ __  (_)__   __(_)  __ _ \n" +
                "  / _ \\  | '_ \\ | |\\ \\ / /| | / _` |\n" +
                " / ___ \\ | | | || | \\ V / | || (_| |\n" +
                "/_/   \\_\\|_| |_||_|  \\_/  |_| \\__,_|");
    }
}
