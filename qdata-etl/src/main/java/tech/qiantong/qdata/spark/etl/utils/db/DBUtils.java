package tech.qiantong.qdata.spark.etl.utils.db;

import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.collections4.MapUtils;
import org.apache.spark.sql.jdbc.JdbcDialects;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.constants.DbType;
import tech.qiantong.qdata.spark.etl.utils.db.spark.dialect.HiveSqlDialect;

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
        JSONObject connection = parameter.getJSONObject("connection");

        Map<String, String> options = new HashMap<>();
        //注册驱动
        try {
            // 根据不同数据库类型设置连接参数
            switch (DbType.getDbType(parameter.getString("dbType"))) {
                case DM8:
                    Class.forName("dm.jdbc.driver.DmDriver");
                    options.put("driver", "dm.jdbc.driver.DmDriver");
                    break;
                case ORACLE:
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
                //后续再扩展
                default:
                    throw new RuntimeException("Unsupported database type: " + parameter.getString("dbType"));
            }
        } catch (ClassNotFoundException e) {
        }
        options.put("url", connection.getString("jdbcUrl"));
        options.put("user", parameter.getString("username"));
        options.put("password", parameter.getString("password"));
        if (connection.containsKey("table")) {
            //表查询
            options.put("dbtable", connection.getString("table"));
        } else {
            //sql查询
            options.put("query", connection.getString("querySql"));
        }
        return options;
    }

    public static void init() {
        JdbcDialects.registerDialect(new HiveSqlDialect());
    }
}
