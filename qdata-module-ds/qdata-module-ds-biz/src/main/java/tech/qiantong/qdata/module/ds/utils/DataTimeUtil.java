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

package tech.qiantong.qdata.module.ds.utils;


import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 时间工具类
 * 2024-01-05
 */
@Slf4j
public class DataTimeUtil {


    /**
     * LocalDateTime格式对象转换成时间戳
     * @param now
     * @return
     */
    public static long timeByTimeStamp(LocalDateTime now) {
        try {
            // 转换为Instant对象
            Instant instant = now.atZone(ZoneId.systemDefault()).toInstant();
            // 从Instant对象获取时间戳（毫秒）
            return instant.toEpochMilli();
        }catch (Exception e){
            log.debug("LocalDateTime转换时间戳失败 now{}",now);
            log.debug("LocalDateTime转换时间戳失败 e{}",e.toString());
            return System.currentTimeMillis();
        }
    }
}
