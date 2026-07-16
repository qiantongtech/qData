/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
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
 * Purpose: Stored procedure
 * </p>
 **/
public class ProcedureComponent implements ComponentItem {

    /**
     *
     * taskParams PROCEDURE (stored procedure)
     * {
     *     "localParams": [], // Default []
     *     "resourceList": [], // Default []
     *     "type":"MYSQL", // Database type; currently supports MYSQL, ORACLE, DM8, KINGBASE
     *     "method":"call test(${in1},${out1});", // Call stored procedure
     *     "preStatements":[], // Default []
     *     "postStatements":[], // Default []
     *     "displayRows":10, // Default 10
     *     "datasources":{
     *         "type":"MYSQL", // Database type
     *         "host":"", // IP
     *         "port":1521, // Port
     *         "userName":"", // Account
     *         "password":"", // Password
     *         "database":"" // Database name
     *     }
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

        taskParams.put("method", params.getOrDefault("sql", "")); // Default empty string

        taskParams.put("preStatements", params.getOrDefault("preStatements", new ArrayList<>()));
        taskParams.put("postStatements", params.getOrDefault("postStatements", new ArrayList<>()));
        taskParams.put("displayRows", params.getOrDefault("displayRows", 10));

        Map<String, Object> datasources = new HashMap<>();
        datasources.put("type",MD5Util.getNormalizedDbType(dbQueryProperty.getDbType()) );
        datasources.put("host",dbQueryProperty.getHost() );
        datasources.put("port",dbQueryProperty.getPort() );
        datasources.put("userName",dbQueryProperty.getUsername() );
        datasources.put("password",dbQueryProperty.getPassword() );
        datasources.put("database",MD5Util.wrapDsDatabaseParams(dbQueryProperty) );

        datasources.put("connectType",MD5Util.wrapDsConnectTypeParams(dbQueryProperty) );
        datasources.put("other",MD5Util.wrapOtherParams(dbQueryProperty) );

        taskParams.put("datasources",datasources);

        return taskParams;
    }

    @Override
    public String code() {
        return TaskComponentTypeEnum.PROCEDURE_DEV.getCode();
    }
}
