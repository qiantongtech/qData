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

package tech.qiantong.qdata.ai.server.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.TimeUnit;

/**
 * Embedded Redis instance configuration to solve the problem of local lag
 * For local development and testing only, please note!!!!
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

                // Print start information
                log.info("-------------------------------------------------");
                log.info("| 注意: 仅供测试使用，生产环境误用！！！           |");
                log.info("| 注意: 本地嵌入式 Redis Server 正在启动...         |");
                log.info("-------------------------------------------------");

                // Waiting animation before starting the Redis server
                String[] frames = new String[]{"-", "\\", "|", "/"};
                for (int i = 0; i < 12; i++) {
                    for (String frame : frames) {
                        System.out.print("\r" + frame + " 启动中... 仅供开发和测试使用，请勿用于生产环境！");
                        System.out.flush();
                        TimeUnit.MILLISECONDS.sleep(50);
                    }
                }

                // Actually start the Redis server
                redisServer.start();

                // Clear the current line and print a final success message
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
     * Check whether the specified port is available
     *
     * @param port The port number to check
     * @return Returns true if the port is not occupied, otherwise returns false
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
