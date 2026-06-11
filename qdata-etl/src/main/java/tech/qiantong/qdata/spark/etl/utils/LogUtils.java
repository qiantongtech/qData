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

package tech.qiantong.qdata.spark.etl.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Arrays;
import java.util.Date;

/**
 * <P>
 * 用途:
 * </p>
 *
 * @author: FXB
 * @create: 2025-04-30 09:34
 **/
@Slf4j
public class LogUtils {

    /**
     * 生成日志路径
     *
     * @param nodeJson
     * @return
     */
    @Deprecated
    public static String createLogPath(String resourceUrl, JSONObject nodeJson) {
        String logPath = resourceUrl
                + DateUtil.format(new Date(), "yyyyMMdd") + File.separator
                + nodeJson.getString("nodeCode") + File.separator
                + nodeJson.getString("nodeVersion") + File.separator
                + IDGeneratorUtils.getStringId() + ".log";
        log.info("日志路径: {}", logPath);
        FileUtil.touch(logPath);
        return logPath;
    }

    @Deprecated
    public static void writeLog(String logPath, String meesage) {
        synchronized (logPath) {
            String time = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS");
            meesage = time + " - " + meesage;
            FileUtil.appendUtf8Lines(Arrays.asList(meesage), logPath);
        }
    }

    public static void writeLog(Params params, String meesage) {
        synchronized (params) {
            String time = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS");
            meesage = time + " - " + meesage+ "\n";
            params.setLog(meesage);
            RabbitmqUtils.convertAndSend(params.getRabbitmq(), "ds.exchange.taskInstance.log", "ds.queue.taskInstance.log", params);
        }
    }

    @Data
    public static class Params {

        /**
         * rabbitmq连接信息
         */
        @JsonIgnore
        private JSONObject rabbitmq;

        /**
         * 流程实例ID
         */
        private String workflowInstanceId;
        /**
         * 任务实例ID
         */
        private String taskInstanceId;

        /**
         * 日志
         */
        private String log;

        public Params(JSONObject rabbitmq, Long workflowInstanceId, Long taskInstanceId) {
            this.rabbitmq = rabbitmq;
            this.workflowInstanceId = String.valueOf(workflowInstanceId);
            this.taskInstanceId = String.valueOf(taskInstanceId);
        }
    }

}
