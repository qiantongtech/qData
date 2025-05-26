package tech.qiantong.qdata.module.dpp.utils.ds.component;

import org.apache.commons.collections4.MapUtils;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.utils.MD5Util;
import tech.qiantong.qdata.common.enums.TaskComponentTypeEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <P>
 * 用途:SQL-关系数据库 组件
 * </p>
 **/
public class SQLComponent implements ComponentItem {

    /**
     * taskParams SQL (关系数据库)
     * {
     * "localParams": [],//默认 []
     * "resourceList": [],//默认 []
     * "type":"MYSQL",//数据库类型 目前支持 MYSQL、ORACLE、DM8、KINGBASE
     * "sql":"",//sql语句
     * "sqlType":"",
     * "preStatements":[],//默认 []
     * "postStatements":[],//默认 []
     * "displayRows":10,//默认10
     * "datasources":{
     * "type":"MYSQL",//数据库类型 目前支持 MYSQL、ORACLE、DM8、KINGBASE
     * "host":"",//ip
     * "port":1521,//端口
     * "userName":"",//账号
     * "password":"",//密码
     * "database":""//数据库名
     * }
     * }
     *
     * @param params
     * @return
     */
    @Override
    public Map<String, Object> parse(Map<String, Object> params) {

        Map<String, Object> map = (Map<String, Object>) MapUtils.getObject(params, "datasources");
        DbQueryProperty dbQueryProperty = MD5Util.buildJobDatasource(map);

        Map<String, Object> taskParams = new LinkedHashMap<>();
        taskParams.put("localParams", params.getOrDefault("localParams", new ArrayList<>()));
        taskParams.put("resourceList", params.getOrDefault("resourceList", new ArrayList<>()));
        taskParams.put("type", MD5Util.getNormalizedDbType(dbQueryProperty.getDbType()));
        taskParams.put("sql", params.getOrDefault("sql", "")); // 默认空字符串
        taskParams.put("sqlType", params.getOrDefault("sqlType", "")); // 默认空字符串

        taskParams.put("preStatements", params.getOrDefault("preStatements", new ArrayList<>()));
        taskParams.put("postStatements", params.getOrDefault("postStatements", new ArrayList<>()));
        taskParams.put("displayRows", params.getOrDefault("displayRows", 10));

        Map<String, Object> datasources = new HashMap<>();
        datasources.put("type", MD5Util.getNormalizedDbType(dbQueryProperty.getDbType()));
        datasources.put("host", dbQueryProperty.getHost());
        datasources.put("port", dbQueryProperty.getPort());
        datasources.put("userName", dbQueryProperty.getUsername());
        datasources.put("password", dbQueryProperty.getPassword());
        datasources.put("database", MD5Util.wrapDsDatabaseParams(dbQueryProperty));

        datasources.put("connectType", MD5Util.wrapDsConnectTypeParams(dbQueryProperty));
        datasources.put("other", MD5Util.wrapOtherParams(dbQueryProperty));
        taskParams.put("datasources", datasources);
        return taskParams;
    }

    @Override
    public String code() {
        return TaskComponentTypeEnum.SQL_DEV.getCode();
    }
}
