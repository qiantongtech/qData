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

/**
 * Copyright 2010-2012 Twitter, Inc.
 */

package tech.qiantong.qdata.spark.etl.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

/**
 * Rewriting based on Twitter snowflake algorithm
 */
@Slf4j
public class CodeGenerateUtils {

    private static final CodeGenerator codeGenerator;

    public static int getProcessID() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        return Integer.parseInt(runtimeMXBean.getName().split("@")[0]);
    }

    static {
        try {
            codeGenerator = new CodeGenerator(InetAddress.getLocalHost().getHostName() + "-" + getProcessID());
        } catch (UnknownHostException e) {
            throw new CodeGenerateException(e.getMessage());
        }
    }

    public static long genCode() throws CodeGenerateException {
        return codeGenerator.genCode();
    }

    public static class CodeGenerator {

        // start timestamp
        private static final long START_TIMESTAMP = 1609430400000L; // 2021-01-01 00:00:00
        // Each machine generates 32 in the same millisecond
        private static final long LOW_DIGIT_BIT = 5L;
        private static final long MACHINE_BIT = 5L;
        private static final long MAX_LOW_DIGIT = ~(-1L << LOW_DIGIT_BIT);
        // The displacement to the left
        private static final long HIGH_DIGIT_LEFT = LOW_DIGIT_BIT + MACHINE_BIT;
        public final long machineHash;
        private long lowDigit = 0L;
        private long recordMillisecond = -1L;

        private static final long SYSTEM_TIMESTAMP = System.currentTimeMillis();
        private static final long SYSTEM_NANOTIME = System.nanoTime();

        public CodeGenerator(String appName) {
            this.machineHash = Math.abs(Objects.hash(appName)) % (1 << MACHINE_BIT);
        }

        public synchronized long genCode() throws CodeGenerateException {
            long nowtMillisecond = systemMillisecond();
            if (nowtMillisecond < recordMillisecond) {
                throw new CodeGenerateException("New code exception because time is set back.");
            }
            if (nowtMillisecond == recordMillisecond) {
                lowDigit = (lowDigit + 1) & MAX_LOW_DIGIT;
                if (lowDigit == 0L) {
                    while (nowtMillisecond <= recordMillisecond) {
                        nowtMillisecond = systemMillisecond();
                    }
                }
            } else {
                lowDigit = 0L;
            }
            recordMillisecond = nowtMillisecond;
            return (nowtMillisecond - START_TIMESTAMP) << HIGH_DIGIT_LEFT | machineHash << LOW_DIGIT_BIT | lowDigit;
        }

        private long systemMillisecond() {
            return SYSTEM_TIMESTAMP + (System.nanoTime() - SYSTEM_NANOTIME) / 1000000;
        }
    }

    public static class CodeGenerateException extends RuntimeException {

        public CodeGenerateException(String message) {
            super(message);
        }
    }
}
