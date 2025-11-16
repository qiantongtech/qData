/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

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
