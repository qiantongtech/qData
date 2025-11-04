package tech.qiantong.qdata.spark.etl.utils.db;

import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.sql.jdbc.JdbcDialects;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.constants.DbType;
import tech.qiantong.qdata.spark.etl.utils.RedisUtils;

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

        if (jdbcUrlOld != null && !"".equals(jdbcUrlOld) && datasourceId == null) {
            datasourceId = RedisUtils.hget("datasource-old", jdbcUrlOld);
        }

        Map<String, String> options = new HashMap<>();
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
                case MYSQL:
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    options.put("driver", "com.mysql.cj.jdbc.Driver");
                    break;
                case KINGBASE8:
                    Class.forName("com.kingbase8.Driver");
                    options.put("driver", "com.kingbase8.Driver");
                    break;
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


        String jdbcUrl = jdbcUrlOld;
        String sid = parameter.getString("sid");
        String dbName = parameter.getString("dbName");
        String username = parameter.getString("username");
        String password = parameter.getString("password");

        String datasource = RedisUtils.hget("datasource", datasourceId);
        // 替换存储在 redis 中最新的数据源连接信息
        if (datasource != null && !"".equals(datasource)) {
            DbQueryProperty dbQueryProperty = JSONObject.parseObject(datasource, DbQueryProperty.class);
            dbType = dbQueryProperty.getDbType();
            jdbcUrl = dbQueryProperty.trainToJdbcUrl();
            sid = dbQueryProperty.getSid();
            dbName = dbQueryProperty.getDbName();
            username = dbQueryProperty.getUsername();
            password = dbQueryProperty.getPassword();
        }

        if (StringUtils.indexOf(jdbcUrl, "?stringtype=unspecified") == -1
                && (StringUtils.equals(DbType.KINGBASE8.getDb(), dbType))) {
            options.put("url", jdbcUrl + "?stringtype=unspecified");
        } else {
            options.put("url", jdbcUrl);
        }
        options.put("user", username);
        options.put("password", password);
        options.put("dbName", dbName);
        if (connection.containsKey("table")) {
            //{\"username\":\"qdata_dev\",\"password\":\"2LKqLVMQ!xVDT$Qx\",\"dbname\":\"qdata_dev\",\"sid\":\"public\"}
            //表查询
            if (StringUtils.equals(DbType.KINGBASE8.getDb(), dbType) ||
                    StringUtils.equals(DbType.SQL_SERVER.getDb(), dbType)) {
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
