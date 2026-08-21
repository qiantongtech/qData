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

package tech.qiantong.qdata.spark.etl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import tech.qiantong.qdata.api.ds.api.etl.ds.ProcessInstance;
import tech.qiantong.qdata.api.ds.api.etl.ds.TaskInstance;
import tech.qiantong.qdata.common.enums.*;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.spark.etl.reader.ReaderFactory;
import tech.qiantong.qdata.spark.etl.transition.TransitionFactory;
import tech.qiantong.qdata.spark.etl.utils.IDGeneratorUtils;
import tech.qiantong.qdata.spark.etl.utils.LogUtils;
import tech.qiantong.qdata.spark.etl.utils.RabbitmqUtils;
import tech.qiantong.qdata.spark.etl.utils.RedisUtils;
import tech.qiantong.qdata.spark.etl.utils.db.DBUtils;
import tech.qiantong.qdata.spark.etl.writer.WriterFactory;


import java.util.*;

/**
 * <P>
 * Purpose: ETL program entry
 * </p>
 *
 * @author: FXB
 * @create: 2025-04-16 09:43
 **/
@Slf4j
public class EtlApplication {

    public static void main(String[] args) {
        SparkSession spark = null;
        ProcessInstance processInstance = null;
        TaskInstance activeTaskInstance = null;
        LogUtils.Params activeLogParams = null;
        JSONObject rabbitmq = null;

        try {
            DBUtils.init();
            if (args == null || args.length == 0 || args[0] == null) {
                throw new IllegalArgumentException("ETL task parameters must not be empty");
            }

            Date now = new Date();
            log.info(args[0]);
            String jsonStr = Base64.decodeStr(args[0]);
            log.info(jsonStr);
            JSONObject taskParams = JSONObject.parseObject(jsonStr);
            JSONObject config = taskParams.getJSONObject("config");
            rabbitmq = config.getJSONObject("rabbitmq");
            JSONObject redis = config.getJSONObject("redis");
            JSONObject taskInfo = config.getJSONObject("taskInfo");

            // Publish the running state before Redis/Spark startup so the UI can show it promptly.
            processInstance = createProcess(taskInfo, now, rabbitmq);

            // Initialize redis (compatible with historical tasks, repackage after configuring default values in RedisUtils)
            if (redis != null && !redis.isEmpty()) {
                RedisUtils.init(redis);
            }

            SparkConf conf = new SparkConf().setAppName("EtlApplication");
            spark = SparkSession.builder().config(conf).getOrCreate();

            JSONObject reader = taskParams.getJSONObject("reader");
            TaskComponentTypeEnum readerComponentType = TaskComponentTypeEnum.findEnumByType(reader.getString("componentType"));
            List<String> readerColumns = new ArrayList<>();

            activeTaskInstance = createTask(processInstance, reader, now, rabbitmq);
            activeLogParams = new LogUtils.Params(rabbitmq, activeTaskInstance.getProcessInstanceId(), activeTaskInstance.getId());
            Dataset<Row> data = ReaderFactory.getReader(readerComponentType.getCode())
                    .read(spark, reader, readerColumns, activeLogParams);
            if (data == null) {
                throw new IllegalStateException("Database input returned no dataset");
            }
            finishTaskSuccessfully(activeTaskInstance, activeLogParams, rabbitmq);
            activeTaskInstance = null;
            activeLogParams = null;

            JSONArray transitionArr = taskParams.getJSONArray("transition");
            if (transitionArr != null) {
                for (int i = 0; i < transitionArr.size(); i++) {
                    JSONObject transition = transitionArr.getJSONObject(i);
                    TaskComponentTypeEnum transitionComponentType = TaskComponentTypeEnum.findEnumByType(
                            transition.getString("componentType"));

                    activeTaskInstance = createTask(processInstance, transition, now, rabbitmq);
                    activeLogParams = new LogUtils.Params(rabbitmq, activeTaskInstance.getProcessInstanceId(), activeTaskInstance.getId());
                    data = TransitionFactory.getTransition(transitionComponentType.getCode())
                            .transition(spark, data, transition, activeLogParams);
                    finishTaskSuccessfully(activeTaskInstance, activeLogParams, rabbitmq);
                    activeTaskInstance = null;
                    activeLogParams = null;
                }
            }

            JSONObject writer = taskParams.getJSONObject("writer");
            TaskComponentTypeEnum writerComponentType = TaskComponentTypeEnum.findEnumByType(writer.getString("componentType"));
            activeTaskInstance = createTask(processInstance, writer, now, rabbitmq);
            activeLogParams = new LogUtils.Params(rabbitmq, activeTaskInstance.getProcessInstanceId(), activeTaskInstance.getId());

            Boolean writeSucceeded = WriterFactory.getWriter(writerComponentType.getCode())
                    .writer(config, data, writer, activeLogParams);
            if (!Boolean.TRUE.equals(writeSucceeded)) {
                throw new IllegalStateException("Database output returned a failed result");
            }

            // Store the incremental cursor only after the target write succeeds.
            if (reader.containsKey("cacheDataMap")) {
                Map<String, String> cacheDataMap = (Map<String, String>) reader.get("cacheDataMap");
                cacheDataMap.forEach((key, value) -> RedisUtils.set(key, value, -1));
            }

            updateTask(activeTaskInstance, TaskExecutionStatus.SUCCESS, rabbitmq);
            updateProcess(processInstance, WorkflowExecutionStatus.SUCCESS, rabbitmq);
            LogUtils.writeLog(activeLogParams, MessageUtils.messageEn("etl.task.success"));
            LogUtils.writeLog(activeLogParams, "FINALIZE_SESSION");
            activeTaskInstance = null;
            activeLogParams = null;
        } catch (Exception e) {
            log.error("ETL task failed", e);
            RuntimeException callbackFailure = notifyFailure(
                    processInstance, activeTaskInstance, activeLogParams, rabbitmq, e);
            RuntimeException executionFailure = e instanceof RuntimeException
                    ? (RuntimeException) e
                    : new IllegalStateException("ETL task failed", e);
            if (callbackFailure != null) {
                executionFailure.addSuppressed(callbackFailure);
            }
            // A failed ETL must also fail the Spark/DS process; returning here would produce exit code 0.
            throw executionFailure;
        } finally {
            if (spark != null) {
                try {
                    spark.stop();
                } catch (Exception e) {
                    log.warn("Failed to stop Spark session", e);
                }
            }
            // close() is null-safe and also cleans up a partially initialized client.
            RedisUtils.close();
            RabbitmqUtils.close();
        }
    }

