package tech.qiantong.qdata.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 用途: 读取 Redis 相关配置
 * </p>
 *
 * <p>对应 application.yml 中 ds.redis.* 的配置</p>
 *
 * @author MING
 * @since 2025-04-28
 */
@Data
@Component
@ConfigurationProperties(prefix = "ds.redis")
public class DsRedisConfig {

    /**
     * Redis 服务地址
     */
    private String host;

    /**
     * Redis 服务端口
     */
    private Integer port;

    /**
     * Redis 使用的数据库索引
     */
    private Integer database;

    /**
     * Redis 密码
     */
    private String password;
}
