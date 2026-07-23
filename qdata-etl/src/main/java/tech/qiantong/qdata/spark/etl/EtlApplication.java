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
        DBUtils.init();

        Date now = new Date();
        log.info(args[0]);
        String jsonStr = Base64.decodeStr(args[0]);
        log.info(jsonStr);
        JSONObject taskParams = JSONObject.parseObject(jsonStr);
        JSONObject config = taskParams.getJSONObject("config");
        JSONObject rabbitmq = config.getJSONObject("rabbitmq");
        JSONObject redis = config.getJSONObject("redis");
        JSONObject taskInfo = config.getJSONObject("taskInfo");

        // Initialize redis (compatible with historical tasks, repackage after configuring default values in RedisUtils)
        if (redis != null && redis.size() > 0) {
            RedisUtils.init(redis);
        }

        //Create process instance
        ProcessInstance processInstance = createProcess(taskInfo, now, rabbitmq);

        //Register spark
        SparkConf conf = new SparkConf().setAppName("EtlApplication");

        SparkSession spark = SparkSession.builder()
                .config(conf)
                .getOrCreate();

        //Read configuration
        JSONObject reader = taskParams.getJSONObject("reader");
        //Parameter information
        JSONObject readParameter = reader.getJSONObject("parameter");

        //Input type
        TaskComponentTypeEnum readerComponentType = TaskComponentTypeEnum.findEnumByType(reader.getString("componentType"));

        //Input field
        List<String> readerColumns = new ArrayList<>();

        //Create an input node instance
        TaskInstance readerTaskInstance = createTask(processInstance, reader, now, rabbitmq);
        LogUtils.Params readerLogParams = new LogUtils.Params(rabbitmq, readerTaskInstance.getProcessInstanceId(), readerTaskInstance.getId());

        //Read the data set
        Dataset<Row> data;
        try {
            data = ReaderFactory.getReader(readerComponentType.getCode())
                    .read(spark, reader, readerColumns, readerLogParams);
            if (data == null) {
                LogUtils.writeLog(readerLogParams, MessageUtils.messageEn("etl.task.failed"));
                updateProcess(processInstance, WorkflowExecutionStatus.FAILURE, rabbitmq);
                //Update input node instance execution failed
                updateTask(readerTaskInstance, TaskExecutionStatus.FAILURE, rabbitmq);
                spark.stop();
                return;
            }
        } catch (Exception e) {
            log.error(MessageUtils.message("etl.task.failed"), e);
            updateProcess(processInstance, WorkflowExecutionStatus.FAILURE, rabbitmq);
            //Update input node instance execution failed
            updateTask(readerTaskInstance, TaskExecutionStatus.FAILURE, rabbitmq);
            LogUtils.writeLog(readerLogParams, MessageUtils.messageEn("etl.failure.reason", e.getMessage()));
            LogUtils.writeLog(readerLogParams, MessageUtils.messageEn("etl.task.failed"));
            LogUtils.writeLog(readerLogParams, "FINALIZE_SESSION");
            spark.stop();
            return;
        }

        //Update input node instance executed successfully
        updateTask(readerTaskInstance, TaskExecutionStatus.SUCCESS, rabbitmq);
        LogUtils.writeLog(readerLogParams, MessageUtils.messageEn("etl.task.success"));
        LogUtils.writeLog(readerLogParams, "FINALIZE_SESSION");

//        if (readParameter.containsKey("batchSize")) {
// //Batch processing
//            data = data.repartition(readParameter.getInteger("batchSize"));
//        }

        if (taskParams.getJSONArray("transition") != null && taskParams.getJSONArray("transition").size() > 0) {
            //Read configuration
            JSONArray transitionArr = taskParams.getJSONArray("transition");
            for (int i = 0; i < transitionArr.size(); i++) {
                JSONObject transition = (JSONObject) transitionArr.get(i);
                //Conversion type
                TaskComponentTypeEnum transitionComponentType = TaskComponentTypeEnum.findEnumByType(transition.getString("componentType"));

                //Create a transformation node instance
                TaskInstance transitionTaskInstance = createTask(processInstance, transition, now, rabbitmq);
                LogUtils.Params transitionLogParams = new LogUtils.Params(rabbitmq, transitionTaskInstance.getProcessInstanceId(), transitionTaskInstance.getId());

                try {
                    data = TransitionFactory.getTransition(transitionComponentType.getCode())
                            .transition(spark, data, transition, transitionLogParams);
                } catch (Exception e) {
                    //Update cleaning node instance execution failed
                    updateProcess(processInstance, WorkflowExecutionStatus.FAILURE, rabbitmq);
                    updateTask(transitionTaskInstance, TaskExecutionStatus.FAILURE, rabbitmq);
                    spark.stop();
                    LogUtils.writeLog(transitionLogParams, MessageUtils.messageEn("etl.failure.reason", e.getMessage()));
                    LogUtils.writeLog(transitionLogParams, MessageUtils.messageEn("etl.task.failed"));
                    LogUtils.writeLog(transitionLogParams, "FINALIZE_SESSION");
                    spark.stop();
                    return;
                }
                //Update input node instance executed successfully
                updateTask(transitionTaskInstance, TaskExecutionStatus.SUCCESS, rabbitmq);
                LogUtils.writeLog(transitionLogParams, MessageUtils.messageEn("etl.task.success"));
                LogUtils.writeLog(transitionLogParams, "FINALIZE_SESSION");
            }
        }
        //Write configuration
        JSONObject writer = taskParams.getJSONObject("writer");
        //Output type
        TaskComponentTypeEnum writerComponentType = TaskComponentTypeEnum.findEnumByType(writer.getString("componentType"));


        //Create an output node instance
        TaskInstance writerTaskInstance = createTask(processInstance, writer, now, rabbitmq);

        LogUtils.Params writerLogParams = new LogUtils.Params(rabbitmq, writerTaskInstance.getProcessInstanceId(), writerTaskInstance.getId());

        Boolean flag = false;
        try {
            flag = WriterFactory.getWriter(writerComponentType.getCode())
                    .writer(config, data, writer, writerLogParams);
        } catch (Exception e) {
            log.error(MessageUtils.message("etl.task.failed"), e);
            LogUtils.writeLog(writerLogParams, MessageUtils.messageEn("etl.failure.reason", e.getMessage()));
        }

        if (flag) {
            updateTask(writerTaskInstance, TaskExecutionStatus.SUCCESS, rabbitmq);
            updateProcess(processInstance, WorkflowExecutionStatus.SUCCESS, rabbitmq);
            LogUtils.writeLog(writerLogParams, MessageUtils.messageEn("etl.task.success"));
            LogUtils.writeLog(writerLogParams, "FINALIZE_SESSION");
            //Determine whether there is data cache
            if (reader.containsKey("cacheDataMap")) {
                Map<String, String> cacheDataMap = (Map<String, String>) reader.get("cacheDataMap");
                cacheDataMap.forEach((key, value) -> {
                    RedisUtils.set(key, value, -1);
                });
            }
        } else {
            updateTask(writerTaskInstance, TaskExecutionStatus.FAILURE, rabbitmq);
            updateProcess(processInstance, WorkflowExecutionStatus.FAILURE, rabbitmq);
            LogUtils.writeLog(writerLogParams, MessageUtils.messageEn("etl.task.failed"));
            LogUtils.writeLog(writerLogParams, "FINALIZE_SESSION");
        }
        spark.stop();
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