    private static void finishTaskSuccessfully(TaskInstance taskInstance, LogUtils.Params logParams,
                                               JSONObject rabbitmq) {
        updateTask(taskInstance, TaskExecutionStatus.SUCCESS, rabbitmq);
        LogUtils.writeLog(logParams, MessageUtils.messageEn("etl.task.success"));
        LogUtils.writeLog(logParams, "FINALIZE_SESSION");
    }

    private static RuntimeException notifyFailure(ProcessInstance processInstance, TaskInstance taskInstance,
                                                  LogUtils.Params logParams, JSONObject rabbitmq, Exception cause) {
        RuntimeException callbackFailure = null;
        if (processInstance != null && rabbitmq != null) {
            try {
                updateProcess(processInstance, WorkflowExecutionStatus.FAILURE, rabbitmq);
            } catch (RuntimeException e) {
                callbackFailure = e;
                log.error("Failed to publish ETL process failure status", e);
            }
        }
        if (taskInstance != null && rabbitmq != null) {
            try {
                updateTask(taskInstance, TaskExecutionStatus.FAILURE, rabbitmq);
            } catch (RuntimeException e) {
                callbackFailure = mergeCallbackFailure(callbackFailure, e);
                log.error("Failed to publish ETL node failure status", e);
            }
        }
        if (logParams != null) {
            String reason = cause.getMessage() == null ? cause.toString() : cause.getMessage();
            callbackFailure = writeFailureLog(logParams,
                    MessageUtils.messageEn("etl.failure.reason", reason), callbackFailure);
            callbackFailure = writeFailureLog(logParams,
                    MessageUtils.messageEn("etl.task.failed"), callbackFailure);
            callbackFailure = writeFailureLog(logParams, "FINALIZE_SESSION", callbackFailure);
        }
        return callbackFailure;
    }

