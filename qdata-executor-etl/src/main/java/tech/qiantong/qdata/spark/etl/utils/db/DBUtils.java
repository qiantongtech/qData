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

package tech.qiantong.qdata.spark.etl.utils.db;

import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.constants.DbType;

import java.util.HashMap;
import java.util.Map;

/**
 * <P>
 * Purpose: Database related tools
 * </p>
 *
 * @author: FXB
 * @create: 2025-04-21 13:54
 **/
public class DBUtils {
    public static DbQueryProperty buildJobDatasource(Map<String, Object> datasource) {
        String ip = MapUtils.getString(datasource, "ip");
        long port = MapUtils.getLong(datasource, "port");
        String datasourceConfig = MapUtils.getString(datasource, "datasourceConfig");
        String datasourceType = MapUtils.getString(datasource, "datasourceType");

        DbQueryProperty dbQueryProperty = new DbQueryProperty(datasourceType, ip, port, datasourceConfig);
        return dbQueryProperty;
    }

    /**
     * Get database connection configuration
     */
    public static Map<String, String> getDbOptions(JSONObject parameter) {
        String datasourceId = parameter.getString("datasourceId");
        JSONObject connection = parameter.getJSONObject("connection");
        String jdbcUrlOld = connection.getString("jdbcUrl");
        String dbType = parameter.getString("dbType");

        Map<String, String> options = new HashMap<>();

        String jdbcUrl = jdbcUrlOld;
        String sid = parameter.getString("sid");
        String dbName = parameter.getString("dbName");
        String username = parameter.getString("username");
        String password = parameter.getString("password");

        if (StringUtils.indexOf(jdbcUrl, "?stringtype=unspecified") == -1
                && (StringUtils.equals(DbType.KINGBASE8.getDb(), dbType))) {
            options.put("url", jdbcUrl + "?stringtype=unspecified");
        } else {
            options.put("url", jdbcUrl);
        }
        //Register driver
        try {
            // Set connection parameters according to different database types
            switch (DbType.getDbType(dbType)) {
                case DM8:
                    Class.forName("dm.jdbc.driver.DmDriver");
                    options.put("driver", "dm.jdbc.driver.DmDriver");
                    break;
                case ORACLE:
                case ORACLE_12C:
                    Class.forName("oracle.jdbc.OracleDriver");
                    options.put("driver", "oracle.jdbc.OracleDriver");
                    break;
                case DORIS:
                case MYSQL:
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    options.put("driver", "com.mysql.cj.jdbc.Driver");
                    break;
                case KINGBASE8:
                    Class.forName("com.kingbase8.Driver");
                    options.put("driver", "com.kingbase8.Driver");
                    break;
                case SQL_SERVER2008:
                    if (StringUtils.startsWith(jdbcUrl, "jdbc:jtds:sqlserver")) {
                        Class.forName("net.sourceforge.jtds.jdbc.Driver");
                        options.put("driver", "net.sourceforge.jtds.jdbc.Driver");
                        break;
                    }
                case SQL_SERVER:
                    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                    options.put("driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
                    break;
                //Will be expanded later
                default:
                    throw new RuntimeException("Unsupported database type: " + dbType);
            }
        } catch (ClassNotFoundException e) {
        }
        options.put("user", username);
        options.put("password", password);
        options.put("dbName", dbName);
        if (connection.containsKey("table")) {
            //{\"username\":\"qdata_dev\",\"password\":\"2LKqLVMQ!xVDT$Qx\",\"dbname\":\"qdata_dev\",\"sid\":\"public\"}
            //Table query
            if (StringUtils.equals(DbType.KINGBASE8.getDb(), dbType)
                    || StringUtils.equals(DbType.SQL_SERVER.getDb(), dbType)
                    || StringUtils.equals(DbType.SQL_SERVER2008.getDb(), dbType)) {
                options.put("dbtable", dbName + "." + sid + "." + connection.getString("table"));
            } else if (StringUtils.isNotBlank(dbName)) {
                options.put("dbtable", dbName + "." + connection.getString("table"));
            } else {
                options.put("dbtable", connection.getString("table"));
            }
            options.put("tableName", connection.getString("table"));
        } else {
            //sql query
            options.put("query", connection.getString("querySql"));
        }
        return options;
    }

    public static void init() {
    }
}
