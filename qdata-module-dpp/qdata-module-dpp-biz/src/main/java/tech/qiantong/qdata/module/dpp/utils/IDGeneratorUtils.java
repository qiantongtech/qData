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