    private static RuntimeException writeFailureLog(LogUtils.Params logParams, String message,
                                                    RuntimeException callbackFailure) {
        try {
            LogUtils.writeLog(logParams, message);
        } catch (RuntimeException e) {
            log.error("Failed to publish ETL failure log", e);
            return mergeCallbackFailure(callbackFailure, e);
        }
        return callbackFailure;
    }

    private static RuntimeException mergeCallbackFailure(RuntimeException existing, RuntimeException next) {
        if (existing == null) {
            return next;
        }
        existing.addSuppressed(next);
        return existing;
    }

    public static ProcessInstance createProcess(JSONObject taskInfo, Date now, JSONObject rabbitmq) {
        ProcessInstance processInstance = ProcessInstance.builder()
                .id(IDGeneratorUtils.getLongId())
                .name(taskInfo.getString("name") + "-" + taskInfo.getInteger("taskVersion") + "-" + DateUtil.format(new Date(), "yyyyMMddHHmmssSSS"))
                .projectCode(taskInfo.getString("projectCode"))
                .processDefinitionCode(taskInfo.getString("taskCode"))
                .processDefinitionVersion(taskInfo.getInteger("taskVersion"))
                .runTimes(1)
                .scheduleTime(now)
                .startTime(now)
                .commandStartTime(now)
                .commandType(CommandType.START_PROCESS)
                .failureStrategy(FailureStrategy.CONTINUE)
                .isSubProcess(Flag.NO)
                .state(WorkflowExecutionStatus.RUNNING_EXECUTION)
                .build();

        Map<String, Object> processInstanceMap = new HashMap<>();
        processInstanceMap.put("type", 1);
        processInstanceMap.put("instance", processInstance);

        RabbitmqUtils.convertAndSend(rabbitmq, "ds.exchange.processInstance", "ds.queue.processInstance", processInstanceMap);
        return processInstance;
    }

    public static void updateProcess(ProcessInstance processInstance, WorkflowExecutionStatus status, JSONObject rabbitmq) {
        processInstance.setState(status);
        processInstance.setEndTime(new Date());

        Map<String, Object> processInstanceMap = new HashMap<>();
        processInstanceMap.put("type", 2);
        processInstanceMap.put("instance", processInstance);

        RabbitmqUtils.convertAndSend(rabbitmq, "ds.exchange.processInstance", "ds.queue.processInstance", processInstanceMap);
    }

    public static TaskInstance createTask(ProcessInstance processInstance, JSONObject config, Date now, JSONObject rabbitmq) {
        String nodeName = config.getString("nodeName");
        String nodeCode = config.getString("nodeCode");
        Integer nodeVersion = config.getInteger("nodeVersion");
        TaskInstance taskInstance = TaskInstance.builder()
                .id(IDGeneratorUtils.getLongId())
                .name(nodeName)
                .taskCode(nodeCode)
                .taskDefinitionVersion(nodeVersion)
                .taskType("SPARK")
                .processInstanceId(processInstance.getId())
                .processInstanceName(processInstance.getName())
                .projectCode(config.getString("projectCode"))
                .taskInstancePriority(Priority.MEDIUM)
                .startTime(now)
                .state(TaskExecutionStatus.RUNNING_EXECUTION)
                .build();
        RabbitmqUtils.convertAndSend(rabbitmq, "ds.exchange.taskInstance", "ds.queue.taskInstance.insert", taskInstance);
        return taskInstance;
    }


    public static void updateTask(TaskInstance taskInstance, TaskExecutionStatus status, JSONObject rabbitmq) {
        taskInstance.setState(status);
        taskInstance.setEndTime(new Date());
        RabbitmqUtils.convertAndSend(rabbitmq, "ds.exchange.taskInstance", "ds.queue.taskInstance.update", taskInstance);
    }
}
