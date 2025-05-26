package tech.qiantong.qdata.api.ds.api.etl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <P>
 * 用途:任务保存请求参数DTO
 * </p>
 *
 * @author: FXB
 * @create: 2025-02-19 09:19
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DsTaskSaveReqDTO {

    /**
     * 任务编码（可空）
     */
    Long processDefinitionCode;

    /**
     * 任务名称（必填）
     */
    private String name;

    /**
     * 任务描述
     */
    private String description;

    /**
     * 执行策略（必填） PARALLEL、SERIAL_WAIT、SERIAL_DISCARD、SERIAL_PRIORITY
     */
    private String executionType;

    /**
     * 节点定义json字符串 （必填）
     * 格式如下
     * [{
     * "id":12,
     * "name":"test",
     * "code":133455210749664,
     * "version":10,//版本号
     * "description":"",
     * "workerGroup": "default",//默认 "default"
     * "environmentCode":133155949418208,//默认 133155949418208
     * "flag":"YES",//默认 "YES"
     * "isCache":"NO",//默认 "NO"
     * "taskParams":{"desc"},//JSON对象 结构在下方注释中 注：不是字符串
     * "taskPriority":"MEDIUM",//优先级 默认"MEDIUM"
     * "taskType":"SPARK"//节点类型 DATAX、SPARK、SQL、PROCEDURE（存储过程）、SUB_PROCESS（子任务）等
     * }]
     * taskParams SPARK(清洗)
     * {
     *     "localParams": [],//默认 []
     *     "rawScript": "",//默认 ""
     *     "resourceList": [],//默认 []
     *     "programType": "JAVA",//默认"JAVA"
     *     "mainClass": "com.demo.WordCount",//默认取配置文件中的 ds.spark.main_jar
     *     "mainJar": {
     *         "resourceName": "file:/dolphinscheduler/default/resources/spart-demo-1.0.jar"//默认取配置文件中的 ds.spark.main_jar
     *     },
     *     "deployMode": "client",//默认 "client"
     *     "mainArgs": "这块写入数据源、库表信息及规则的数据json",
     *     "master": "spark://qiantong100:7077",//默认取配置文件中的 ds.spark.master_url
     *     "driverCores": 1,//默认 1
     *     "driverMemory": "512M",//默认 "512M"
     *     "numExecutors": 1,//默认 1
     *     "executorMemory": "1G",//默认 1G
     *     "executorCores": 1,//默认 1
     *     "sqlExecutionType": "SCRIPT"//默认"SCRIPT"
     * }
     * taskParams DATAX
     * {
     *     "localParams": [],//默认 []
     *     "resourceList":[],//默认 []
     *     "customConfig": 0,//默认 []
     *     "customConfig": 1,//写死1
     *     "json": "{}",//DATAX 的JSON字符串数据默认1024
     *     "xms": 1,//默认1
     *     "xmx": 1,//默认1
     *     "batchSize":1024,//一次性写入量，可空，空时datax默认1024
     *     "preSql": "",//前置sql 生成datax json时会转换成数组(已分号进行切割)
     *     "postSql": "",//后置置sql  生成datax json时会转换成数组(已分号进行切割)
     *     //下方为前端返显所需的源表信息及目标表信息数据
     *     "type":1,//节点类型 1:输入节点 2:输出节点
     *     "datasource_id":1,//源表数据源id 如果是输出节点时该字段为null
     *     "asset_id":1,//源表资产id 如果是输出节点时该字段为null
     *     "table_name":1,//源表名 如果是输出节点时该字段为在后端自动生成
     *     "columns":["ID","NAME","AGE"],//源表同步字段列表 如果是输出节点时该字段为在后端自动生成
     *     "target_datasource_id":1,//目标数据源id 如果是输入节点时该字段为null
     *     "target_asset_id":1,//目标资产id 如果是输入节点时该字段为null
     *     "target_table_name":1,//目标表名 如果是输入节点时该字段为在后端自动生成
     *     "target_columns":["ID","NAME","AGE"],//目标表同步字段列表 如果是输入节点时该字段为在后端自动生成
     * }
     *
     * taskParams SQL (关系数据库)
     * {
     *     "localParams": [],//默认 []
     *     "resourceList": [],//默认 []
     *     "type":"MYSQL",//数据库类型 目前支持 MYSQL、ORACLE、DAMENG、KINGBASE
     *     "sql":"",//sql语句
     *     "sqlType":"", // 0 查询 1 非查询
     *     "preStatements":[],//默认 []
     *     "postStatements":[],//默认 []
     *     "displayRows":10,//默认10
     *     "datasources":{
     *         "type":"MYSQL",//数据库类型 目前支持 MYSQL、ORACLE、DAMENG、KINGBASE
     *         "host":"",//ip
     *         "port":1521,//端口
     *         "userName":"",//账号
     *         "password":"",//密码
     *         "database":"",//数据库名
     *         "connectType":"ORACLE_SERVICE_NAME"//默认“ORACLE_SERVICE_NAME”即可，只有oracle才需传入 ORACLE_SERVICE_NAME:服务名 ORACLE_SID:SID
     *         "other":{}//数据库其他参数 可不传
     *     }
     * }
     *
     *
     * taskParams PROCEDURE (存储过程)
     * {
     *     "localParams": [],//默认 []
     *     "resourceList": [],//默认 []
     *     "type":"MYSQL",//数据库类型 目前支持 MYSQL、ORACLE、DAMENG、KINGBASE
     *     "method":"call test(${in1},${out1});",//调用存储过程
     *     "preStatements":[],//默认 []
     *     "postStatements":[],//默认 []
     *     "displayRows":10,//默认10
     *     "datasources":{
     *         "type":"MYSQL",//数据库类型
     *         "host":"",//ip
     *         "port":1521,//端口
     *         "userName":"",//账号
     *         "password":"",//密码
     *         "database":"",//数据库名
     *         "connectType":"ORACLE_SERVICE_NAME"//默认“ORACLE_SERVICE_NAME”即可，只有oracle才需传入 ORACLE_SERVICE_NAME:服务名 ORACLE_SID:SID
     *         "other":{
     *             "schema":"xxx"//模式名，只有oracle 12g及以上才生效
     *         }//其他参数 可不传
     *     }
     * }
     *
     * taskParams SPARK（sparksql开发）
     * {
     *     "localParams": [],//默认 []
     *     "rawScript": "脚本",//脚本
     *     "resourceList": [],//默认 []
     *     "programType": "SQL",//默认"SQL"
     *     "mainClass": "",//默认 ""
     *     "deployMode": "client",//默认 "client"
     *     "yarnQueue":"",//默认 ""
     *     "master": "",//默认""
     *     "driverCores": 1,//默认 1
     *     "driverMemory": "512M",//默认 "512M"
     *     "numExecutors": 1,//默认 1
     *     "executorMemory": "1G",//默认 1G
     *     "executorCores": 1,//默认 1
     *     "sqlExecutionType": "SCRIPT"//默认"SCRIPT"
     * }
     *
     * taskParams SUB_PROCESS（子任务，开发任务也是这个）
     * {
     *     "localParams": [],//默认 []
     *     "resourceList": [],//默认 []
     *     "processDefinitionCode": 135576103357024//子任务编码
     * }
     */
    private String taskDefinitionJson;

    /**
     * 节点关系json字符串 （必填）
     * 格式如下
     * [{
     * "id":1,//修改时才会有该数据
     * "preTaskCode":0,
     * "preTaskVersion":0,
     * "postTaskCode":133455210749664,
     * "postTaskVersion":10,
     * "conditionType":"NONE"//默认"NONE"
     * }]
     */
    private String taskRelationJson;

    /**
     * 节点坐标信息字符串 （必填）
     * 格式如下
     * [{"taskCode":节点编码,"x":225,"y":302}]
     */
    private String locations;

}
