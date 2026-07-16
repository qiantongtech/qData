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
public class TaskLogListener {

    @Resource
    private IDppEtlNodeInstanceService dppEtlNodeInstanceService;

    @SneakyThrows
    @RabbitListener(bindings = @QueueBinding(exchange = @Exchange(name = "ds.exchange.taskInstance.log", type = "direct", durable = "true", autoDelete = "false"),
            key = {"ds.queue.taskInstance.log"},
            value = @Queue(value = "ds.queue.taskInstance.log", durable = "true", exclusive = "false", autoDelete = "false")))
    public void taskInstanceLogInsert(Map map, Channel channel, Message message) {
        //Task instance ID
        String taskInstanceId = String.valueOf(map.get("taskInstanceId"));
        //Workflow instance ID
        String processInstanceId = String.valueOf(map.get("workflowInstanceId"));
        //Log
        String logStr = String.valueOf(map.get("log"));
        //Process log
        try {
            dppEtlNodeInstanceService.taskInstanceLogInsert(taskInstanceId, processInstanceId, logStr);
        } catch (Exception e) {
            log.error("任务实例日志插入异常:{}", e.getMessage());
        }

        // Manual acknowledgment
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
