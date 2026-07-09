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

package tech.qiantong.qdata.module.dpp.listener;

import com.alibaba.fastjson2.JSON;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tech.qiantong.qdata.api.ds.api.etl.ds.TaskInstance;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlNodeInstanceService;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <P>
 * Purpose:
 * </p>
 *
 * @author: FXB
 * @create: 2025-02-24 14:26
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class TaskListener {

    @Resource
    private IDppEtlNodeInstanceService dppEtlNodeInstanceService;

    @SneakyThrows
    @RabbitListener(bindings = @QueueBinding(exchange = @Exchange(name = "ds.exchange.taskInstance", type = "direct", durable = "true", autoDelete = "false"),
            key = {"ds.queue.taskInstance.insert"},
            value = @Queue(value = "ds.queue.taskInstance.insert", durable = "true", exclusive = "false", autoDelete = "false")))
    public void taskInstanceInsert(Map map, Channel channel, Message message) {
        log.info("Task instance creation message started >>>>>>>>>>>>>>>>>>>>>>>>>>>");
        TaskInstance taskInstance = JSON.parseObject(JSON.toJSONString(map), TaskInstance.class);
        try {
            dppEtlNodeInstanceService.createNodeInstance(taskInstance);
        } catch (ServiceException serviceException) {
            log.error("Failed to create task instance: {}", serviceException.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Manual acknowledgment
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        log.info("Task instance creation message ended >>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }


    @SneakyThrows
    @RabbitListener(bindings = @QueueBinding(exchange = @Exchange(name = "ds.exchange.taskInstance", type = "direct", durable = "true", autoDelete = "false"),
            key = {"ds.queue.taskInstance.update"},
            value = @Queue(value = "ds.queue.taskInstance.update", durable = "true", exclusive = "false", autoDelete = "false")))
    public void taskInstanceUpdate(Map map, Channel channel, Message message) {
        log.info("Task instance update message started >>>>>>>>>>>>>>>>>>>>>>>>>>>");
        TaskInstance taskInstance = JSON.parseObject(JSON.toJSONString(map), TaskInstance.class);
        Boolean flag = false;
        try {
            flag = dppEtlNodeInstanceService.updateNodeInstance(taskInstance);
        } catch (ServiceException serviceException) {
            log.error("Failed to update task instance: {}", serviceException.getMessage());
        } catch (Exception e) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            e.printStackTrace();
            return;
        }
        if (flag) {
            // Manual acknowledgment
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
        log.info(taskInstance.getId() + " Task instance update message ended >>>>>>>>>>>>>>>>>>>>>>>>>>>" + flag);
    }
}
