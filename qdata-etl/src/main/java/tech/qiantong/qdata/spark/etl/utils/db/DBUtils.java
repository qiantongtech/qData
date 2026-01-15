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
 * 用途:数据库相关工具类
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
     * 获取数据库连接配置
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
        //注册驱动
        try {
            // 根据不同数据库类型设置连接参数
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
                //后续再扩展
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
            //表查询
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
            //sql查询
            options.put("query", connection.getString("querySql"));
        }
        return options;
    }

    public static void init() {
    }
}
