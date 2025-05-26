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
import tech.qiantong.qdata.spark.etl.reader.ReaderFactory;
import tech.qiantong.qdata.spark.etl.transition.CleanTransition;
import tech.qiantong.qdata.spark.etl.utils.IDGeneratorUtils;
import tech.qiantong.qdata.spark.etl.utils.LogUtils;
import tech.qiantong.qdata.spark.etl.utils.RabbitmqUtils;
import tech.qiantong.qdata.spark.etl.utils.db.DBUtils;
import tech.qiantong.qdata.spark.etl.writer.WriterFactory;

import java.util.*;

/**
 * <P>
 * 用途:ETL程序入口
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
        JSONObject taskInfo = config.getJSONObject("taskInfo");
        String resourceUrl = config.getString("resourceUrl");

        //创建流程实例
        ProcessInstance processInstance = createProcess(taskInfo, now, rabbitmq);

        //注册spark
        SparkConf conf = new SparkConf().setAppName("EtlApplication");

        SparkSession spark = SparkSession.builder()
                .config(conf)
                .getOrCreate();

        //读取配置
        JSONObject reader = taskParams.getJSONObject("reader");
        //参数信息
        JSONObject readParameter = reader.getJSONObject("parameter");

        //输入类型
        TaskComponentTypeEnum readerComponentType = TaskComponentTypeEnum.findEnumByType(reader.getString("componentType"));

        //输入字段
        List<String> readerColumns = new ArrayList<>();
        String readerLogPath = LogUtils.createLogPath(resourceUrl, reader);

        //创建输入节点实例
        TaskInstance readerTaskInstance = createTask(processInstance, readerLogPath, reader, now, rabbitmq);
        //读取数据集
        Dataset<Row> data;
        try {
            data = ReaderFactory.getReader(readerComponentType.getCode())
                    .read(spark, reader, readerColumns, readerLogPath);
            if (data == null) {
                LogUtils.writeLog(readerLogPath, "任务失败");
                updateProcess(processInstance, WorkflowExecutionStatus.FAILURE, rabbitmq);
                //更新输入节点实例执行失败
                updateTask(readerTaskInstance, TaskExecutionStatus.FAILURE, rabbitmq);
                spark.stop();
                return;
            }
            LogUtils.writeLog(readerLogPath, "任务成功");
        } catch (Exception e) {
            log.error("任务失败", e);
            LogUtils.writeLog(readerLogPath, "任务失败");
            updateProcess(processInstance, WorkflowExecutionStatus.FAILURE, rabbitmq);
            //更新输入节点实例执行失败
            updateTask(readerTaskInstance, TaskExecutionStatus.FAILURE, rabbitmq);
            spark.stop();
            return;
        }
        //更新输入节点实例执行成功
        updateTask(readerTaskInstance, TaskExecutionStatus.SUCCESS, rabbitmq);

        if (readParameter.containsKey("batchSize")) {
            //分批处理
            data = data.repartition(readParameter.getInteger("batchSize"));
        }

        if (taskParams.getJSONArray("transition") != null && taskParams.getJSONArray("transition").size() > 0) {
            //读取配置
            JSONArray transitionArr = taskParams.getJSONArray("transition");
            JSONObject transition = (JSONObject) transitionArr.get(0);

            String transitionLogPath = LogUtils.createLogPath(resourceUrl, transition);

            //创建清洗节点实例
            TaskInstance transitionTaskInstance = createTask(processInstance, transitionLogPath, transition, now, rabbitmq);
            try {
                data = CleanTransition.transition(data, transition, transitionLogPath);
                LogUtils.writeLog(transitionLogPath, "任务成功");
            } catch (Exception e) {
                //更新清洗节点实例执行失败
                updateProcess(processInstance, WorkflowExecutionStatus.FAILURE, rabbitmq);
                updateTask(transitionTaskInstance, TaskExecutionStatus.FAILURE, rabbitmq);
                spark.stop();
                LogUtils.writeLog(transitionLogPath, "任务失败");
                return;
            }
            //更新输入节点实例执行成功
            updateTask(transitionTaskInstance, TaskExecutionStatus.SUCCESS, rabbitmq);
        }

        //写入配置
        JSONObject writer = taskParams.getJSONObject("writer");
        //输出类型
        TaskComponentTypeEnum writerComponentType = TaskComponentTypeEnum.findEnumByType(writer.getString("componentType"));

        //创建输出节点实例
        String writerLogPath = LogUtils.createLogPath(resourceUrl, writer);
        TaskInstance writerTaskInstance = createTask(processInstance, writerLogPath, writer, now, rabbitmq);
        Boolean flag = false;
        try {
            flag = WriterFactory.getWriter(writerComponentType.getCode())
                    .writer(config, data, writer, writerLogPath);
        } catch (Exception e) {
            log.error("任务失败", e);
        }

        if (flag) {
            LogUtils.writeLog(writerLogPath, "任务成功");
            updateTask(writerTaskInstance, TaskExecutionStatus.SUCCESS, rabbitmq);
            updateProcess(processInstance, WorkflowExecutionStatus.SUCCESS, rabbitmq);
        } else {
            LogUtils.writeLog(writerLogPath, "任务失败");
            updateTask(writerTaskInstance, TaskExecutionStatus.FAILURE, rabbitmq);
            updateProcess(processInstance, WorkflowExecutionStatus.FAILURE, rabbitmq);
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

    public static TaskInstance createTask(ProcessInstance processInstance, String logPath, JSONObject config, Date now, JSONObject rabbitmq) {
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
                .logPath(logPath)
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
