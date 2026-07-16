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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * <P>
 * Purpose: rabbitmq message middleware tool class
 * </p>
 *
 * @author: FXB
 * @create: 2025-04-28 15:48
 **/
public class RabbitmqUtils {
    public static Boolean convertAndSend(JSONObject config, String exchange, String routingKey, Object object) {
        // Create connection factory
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(config.getString("host"));
        factory.setPort(config.getIntValue("port"));
        factory.setUsername(config.getString("username"));
        factory.setPassword(config.getString("password"));

        Connection connection = null;
        Channel channel = null;
        try {
            // Establish connections and channels
            connection = factory.newConnection();
            channel = connection.createChannel();
            // Declare the queue (create it if it does not exist)
            channel.queueDeclare(routingKey, true, false, false, null);

            // Serialize to JSON using Jackson
            ObjectMapper objectMapper = new ObjectMapper();
            byte[] body = objectMapper.writeValueAsBytes(object);

            // Set message properties (JSON format)
            AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
                    .contentType("application/json")
                    .contentEncoding("utf-8")
                    .build();

            // Send message
            channel.basicPublish(
                    exchange,         // Use default switch (direct swap)
                    routingKey, // Routing key (queue name is used directly here)
                    props,      // Message properties
                    body // Convert message body to byte array
            );
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (channel != null && channel.isOpen()) {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null && connection.isOpen()) {
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
}
