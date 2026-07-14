package tech.qiantong.qdata.datax;

import org.apache.logging.log4j.util.Strings;
import tech.qiantong.qdata.common.database.constants.DbType;
import tech.qiantong.qdata.common.database.utils.AesEncryptUtil;
import tech.qiantong.qdata.common.utils.JSONUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DataX job.json 生成工具类。
 */
public final class DataXJsonBuilder {

    private DataXJsonBuilder() {
    }

    public static String buildJson(Map<String, Object> readerNodeJsonMap,
                                   Map<String, Object> writerNodeJsonMap,
                                   Map<String, Object> definitionJsonMap) {
        List<Map<String, Object>> nodeList = new ArrayList<>();
        nodeList.add(buildContent(readerNodeJsonMap, writerNodeJsonMap, definitionJsonMap));
        Map<String, Object> jobJsonMap = new HashMap<>();
        jobJsonMap.put("job", new HashMap<String, Object>() {{
            put("content", nodeList);
            put("setting", new HashMap<String, Object>() {{
                put("speed", new HashMap<String, Object>() {{
                    put("channel", 1);
                }});
            }});
        }});
        return JSONUtils.toJson(jobJsonMap);
    }

    private static Map<String, Object> buildContent(Map<String, Object> readerNodeJsonMap,
                                                    Map<String, Object> writerNodeJsonMap,
                                                    Map<String, Object> definitionJsonMap) {
        Map<String, Object> content = new HashMap<>();
        content.put("reader", buildReader(readerNodeJsonMap,writerNodeJsonMap));
        content.put("writer", buildWriter(writerNodeJsonMap));
        if (!definitionJsonMap.isEmpty()) {
            content.put("processor", buildProcessor(definitionJsonMap));
        }
        return content;
    }

    /**
     * 构建 DataX reader 节点配置，负责源表、源字段和源数据源连接信息。
     */
    private static Map<String, Object> buildReader(Map<String, Object> readerNodeJsonMap, Map<String, Object> writerNodeJsonMap) {
        Map<String, Object> readerDatasource = parseDatasource(readerNodeJsonMap, "readerDatasource");
        Map<String, Object> reader = new HashMap<>();
        reader.put("name", "rdbmsreader");
        reader.put("parameter", buildParameter(readerNodeJsonMap, writerNodeJsonMap, readerDatasource,
                "columns", "table_name", true));
        return reader;
    }

    /**
     * 构建 DataX writer 节点配置，负责目标表、目标字段和目标数据源连接信息。
     */
    private static Map<String, Object> buildWriter(Map<String, Object> writerNodeJsonMap) {
        Map<String, Object> writerDatasource = parseDatasource(writerNodeJsonMap, "writerDatasource");
        Map<String, Object> writer = new HashMap<>();
        writer.put("name", "rdbmswriter");
        writer.put("parameter", buildParameter(writerNodeJsonMap, writerNodeJsonMap, writerDatasource,
                "target_columns", "target_table_name", false));
        return writer;
    }

    /**
     * 组装 reader/writer 通用参数，保留 DataX 两端一致的字段、SQL、账号和连接结构。
     */
    private static Map<String, Object> buildParameter(Map<String, Object> nodeJsonMap,
                                                      Map<String, Object> columnNodeJsonMap,
                                                      Map<String, Object> datasource,
                                                      String columnKey,
                                                      String tableKey,
                                                      boolean reader) {
        Map<String, Object> datasourceConfig = JSONUtils.convertTaskDefinitionJsonMap(String.valueOf(datasource.get("datasourceConfig")));
        Map<String, Object> parameter = new HashMap<>();
        putSqlParameter(parameter, nodeJsonMap, "where", "string");
        putSqlParameter(parameter, nodeJsonMap, "preSql", "list");
        putSqlParameter(parameter, nodeJsonMap, "postSql", "list");
        parameter.put("column", columnNodeJsonMap.get(columnKey));
        Object object = nodeJsonMap.get("writeModeType");
        if (object != null) {
            if (object.toString().equals("3")) {
                parameter.put("writeMode", "dppUpdate");
                parameter.put("selectedColumns", nodeJsonMap.get("selectedColumns"));
            } else if (object.toString().equals("1")) {
                parameter.put("writeMode", "dppReplace");
            }
        }
        parameter.put("batchSize", nodeJsonMap.get("batchSize"));
        Object description = nodeJsonMap.get("description");
        if (description != null) {
            parameter.put("batchSize", description);
        }
        parameter.put("batchSize", nodeJsonMap.get("description"));
        parameter.put("username", datasourceConfig.get("username"));
        parameter.put("password", decryptPassword(datasourceConfig));
        parameter.put("connection", buildConnection(nodeJsonMap.get(tableKey).toString(), datasource, reader));
        return parameter;
    }

