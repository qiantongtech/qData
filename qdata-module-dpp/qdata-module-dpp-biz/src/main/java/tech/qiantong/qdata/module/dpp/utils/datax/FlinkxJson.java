package tech.qiantong.qdata.module.dpp.utils.datax;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Component;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.utils.MD5Util;
import tech.qiantong.qdata.common.utils.StringUtils;

import java.util.*;

@Component
public class FlinkxJson {

    public static String buildJobJsonMasterdata(Map<String, Object> taskParams) {

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


        //取出数据源链接
        // 输出readerDatasource
        Map<String, Object> readerDatasource = (Map<String, Object>) MapUtils.getObject(taskParams, "readerDatasource");
        //输入writerDatasource
        Map<String, Object> writerDatasource = (Map<String, Object>) MapUtils.getObject(taskParams, "writerDatasource");

        DbQueryProperty readerProperty = MD5Util.buildJobDatasource(readerDatasource);
        DbQueryProperty writerProperty = MD5Util.buildJobDatasource(writerDatasource);


        String writeMode = "";
        String where = MapUtils.getString(taskParams, "where", "");
        String preSql = MapUtils.getString(taskParams, "preSql", "");
        String postSql = MapUtils.getString(taskParams, "postSql", "");
        String table_name = MapUtils.getString(taskParams, "table_name", "");
        String target_table_name = MapUtils.getString(taskParams, "target_table_name", "");
        Object columns = MapUtils.getObject(taskParams, "columns");
        Object target_columns = MapUtils.getObject(taskParams, "target_columns");
        Object writeKeySet = MapUtils.getObject(taskParams, "selectedColumns");//主键
        //节点类型 1:输入节点 2:输出节点
        String type = MapUtils.getString(taskParams, "type", "");
        String writeModeType = MapUtils.getString(taskParams, "writeModeType", "");
        if (StringUtils.equals("1", type)) {
            writeMode = readerProperty.trainToJdbcWriteMode(null, writeModeType,writerProperty.getDbType());
        }
        if (StringUtils.equals("2", type)) {
            writeMode = readerProperty.trainToJdbcWriteMode(writeKeySet, writeModeType,writerProperty.getDbType());
            //当写入是全量时，则输入前置删除sql
            if (StringUtils.equals("1", writeModeType)) {
                preSql = readerProperty.trainToJdbcTruncateTable(writerProperty.getDbNameTableName(target_table_name));
            }
        }

        // 创建 job 相关的 content 配置
        List<Map<String, Object>> content = new ArrayList<>();
        Map<String, Object> contentItem = new HashMap<>();

        // reader 配置
        Map<String, Object> reader = new HashMap<>();
        reader.put("name", readerProperty.trainToJdbcReaderName());
        Map<String, Object> readerParameter = new HashMap<>();
        readerParameter.put("username", readerProperty.getUsername());
        readerParameter.put("password", readerProperty.getPassword());
        readerParameter.put("where", where);
        readerParameter.put("column", columns);
        readerParameter.put("splitPk", MapUtils.getString(taskParams, "readerSplitPk", ""));
        List<Map<String, Object>> readerConnection = new ArrayList<>();
        Map<String, Object> readerConnectionItem = new HashMap<>();
        readerConnectionItem.put("table", Arrays.asList(readerProperty.getDbNameTableName(table_name)));
        readerConnectionItem.put("jdbcUrl", Arrays.asList(readerProperty.trainToJdbcUrl()));
        readerConnection.add(readerConnectionItem);
        readerParameter.put("connection", readerConnection);
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
        if (StringUtils.isNotBlank(preSql)) {
            writerParameter.put("preSql", preSql.split(","));
        }
        if (StringUtils.isNotBlank(postSql)) {
            writerParameter.put("postSql", postSql.split(","));
        }
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
}
