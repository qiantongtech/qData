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

package tech.qiantong.qdata.spark.etl.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import tech.qiantong.qdata.common.utils.MessageUtils;
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
        log.info(MessageUtils.message("log.etl.log.path", logPath));
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
