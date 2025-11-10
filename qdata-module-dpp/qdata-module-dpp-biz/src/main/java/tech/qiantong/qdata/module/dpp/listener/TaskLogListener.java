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
 * For brand customization, please contact the official team for authorization.
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
 * 如需定制品牌，请通过官方渠道申请品牌授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
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
 * 用途:
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
        //任务实例id
        String taskInstanceId = String.valueOf(map.get("taskInstanceId"));
        //工作流实例id
        String processInstanceId = String.valueOf(map.get("workflowInstanceId"));
        //日志
        String logStr = String.valueOf(map.get("log"));
        //处理日志
        try {
            dppEtlNodeInstanceService.taskInstanceLogInsert(taskInstanceId, processInstanceId, logStr);
        } catch (Exception e) {
            log.error("任务实例日志插入异常:{}", e.getMessage());
        }

        // 手动确认
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
