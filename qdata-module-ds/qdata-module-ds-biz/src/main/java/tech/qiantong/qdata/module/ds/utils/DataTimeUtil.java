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
