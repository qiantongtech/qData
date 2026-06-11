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
 */

package tech.qiantong.qdata.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <P>
 * 用途:读取rabbitmq相关配置
 * </p>
 *
 * @author: FXB
 * @create: 2025-04-28 16:00
 **/
@Data
@Component
@ConfigurationProperties(prefix = "spring.rabbitmq")
public class RabbitmqConfig {
    /**
     * 项目名称
     */
    private String host;

    /**
     * 版本
     */
    private Integer port;

    /**
     * 项目名称
     */
    private String username;

    /**
     * 项目名称
     */
    private String password;
}
