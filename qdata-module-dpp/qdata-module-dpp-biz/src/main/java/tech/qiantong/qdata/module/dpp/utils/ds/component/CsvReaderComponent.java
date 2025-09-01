package tech.qiantong.qdata.module.dpp.utils.ds.component;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import org.apache.commons.collections4.MapUtils;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.utils.MD5Util;
import tech.qiantong.qdata.common.enums.TaskComponentTypeEnum;
import tech.qiantong.qdata.module.dpp.utils.model.DsResource;

import java.io.File;
import java.util.*;

/**
 * <P>
 * 用途:EXCEL输入组件
 * </p>
 *
 * @author: FXB
 * @create: 2025-03-14 11:31
 **/
public class CsvReaderComponent implements ComponentItem {
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
        return TaskComponentTypeEnum.CSV_READER.getCode();
    }


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


        //输入writerDatasource
        Map<String, Object> writerDatasource = (Map<String, Object>) MapUtils.getObject(taskParams, "writerDatasource");

        DbQueryProperty writerProperty = MD5Util.buildJobDatasource(writerDatasource);


        String target_table_name = MapUtils.getString(taskParams, "target_table_name", "");
        Object columns = MapUtils.getObject(taskParams, "columns");
        Object target_columns = MapUtils.getObject(taskParams, "target_columns");
        String writeMode = "insert";

        // 创建 job 相关的 content 配置
        List<Map<String, Object>> content = new ArrayList<>();
        Map<String, Object> contentItem = new HashMap<>();

        // reader 配置
        Map<String, Object> reader = new HashMap<>();
        reader.put("name", "txtfilereader");
        Map<String, Object> readerParameter = new HashMap<>();
        readerParameter.put("path", Arrays.asList(MapUtils.getString(taskParams, "csvFile")));
        readerParameter.put("encoding", "UTF-8");
        readerParameter.put("column", columns);
        readerParameter.put("fieldDelimiter", ",");
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
        // reader 配置
        Map<String, Object> reader = new HashMap<>();
        reader.put("nodeVersion", nodeVersion);
        reader.put("nodeCode", nodeCode);
        reader.put("componentType", componentType.getCode());

        //参数
        Map<String, Object> parameter = new HashMap<>();
        reader.put("parameter", parameter);

        //复制文件到静态资源中
//        String path = resourceUrl + DateUtil.format(new Date(), "yyyyMMdd") + File.separator + nodeCode + ".csv";
//        String csvFile = (String) taskParams.get("csvFile");
//        FileUtil.copy(csvFile, path, true);

        parameter.put("path", taskParams.get("csvFile"));
        parameter.put("startColumn", taskParams.getOrDefault("startColumn", 1));
        parameter.put("startData", taskParams.getOrDefault("startData", 2));
        parameter.put("column", taskParams.get("columns"));
        return reader;
    }
}
