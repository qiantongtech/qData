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

package tech.qiantong.qdata.module.dpp.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * <P>
 * 用途:生成数字id
 * </p>
 *
 * @author: FXB
 * @create: 2019-12-26 08:59
 **/
@Component
public class IDGeneratorUtils {

    private static long workerId;

    private static long datacenterId;

    @Value("${id.workerId}")
    public  void setWorkerId(long workerId) {
        IDGeneratorUtils.workerId = workerId;
    }

    @Value("${id.datacenterId}")
    public  void setDatacenterId(long datacenterId) {
        IDGeneratorUtils.datacenterId = datacenterId;
    }

    public static long getLongId(){
        final SnowflakeIdHelper snowflakeIdHelper = SnowflakeIdHelper.getInstance(workerId,datacenterId);
        long id = snowflakeIdHelper.nextId();
        return id;
    }
    public static String getStringId(){
        final SnowflakeIdHelper snowflakeIdHelper = SnowflakeIdHelper.getInstance(workerId,datacenterId);
        long id = snowflakeIdHelper.nextId();
        return String.valueOf(id);
    }
}
