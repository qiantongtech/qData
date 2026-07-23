package tech.qiantong.qdata.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>
 * Purpose: Read Redis related configuration
 * </p>
 *
 * <p>Corresponding to the configuration of ds.redis.* in application.yml</p>
 *
 * @author MING
 * @since 2025-04-28
 */
@Data
@Component
@ConfigurationProperties(prefix = "ds.redis")
public class DsRedisConfig {

    /**
     * Redis service address
     */
    private String host;

    /**
     * Redis service port
     */
    private Integer port;

    /**
     * Database index used by Redis
     */
    private Integer database;

    /**
     * Redis password
     */
    private String password;
}
