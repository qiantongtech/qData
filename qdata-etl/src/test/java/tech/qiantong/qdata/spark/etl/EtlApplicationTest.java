package tech.qiantong.qdata.spark.etl;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.junit.jupiter.api.Test;
import tech.qiantong.qdata.api.ds.api.etl.ds.ProcessInstance;
import tech.qiantong.qdata.api.ds.api.etl.ds.TaskInstance;
import tech.qiantong.qdata.common.enums.*;
import tech.qiantong.qdata.spark.etl.reader.ReaderFactory;
import tech.qiantong.qdata.spark.etl.transition.TransitionFactory;
import tech.qiantong.qdata.spark.etl.utils.LogUtils;
import tech.qiantong.qdata.spark.etl.utils.db.DBUtils;
import tech.qiantong.qdata.spark.etl.writer.WriterFactory;

import java.util.*;

import static tech.qiantong.qdata.spark.etl.EtlApplication.*;

@Slf4j
class EtlApplicationTest {


    @Test
    public void test() {

        DBUtils.init();

        Date now = new Date();
        String args = "";
        String jsonStr = Base64.decodeStr(args);
        log.info(jsonStr);
        JSONObject taskParams = JSONObject.parseObject(jsonStr);
        JSONObject config = taskParams.getJSONObject("config");
        JSONObject rabbitmq = config.getJSONObject("rabbitmq");
        JSONObject redis = config.getJSONObject("redis");
        JSONObject taskInfo = config.getJSONObject("taskInfo");
        String resourceUrl = config.getString("resourceUrl");

        //创建流程实例
        ProcessInstance processInstance = createProcess(taskInfo, now, rabbitmq);

        //注册spark
        SparkConf conf = new SparkConf().setAppName("EtlApplication")
                .setMaster("local[*]")
                .set("spark.executor.memory", "6g")
                .set("spark.driver.memory", "3g")
                .set("spark.driver.host", "localhost")
                .set("spark.driver.bindAddress", "localhost")
                .set("spark.sql.shuffle.partitions", "2");

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

        //创建输入节点实例
        TaskInstance readerTaskInstance = createTask(processInstance, reader, now, rabbitmq);
        LogUtils.Params readerLogParams = new LogUtils.Params(rabbitmq, readerTaskInstance.getProcessInstanceId(), readerTaskInstance.getId());

        //读取数据集
        Dataset<Row> data;
        try {
            data = ReaderFactory.getReader(readerComponentType.getCode())
                    .read(spark, reader, readerColumns, readerLogParams);
            if (data == null) {
                LogUtils.writeLog(readerLogParams, "任务失败");
                updateProcess(processInstance, WorkflowExecutionStatus.FAILURE, rabbitmq);
                //更新输入节点实例执行失败
                updateTask(readerTaskInstance, TaskExecutionStatus.FAILURE, rabbitmq);
                spark.stop();
                return;
            }
        } catch (Exception e) {
            log.error("任务失败", e);
            updateProcess(processInstance, WorkflowExecutionStatus.FAILURE, rabbitmq);
            //更新输入节点实例执行失败
            updateTask(readerTaskInstance, TaskExecutionStatus.FAILURE, rabbitmq);
            LogUtils.writeLog(readerLogParams, "任务失败");
            LogUtils.writeLog(readerLogParams, "FINALIZE_SESSION");
            spark.stop();
            return;
        }

        //更新输入节点实例执行成功
        updateTask(readerTaskInstance, TaskExecutionStatus.SUCCESS, rabbitmq);
        LogUtils.writeLog(readerLogParams, "任务成功");
        LogUtils.writeLog(readerLogParams, "FINALIZE_SESSION");

//        if (readParameter.containsKey("batchSize")) {
//            //分批处理
//            data = data.repartition(readParameter.getInteger("batchSize"));
//        }

        if (taskParams.getJSONArray("transition") != null && taskParams.getJSONArray("transition").size() > 0) {
            //读取配置
            JSONArray transitionArr = taskParams.getJSONArray("transition");
            for (int i = 0; i < transitionArr.size(); i++) {
                JSONObject transition = (JSONObject) transitionArr.get(i);
                //转换类型
                TaskComponentTypeEnum transitionComponentType = TaskComponentTypeEnum.findEnumByType(transition.getString("componentType"));

                //创建转换节点实例
                TaskInstance transitionTaskInstance = createTask(processInstance, transition, now, rabbitmq);
                LogUtils.Params transitionLogParams = new LogUtils.Params(rabbitmq, transitionTaskInstance.getProcessInstanceId(), transitionTaskInstance.getId());

                try {
                    data = TransitionFactory.getTransition(transitionComponentType.getCode())
                            .transition(spark, data, transition, transitionLogParams);
                } catch (Exception e) {
                    //更新清洗节点实例执行失败
                    updateProcess(processInstance, WorkflowExecutionStatus.FAILURE, rabbitmq);
                    updateTask(transitionTaskInstance, TaskExecutionStatus.FAILURE, rabbitmq);
                    spark.stop();
                    LogUtils.writeLog(transitionLogParams, "任务失败");
                    LogUtils.writeLog(transitionLogParams, "FINALIZE_SESSION");
                    spark.stop();
                    return;
                }
                //更新输入节点实例执行成功
                updateTask(transitionTaskInstance, TaskExecutionStatus.SUCCESS, rabbitmq);
                LogUtils.writeLog(transitionLogParams, "任务成功");
                LogUtils.writeLog(transitionLogParams, "FINALIZE_SESSION");
            }
        }

        //写入配置
        JSONObject writer = taskParams.getJSONObject("writer");
        //输出类型
        TaskComponentTypeEnum writerComponentType = TaskComponentTypeEnum.findEnumByType(writer.getString("componentType"));


        //创建输出节点实例
        TaskInstance writerTaskInstance = createTask(processInstance, writer, now, rabbitmq);

        LogUtils.Params writerLogParams = new LogUtils.Params(rabbitmq, writerTaskInstance.getProcessInstanceId(), writerTaskInstance.getId());

        Boolean flag = false;
        try {
            flag = WriterFactory.getWriter(writerComponentType.getCode())
                    .writer(config, data, writer, writerLogParams);
        } catch (Exception e) {
            log.error("任务失败", e);
        }

        if (flag) {
            updateTask(writerTaskInstance, TaskExecutionStatus.SUCCESS, rabbitmq);
            updateProcess(processInstance, WorkflowExecutionStatus.SUCCESS, rabbitmq);
            LogUtils.writeLog(writerLogParams, "任务成功");
            LogUtils.writeLog(writerLogParams, "FINALIZE_SESSION");
        } else {
            updateTask(writerTaskInstance, TaskExecutionStatus.FAILURE, rabbitmq);
            updateProcess(processInstance, WorkflowExecutionStatus.FAILURE, rabbitmq);
            LogUtils.writeLog(writerLogParams, "任务失败");
            LogUtils.writeLog(writerLogParams, "FINALIZE_SESSION");
        }
        spark.stop();
    }

}