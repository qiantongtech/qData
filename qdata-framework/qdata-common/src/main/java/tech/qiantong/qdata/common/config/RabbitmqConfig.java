package tech.qiantong.qdata.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <P>
 * 用途:读取rabbitmq相关配置
 * </p>
 *
 * @author: FXB
 * @create: 2025-04-28 16:00
 **/
@Data
@Component
@ConfigurationProperties(prefix = "spring.rabbitmq")
public class RabbitmqConfig {
    /**
     * 项目名称
     */
    private String host;

    /**
     * 版本
     */
    private Integer port;

    /**
     * 项目名称
     */
    private String username;

    /**
     * 项目名称
     */
    private String password;
}
