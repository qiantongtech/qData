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

package tech.qiantong.qdata.spark.etl.utils;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeoutException;

/**
 * RabbitMQ publisher used by the standalone ETL process.
 *
 * A Spark task emits many status and log messages. Reusing one confirmed
 * channel avoids a TCP/AMQP handshake for every log line and, more
 * importantly, makes a failed callback visible to DolphinScheduler instead
 * of silently reporting success.
 */
@Slf4j
public final class RabbitmqUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final long PUBLISH_CONFIRM_TIMEOUT_MS = 5000L;

    private static Connection connection;
    private static Channel channel;
    private static String connectionKey;
    private static final Set<String> declaredRoutes = new HashSet<>();

    private RabbitmqUtils() {
    }

    public static synchronized Boolean convertAndSend(JSONObject config, String exchange,
                                                       String routingKey, Object object) {
        if (config == null || config.isEmpty()) {
            throw new IllegalArgumentException("RabbitMQ configuration must not be empty");
        }

        try {
            ensureChannel(config);
            ensureRoute(exchange, routingKey);
            byte[] body = OBJECT_MAPPER.writeValueAsBytes(object);
            AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
                    .contentType("application/json")
                    .contentEncoding("utf-8")
                    .deliveryMode(2)
                    .build();

            channel.basicPublish(exchange, routingKey, true, props, body);
            channel.waitForConfirmsOrDie(PUBLISH_CONFIRM_TIMEOUT_MS);
            return true;
        } catch (Exception e) {
            closeInternal();
            throw new IllegalStateException(
                    "RabbitMQ callback failed, exchange=" + exchange + ", routingKey=" + routingKey, e);
        }
    }

    public static synchronized void close() {
        closeInternal();
    }

    private static void ensureChannel(JSONObject config) throws IOException, TimeoutException {
        String expectedKey = buildConnectionKey(config);
        if (!expectedKey.equals(connectionKey)) {
            closeInternal();
        }
        if (connection == null || !connection.isOpen()) {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(config.getString("host"));
            factory.setPort(config.getIntValue("port"));
            factory.setUsername(config.getString("username"));
            factory.setPassword(config.getString("password"));
            factory.setConnectionTimeout(config.getIntValue("connectionTimeoutMs", 5000));
            factory.setHandshakeTimeout(config.getIntValue("handshakeTimeoutMs", 5000));
            factory.setAutomaticRecoveryEnabled(true);
            connection = factory.newConnection("qdata-etl-callback");
            connectionKey = expectedKey;
        }
        if (channel == null || !channel.isOpen()) {
            channel = connection.createChannel();
            channel.confirmSelect();
        }
    }

    private static String buildConnectionKey(JSONObject config) {
        return config.getString("host") + ':' + config.getIntValue("port") + ':'
                + config.getString("username");
    }

    private static void ensureRoute(String exchange, String routingKey) throws IOException {
        String route = exchange + '|' + routingKey;
        if (declaredRoutes.add(route)) {
            channel.exchangeDeclare(exchange, "direct", true);
            channel.queueDeclare(routingKey, true, false, false, null);
            channel.queueBind(routingKey, exchange, routingKey);
        }
    }

    private static void closeInternal() {
        if (channel != null) {
            try {
                channel.close();
            } catch (Exception e) {
                log.debug("Failed to close RabbitMQ channel", e);
            }
        }
        channel = null;

        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                log.debug("Failed to close RabbitMQ connection", e);
            }
        }
        connection = null;
        connectionKey = null;
        declaredRoutes.clear();
    }
}
