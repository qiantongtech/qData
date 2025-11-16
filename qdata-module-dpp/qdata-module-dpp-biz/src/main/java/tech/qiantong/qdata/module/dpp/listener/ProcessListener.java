/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
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
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlTaskInstanceService;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <P>
 * 用途:
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
//        log.error("流程实例创建消息开始>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//        ProcessInstance processInstance = JSON.parseObject(JSON.toJSONString(map), ProcessInstance.class);
//        try {
//            dppEtlTaskInstanceService.createTaskInstance(processInstance);
//        } catch (ServiceException serviceException) {
//            log.error("创建流程实例异常:{}", serviceException.getMessage());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        // 手动确认
//        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//        log.info("流程实例创建消息结束>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//    }


    @SneakyThrows
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(name = "ds.exchange.processInstance", type = "direct", durable = "true", autoDelete = "false"),
            key = {"ds.queue.processInstance"},
            value = @Queue(value = "ds.queue.processInstance", durable = "true", exclusive = "false", autoDelete = "false")))
    public void processInstanceUpdate(Map map, Channel channel, Message message) {
        log.error("流程实例创建更新消息开始>>>>>>>>>>>>>>>>>>>>>>>>>>>");
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
            log.error("创建更新任务实例异常:{}", serviceException.getMessage());
        } catch (Exception e) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            e.printStackTrace();
            return;
        }
        if (flag) {
            // 手动确认
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
        log.info(processInstance.getId() + "流程实例创建更新消息结束>>>>>>>>>>>>>>>>>>>>>>>>>>>" + flag);
    }
}
