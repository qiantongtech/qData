package tech.qiantong.qdata.module.dpp.utils.ds.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.utils.MD5Util;
import tech.qiantong.qdata.common.enums.TaskComponentTypeEnum;
import tech.qiantong.qdata.module.dpp.utils.model.DsResource;

import java.util.*;

/**
 * <P>
 * 用途:Kafka输入组件
 * </p>
 *
 * @author: FXB
 * @create: 2025-03-25 13:48
 **/
public class KafkaReaderComponent implements ComponentItem {
    @Override
    public Map<String, Object> parse(Map<String, Object> params) {
        Map<String, Object> taskParams = new LinkedHashMap<>();
        taskParams.put("localParams", params.getOrDefault("localParams", new ArrayList<>())); // 默认空列表
        taskParams.put("resourceList", params.getOrDefault("resourceList", new ArrayList<>())); // 默认空列表
        taskParams.put("customConfig", params.getOrDefault("customConfig", 1)); // 默认写死1
        taskParams.put("xms", params.getOrDefault("xms", 1)); // 默认1
        taskParams.put("xmx", params.getOrDefault("xmx", 1)); // 默认1
        taskParams.put("json", buildJson(params)); // 默认空的JSON字符串
        return taskParams;
    }

    @Override
    public String code() {
        return TaskComponentTypeEnum.KAFKA_READER.getCode();
    }


