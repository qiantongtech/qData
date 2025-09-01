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
 * 用途:rabbitmq消息中间件工具类
 * </p>
 *
 * @author: FXB
 * @create: 2025-04-28 15:48
 **/
public class RabbitmqUtils {
    public static Boolean convertAndSend(JSONObject config, String exchange, String routingKey, Object object) {
        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(config.getString("host"));
        factory.setPort(config.getIntValue("port"));
        factory.setUsername(config.getString("username"));
        factory.setPassword(config.getString("password"));

        Connection connection = null;
        Channel channel = null;
        try {
            // 建立连接和通道
            connection = factory.newConnection();
            channel = connection.createChannel();
            // 声明队列（如果不存在则创建）
            channel.queueDeclare(routingKey, true, false, false, null);

            // 使用 Jackson 序列化为 JSON
            ObjectMapper objectMapper = new ObjectMapper();
            byte[] body = objectMapper.writeValueAsBytes(object);

            // 设置消息属性（JSON 格式）
            AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
                    .contentType("application/json")
                    .contentEncoding("utf-8")
                    .build();

            // 发送消息
            channel.basicPublish(
                    exchange,         // 使用默认交换机（直接交换）
                    routingKey, // 路由键（这里直接用队列名称）
                    props,      // 消息属性
                    body // 消息体转换为字节数组
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
