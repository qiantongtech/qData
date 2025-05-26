package tech.qiantong.qdata.server.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.TimeUnit;

/**
 * 嵌入式 Redis实例配置，解决本地卡顿的问题
 * 仅供本地开发测试使用，注意！！！！
 * @author Ming
 */
@Configuration
@Slf4j
public class EmbeddedRedisConfig {

    private RedisServer redisServer;

    @Value("${spring.profiles.active}")
    private String active;

    @PostConstruct
    public void startRedis() throws IOException, InterruptedException {
        if ("dev".equals(active)) {
            int redisPort = 12138;
            if (isPortAvailable(redisPort)) {
                redisServer = new RedisServer(redisPort);

                // 打印开始信息
                log.info("-------------------------------------------------");
                log.info("| 注意: 仅供测试使用，生产环境误用！！！           |");
                log.info("| 注意: 本地嵌入式 Redis Server 正在启动...         |");
                log.info("-------------------------------------------------");

                // 启动Redis服务器前的等待动画
                String[] frames = new String[]{"-", "\\", "|", "/"};
                for (int i = 0; i < 12; i++) {
                    for (String frame : frames) {
                        System.out.print("\r" + frame + " 启动中... 仅供开发和测试使用，请勿用于生产环境！");
                        System.out.flush();
                        TimeUnit.MILLISECONDS.sleep(50);
                    }
                }

                // 实际启动Redis服务器
                redisServer.start();

                // 清除当前行并打印最终成功消息
                System.out.print("\r✓ 本地嵌入式 Redis Server 已成功启动于端口: " + redisServer.ports());
                System.out.println();
                log.info("-------------------------------------------------");
                log.info("| 成功: 本地嵌入式 Redis Server 已经启动完成。      |");
                log.info("| 端口: {} ", redisServer.ports());
                log.info("-------------------------------------------------");
            } else {
                log.warn("Redis 服务器端口 {} 已在使用中。跳过 Redis 启动。", redisPort);
            }
        }
    }

    /**
     * 检查指定端口是否可用
     *
     * @param port 要检查的端口号
     * @return 如果端口未被占用返回true，否则返回false
     */
    private boolean isPortAvailable(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @PreDestroy
    public void stopRedis() throws IOException {
        if (redisServer != null) {
            redisServer.stop();
        }
    }

    @Bean
    public RedisServer redisServer() {
        return redisServer;
    }
}
