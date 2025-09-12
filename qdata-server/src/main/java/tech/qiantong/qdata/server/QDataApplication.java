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
