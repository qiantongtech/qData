package tech.qiantong.qdata.module.dpp.utils.ds.component;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.constants.DbType;
import tech.qiantong.qdata.common.database.utils.MD5Util;
import tech.qiantong.qdata.common.enums.TaskComponentTypeEnum;
import tech.qiantong.qdata.module.dpp.utils.datax.FlinkxJson;
import tech.qiantong.qdata.module.dpp.utils.model.DsResource;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <P>
 * 用途:数据库输入组件
 * </p>
 *
 * @author: FXB
 * @create: 2025-03-12 16:31
 **/
public class DBReaderComponent implements ComponentItem {

    /**
     * taskParams SPARK（sparksql开发）
     * {
     * "batchSize":1024,//读取数量，空时默认1024
     * "datasource_id":1,//源表数据源id 如果是输出节点时该字段为null
     * "asset_id":1,//源表资产id 如果是输出节点时该字段为null
     * "table_name":1,//源表名 如果是输出节点时该字段为在后端自动生成
     * "columns":["ID","NAME","AGE"],//源表同步字段列表 如果是输出节点时该字段为在后端自动生成
     * "where":"",//条件
     * "querySql": "sql",//该值不为空时 column where不能有值
     * }
     *
     * @param params
     * @return
     */
    @Override
    public Map<String, Object> parse(Map<String, Object> params) {
        Map<String, Object> taskParams = new LinkedHashMap<>();
        taskParams.put("localParams", params.getOrDefault("localParams", new ArrayList<>())); // 默认空列表
        taskParams.put("resourceList", params.getOrDefault("resourceList", new ArrayList<>())); // 默认空列表
        taskParams.put("customConfig", params.getOrDefault("customConfig", 1)); // 默认写死1
        taskParams.put("xms", params.getOrDefault("xms", 1)); // 默认1
        taskParams.put("xmx", params.getOrDefault("xmx", 1)); // 默认1
        taskParams.put("json", FlinkxJson.buildJobJsonMasterdata(params)); // 默认空的JSON字符串
        return taskParams;
    }

    @Override
    public String code() {
        return TaskComponentTypeEnum.DB_READER.getCode();
    }


    /**
     * taskParams SPARK（sparksql开发）
     * {
     * "batchSize":1024,//读取数量，空时默认1024
     * "datasource_id":1,//源表数据源id 如果是输出节点时该字段为null
     * "asset_id":1,//源表资产id 如果是输出节点时该字段为null
     * "table_name":1,//源表名 如果是输出节点时该字段为在后端自动生成
     * "columns":["ID","NAME","AGE"],//源表同步字段列表 如果是输出节点时该字段为在后端自动生成
     * "where":"",//条件
     * "querySql": "sql",//该值不为空时 column where不能有值
     * "read_type":"1",//读取方式 1:全量 2:id增量 3:时间范围增量 默认全量
     * "idIncrementConfig":{
     * "incrementColumn":"ID",//增量字段
     * "incrementStart":1,//开始值
     * },
     * "dateIncrementConfig":{
     * "logic":"and",//逻辑运算符 1:and 2:or 默认and
     * "dateFormat":"yyyy-MM-dd",//时间格式 yyyy-MM-dd 或 yyyy-MM-dd HH:mm:ss（手动输入）
     * "column":[
     * {
     * "type":"1",//类型  1:固定值 2:自动(当前时间) 3:SQL表达式
     * "incrementColumn":"CREATE_TIME",//增量字段
     * "operator":">",//时间 运算符 > 、=>、< 、<=
     * "data":"xxx",//固定值：为 2023-01-01  SQL表达式：为sql函数
     * }
     * ]
     * }
     * }
     *
     * @param nodeCode
     * @param componentType
     * @param taskParams
     * @return
     */
    @Override
    public Map<String, Object> parse2(String nodeCode, Integer nodeVersion, TaskComponentTypeEnum componentType, Map<String, Object> taskParams, String resourceUrl, List<DsResource> resourceList) {
        // reader 配置
        Map<String, Object> reader = new HashMap<>();
        // 输入readerDatasource
        Map<String, Object> readerDatasource = (Map<String, Object>) MapUtils.getObject(taskParams, "readerDatasource");
        DbQueryProperty readerProperty = MD5Util.buildJobDatasource(readerDatasource);
        reader.put("nodeCode", nodeCode);
        reader.put("nodeVersion", nodeVersion);
        reader.put("componentType", componentType.getCode());

        //参数
        Map<String, Object> parameter = new HashMap<>();
        reader.put("parameter", parameter);

        parameter.put("batchSize", taskParams.getOrDefault("batchSize", 1024));
        parameter.put("datasourceId", readerDatasource.get("datasourceId"));
        parameter.put("username", readerProperty.getUsername());
        parameter.put("password", readerProperty.getPassword());
        parameter.put("dbType", readerProperty.getDbType());
        if (StringUtils.isNotBlank(readerProperty.getDbName())) {
            parameter.put("dbName", readerProperty.getDbName());
        }
        if (StringUtils.isNotBlank(readerProperty.getSid())) {
            parameter.put("sid", readerProperty.getSid());
        }
        parameter.put("column", taskParams.get("columns"));

        parameter.put("where", taskParams.get("where"));
        parameter.put("readModeType", taskParams.get("readModeType"));
        parameter.put("idIncrementConfig", taskParams.get("idIncrementConfig"));
        parameter.put("dateIncrementConfig", taskParams.get("dateIncrementConfig"));

        Map<String, Object> connection = new HashMap<>();
        if (taskParams.containsKey("querySql") && StringUtils.isNotBlank(taskParams.get("querySql").toString())) {
            connection.put("querySql", taskParams.get("querySql"));
        } else {
            connection.put("table", taskParams.get("table_name"));
        }

        connection.put("jdbcUrl", readerProperty.trainToJdbcUrl());
        parameter.put("connection", connection);
        parameter.put("readerProperty", readerProperty);
        return reader;
    }
}
