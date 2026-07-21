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
import tech.qiantong.qdata.api.ds.api.etl.ds.ProcessInstance;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlTaskInstanceService;

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
public class ProcessListener {

    @Resource
    private IDppEtlTaskInstanceService dppEtlTaskInstanceService;

//    @SneakyThrows
//    @RabbitListener(bindings = @QueueBinding(exchange = @Exchange(name = "ds.exchange.processInstance", type = "direct", durable = "true", autoDelete = "false"),
//            key = {"ds.queue.processInstance.insert"},
//            value = @Queue(value = "ds.queue.processInstance.insert", durable = "true", exclusive = "false", autoDelete = "false")))
//    public void processInstanceInsert(Map map, Channel channel, Message message) {
//        log.error("Process instance creation message start>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//        ProcessInstance processInstance = JSON.parseObject(JSON.toJSONString(map), ProcessInstance.class);
//        try {
//            dppEtlTaskInstanceService.createTaskInstance(processInstance);
//        } catch (ServiceException serviceException) {
//            log.error("Create process instance exception:{}", serviceException.getMessage());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        // Manual acknowledgment
//        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//        log.info("Process instance creation message end>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//    }


    @SneakyThrows
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(name = "ds.exchange.processInstance", type = "direct", durable = "true", autoDelete = "false"),
            key = {"ds.queue.processInstance"},
            value = @Queue(value = "ds.queue.processInstance", durable = "true", exclusive = "false", autoDelete = "false")))
    public void processInstanceUpdate(Map map, Channel channel, Message message) {
        log.error("Starting process instance create-or-update>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        Integer type = (Integer) map.get("type");
        ProcessInstance processInstance = JSON.parseObject(JSON.toJSONString(map.get("instance")), ProcessInstance.class);
        Boolean flag = false;
        try {
            if (type == 1) {
                flag = dppEtlTaskInstanceService.createTaskInstance(processInstance);
            } else {
                flag = dppEtlTaskInstanceService.updateTaskInstance(processInstance);
            }
        } catch (ServiceException serviceException) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            log.error("Failed to create or update task instance: {}", serviceException.getMessage());
        } catch (Exception e) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            e.printStackTrace();
            return;
        }
        if (flag) {
            // Manual acknowledgment
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
        log.info(processInstance.getId() + "Process instance create-or-update completed>>>>>>>>>>>>>>>>>>>>>>>>>>>" + flag);
    }
}
