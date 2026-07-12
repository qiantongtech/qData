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

package tech.qiantong.qdata.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tech.qiantong.qdata.common.utils.MessageUtils;

/**
 * <P>
 * Purpose:
 * </p>
 *
 * @author: FXB
 * @create: 2025-02-24 14:23
 **/
@Slf4j
@Configuration
public class RabbitConfig {
    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        // Whether the message was successfully sent to the Exchange
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                log.info(MessageUtils.messageEn("log.rabbit.send.success"));
            } else {
                log.info(MessageUtils.messageEn("log.rabbit.send.fail"), correlationData, cause);
            }
        });
        // mandatory=true is required to trigger the return callback; otherwise the message is discarded if no matching Queue is found on the Exchange
        rabbitTemplate.setMandatory(true);
        // Whether the message was routed from Exchange to Queue; note: this is a failure callback, only invoked when routing from Exchange to Queue fails
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            log.info(MessageUtils.messageEn("log.rabbit.route.fail"), exchange, routingKey, replyCode, replyText, message);
        });
        return rabbitTemplate;
    }


    /**
     * {@link org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration}
     *  Auto-detected
     * @param objectMapper JSON serialization implementation class
     * @return MQ message serialization tool
     */
    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