    /**
     * 构建JSON字符串
     *
     * @param taskParams {
     *                   "topic": "test",
     *                   "columns": [
     *                   {
     *                   "name": "id",
     *                   "type": "LONG",
     *                   "key": "id"
     *                   },
     *                   {
     *                   "name": "name",
     *                   "type": "STRING",
     *                   "key": "name"
     *                   },
     *                   {
     *                   "name": "age",
     *                   "type": "LONG",
     *                   "key": "age"
     *                   }
     *                   ],
     *                   "config": "{\"group.id\":\"test\"}",
     *                   "readerDatasource": {
     *                   <p>
     *                   },
     *                   "writerDatasource": {
     *                   <p>
     *                   },
     *                   "target_table_name": "xxx",
     *                   "target_columns": "xxx"
     *                   }
     * @return
     */
    public static String buildJson(Map<String, Object> taskParams) {

        // 创建最外层的 jobJson Map
        Map<String, Object> jobJson = new HashMap<>();

        // 设置 job 相关的 setting 配置
        Map<String, Object> setting = new HashMap<>();

        // speed 配置，默认值已直接赋予
        Map<String, Object> speed = new HashMap<>();
        speed.put("channel", 1);  // 默认值
        speed.put("bytes", 0);    // 默认值
        setting.put("speed", speed);

        // errorLimit 配置，默认值已直接赋予
        Map<String, Object> errorLimit = new HashMap<>();
        errorLimit.put("record", 999999999);  // 默认值
        setting.put("errorLimit", errorLimit);

        // restore 配置，默认值已直接赋予
        Map<String, Object> restore = new HashMap<>();
        restore.put("maxRowNumForCheckpoint", 0);   // 默认值
        restore.put("isRestore", false);            // 默认值
        restore.put("restoreColumnName", "");       // 默认值
        restore.put("restoreColumnIndex", 0);       // 默认值
        setting.put("restore", restore);

        // log 配置，默认值已直接赋予
        Map<String, Object> log = new HashMap<>();
        log.put("isLogger", false);  // 默认值
        log.put("level", "debug");   // 默认值
        log.put("path", "");         // 默认值
        log.put("pattern", "");      // 默认值
        setting.put("log", log);

        jobJson.put("setting", setting);


        // 输出readerDatasource
        Map<String, Object> readerDatasource = (Map<String, Object>) MapUtils.getObject(taskParams, "readerDatasource");
        // 输入writerDatasource
        Map<String, Object> writerDatasource = (Map<String, Object>) MapUtils.getObject(taskParams, "writerDatasource");

        DbQueryProperty writerProperty = MD5Util.buildJobDatasource(writerDatasource);

        String ip = MapUtils.getString(readerDatasource, "ip");
        long port = MapUtils.getLong(readerDatasource, "port");
        String topic = MapUtils.getString(taskParams, "topic");
        String target_table_name = MapUtils.getString(taskParams, "target_table_name", "");
        Object columns = MapUtils.getObject(taskParams, "columns");
        Object target_columns = MapUtils.getObject(taskParams, "target_columns");
        String config = MapUtils.getString(taskParams, "config");
        String writeMode = "insert";

        // 创建 job 相关的 content 配置
        List<Map<String, Object>> content = new ArrayList<>();
        Map<String, Object> contentItem = new HashMap<>();

        // reader 配置
        Map<String, Object> reader = new HashMap<>();
        reader.put("name", "kafkareader");
        Map<String, Object> readerParameter = new HashMap<>();
        readerParameter.put("bootstrapServers", ip + ":" + port);
        readerParameter.put("topic", topic);
        readerParameter.put("readType", "json");
        readerParameter.put("column", columns);
        readerParameter.put("pollTimeoutMs", 2000);
        if (StringUtils.isNotBlank(config)) {
            readerParameter.put("config", JSONObject.parse(config));
        }
        reader.put("parameter", readerParameter);

        // writer 配置
        Map<String, Object> writer = new HashMap<>();
        writer.put("name", writerProperty.trainToJdbcWriterName());
        Map<String, Object> writerParameter = new HashMap<>();
        writerParameter.put("username", writerProperty.getUsername());
        writerParameter.put("password", writerProperty.getPassword());
        writerParameter.put("batchSize", taskParams.getOrDefault("batchSize", 1024)); // 默认1024
        //
        writerParameter.put("writeMode", writeMode);
        writerParameter.put("column", target_columns);
        List<Map<String, Object>> writerConnection = new ArrayList<>();
        Map<String, Object> writerConnectionItem = new HashMap<>();
        writerConnectionItem.put("table", Arrays.asList(writerProperty.getDbNameTableName(target_table_name)));
        writerConnectionItem.put("jdbcUrl", writerProperty.trainToJdbcUrl());
        writerConnection.add(writerConnectionItem);
        writerParameter.put("connection", writerConnection);
        writer.put("parameter", writerParameter);

        // 将 reader 和 writer 添加到 content 中
        contentItem.put("reader", reader);
        contentItem.put("writer", writer);
        content.add(contentItem);

        jobJson.put("content", content);
        Map<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("job", jobJson);
        // 转换为 JSON 字符串并返回
        return JSON.toJSONString(objectObjectHashMap);
    }

    @Override
    public Map<String, Object> parse2(String nodeCode, Integer nodeVersion, TaskComponentTypeEnum componentType, Map<String, Object> taskParams, String resourceUrl, List<DsResource> resourceList) {
        // 输入readerDatasource
        Map<String, Object> readerDatasource = (Map<String, Object>) MapUtils.getObject(taskParams, "readerDatasource");
        DbQueryProperty readerProperty = MD5Util.buildJobDatasource(readerDatasource);

        // reader 配置
        Map<String, Object> reader = new HashMap<>();
        reader.put("nodeCode", nodeCode);
        reader.put("nodeVersion", nodeVersion);
        reader.put("componentType", componentType.getCode());

        //参数
        Map<String, Object> parameter = new HashMap<>();
        reader.put("parameter", parameter);

        parameter.put("bootstrapServers", readerProperty.getHost() + ":" + readerProperty.getPort());
        parameter.put("topic", taskParams.get("topic"));
        parameter.put("column", taskParams.get("columns"));
        String config = MapUtils.getString(taskParams, "config");
        if (StringUtils.isNotBlank(config)) {
            parameter.put("config", JSONObject.parse(config));
        }else{
            parameter.put("config", new HashMap<>());
        }
        return reader;
    }
}
