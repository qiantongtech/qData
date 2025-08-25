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
 * 用途:数据质量启动类
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
        //临时加入，如遇到Mongo报错可以开启进行忽略
//        MongoAutoConfiguration.class,
//        MongoDataAutoConfiguration.class
})
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class QualityApplication {
    public static void main(String[] args) {
        SpringApplication.run(QualityApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  数据质量启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                "    _            _         _        \n" +
                "   / \\    _ __  (_)__   __(_)  __ _ \n" +
                "  / _ \\  | '_ \\ | |\\ \\ / /| | / _` |\n" +
                " / ___ \\ | | | || | \\ V / | || (_| |\n" +
                "/_/   \\_\\|_| |_||_|  \\_/  |_| \\__,_|");
    }
}