    /**
     * where、preSql、postSql 为空时不写入，避免生成无意义的 DataX 参数。
     * @param type 数据类型："string" 或 "list"
     */
    private static void putSqlParameter(Map<String, Object> parameter, Map<String, Object> nodeJsonMap, String key, String type) {
        Object object = nodeJsonMap.get(key);
        if (object == null) {
            return;
        }
        String value = String.valueOf(object);
        if (Strings.isNotBlank(value)) {
            if ("list".equals(type)) {
                parameter.put(key, new ArrayList<Object>() {{
                    add(value);
                }});
            } else {
                parameter.put(key, value);
            }
        }
    }

    /**
     * 节点内的数据源以 JSON 字符串保存，这里统一转成 Map 供后续构建连接信息。
     */
    private static Map<String, Object> parseDatasource(Map<String, Object> nodeJsonMap, String datasourceKey) {
        return JSONUtils.convertTaskDefinitionJsonMap(String.valueOf(nodeJsonMap.get(datasourceKey)));
    }

    /**
     * 兼容明文和加密密码：解密失败时沿用原值。
     */
    private static String decryptPassword(Map<String, Object> datasourceConfig) {
        String password = datasourceConfig.get("password").toString();
        try {
            password = AesEncryptUtil.desEncrypt(datasourceConfig.get("password").toString()).trim();
        } catch (Exception ignored) {
        }
        return password;
    }

    /**
     * 构建 DataX connection 配置；reader 的 jdbcUrl 使用列表，writer 沿用原字符串结构。
     */
    private static List<Map<String, Object>> buildConnection(String tableName,
                                                             Map<String, Object> datasource,
                                                             boolean reader) {
        List<Map<String, Object>> connections = new ArrayList<>();
        Map<String, Object> connection = new HashMap<>();
        List<String> tables = new ArrayList<>();
        tables.add(tableName);
        connection.put("table", tables);
        if (reader) {
            List<String> jdbcUrls = new ArrayList<>();
            jdbcUrls.add(buildJdbcUrl(datasource));
            connection.put("jdbcUrl", jdbcUrls);
        } else {
            connection.put("jdbcUrl", buildJdbcUrl(datasource));
        }
        connections.add(connection);
        return connections;
    }

    /**
     * 数据去重等处理节点配置会写入 processor.nodes。
     */
    private static Map<String, Object> buildProcessor(Map<String, Object> definitionJsonMap) {
        Map<String, Object> processor = new HashMap<>();
        processor.put("nodes", definitionJsonMap.get("tableFields"));
        return processor;
    }

    /**
     * 根据数据源类型模板生成 JDBC URL，并替换前端保存的数据源连接参数。
     */
    private static String buildJdbcUrl(Map<String, Object> datasource) {
        String datasourceType = String.valueOf(datasource.get("datasourceType"));
        String urlTemplate = DbType.getDbType(datasourceType).getUrl();
        return urlTemplate
                .replace("${host}", valueOf(datasource.get("ip")))
                .replace("${port}", valueOf(datasource.get("port")))
                .replace("${dbName}", valueOf(datasource.get("dbname")))
                .replace("${sid}", valueOf(datasource.get("sid")));
    }

    private static String valueOf(Object value) {
        return value == null ? "" : String.valueOf(value);
    }
}
