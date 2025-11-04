package tech.qiantong.qdata.common.database.constants;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.common.database.exception.DataQueryException;
import tech.qiantong.qdata.common.database.utils.AesEncryptUtil;
import com.alibaba.fastjson2.annotation.JSONField;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class DbQueryProperty implements Serializable {

    private static final long serialVersionUID = 1L;

    @JSONField(alternateNames = {"type"})
    private String dbType;
    private String host;
    private String username;
    private String password;
    private Integer port;
    private String dbName;
    private String sid;
    //kafka配置或生成表sql时所需的配置
    private Map<String, Object> config;

    /**
     * 配置
     */
    private Map<String, Object> datasourceConfig;

    /**
     * 不解密的构造方法
     *
     * @param dbType
     * @param host
     * @param username
     * @param password
     * @param port
     * @param dbName
     * @param sid
     */
    public DbQueryProperty(String dbType, String host, String username, String password, Integer port, String dbName, String sid) {
        this.dbType = dbType;
        this.host = host;
        this.username = username;
        this.password = password;
        this.port = port;
        this.dbName = dbName;
        this.sid = sid;
    }

    /**
     * 参数合法性校验
     */
    public void viald() {
        if (StringUtils.isBlank(dbType)) {
            throw new DataQueryException("参数不完整");
        }
        DbType dbTypeEnum = DbType.getDbType(dbType);
        switch (dbTypeEnum) {
            case MYSQL:
            case ORACLE:
            case ORACLE_12C:
            case SQL_SERVER2008:
            case SQL_SERVER:
            case DM8:
            case KINGBASE8:
                if (StringUtils.isBlank(host)
                        || StringUtils.isBlank(username)
                        || StringUtils.isBlank(password)
                        || port == null) {
                    throw new DataQueryException("参数不完整");
                }
                break;
            case OTHER:
                throw new DataQueryException("不支持的数据库类型");
        }
    }

    /**
     * @param datasourceType   类型
     * @param ip               ip
     * @param port             端口
     * @param datasourceConfig 配置信息（JSON字符串）
     */
    public DbQueryProperty(String datasourceType, String ip, Long port, String datasourceConfig) {
        if (org.apache.commons.lang.StringUtils.isEmpty(datasourceType)) {
            throw new DataQueryException("数据库类型不能为空");
        }
        if (StringUtils.isEmpty(datasourceConfig)) {
            throw new DataQueryException("数据源配置不能为空");
        }
        if (DbType.getDbType(datasourceType) == null) {
            throw new DataQueryException("不支持的数据库类型");
        }

        JSONObject configJson;
        try {
            configJson = JSON.parseObject(datasourceConfig);
        } catch (Exception e) {
            throw new DataQueryException("数据源配置格式错误，应为合法的 JSON");
        }
        this.datasourceConfig = configJson;

        this.dbType = datasourceType;
        this.host = ip;
        if (port != null) {
            this.port = port.intValue();
        }

        this.username = configJson.getString("username");

        String passwordAes = configJson.getString("password");
        //发布商业版，临时注释
        if(StringUtils.isNotBlank(passwordAes)){
            try {
                this.password = AesEncryptUtil.desEncrypt(configJson.getString("password")).trim();
            } catch (Exception e) {
                this.password = configJson.getString("password");
            }
        }
//        this.password = passwordAes;
        this.sid = configJson.getString("sid");
        this.dbName = configJson.getString("dbname");
        String config = configJson.getString("config");
        if (StringUtils.isNotBlank(config)) {
            this.config = JSONObject.parseObject(config);
        }

        if (!StringUtils.equals(DbType.KAFKA.getDb(), dbType)) {
            if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
                throw new DataQueryException("数据源配置中必须包含 username、password");
            }
        }
    }

    public String trainToJdbcUrl() {
        String url = DbType.getDbType(this.getDbType()).getUrl();
        if (StringUtils.isEmpty(url)) {
            throw new DataQueryException("无效数据库类型!");
        }
        url = url.replace("${host}", this.getHost());
        url = url.replace("${port}", String.valueOf(this.getPort()));
        if (DbType.ORACLE.getDb().equals(this.getDbType()) || DbType.ORACLE_12C.getDb().equals(this.getDbType())) {
            url = url.replace("${sid}", this.getSid());
        } else {
            url = url.replace("${dbName}", this.getDbName());
        }
        return url;
    }

    public String trainToJdbcWriterName() {
        if (DbType.ORACLE.getDb().equals(this.getDbType())) {
            return "oraclewriter"; // Oracle 类型返回 "oraclewriter"
        } else if (DbType.MYSQL.getDb().equals(this.getDbType())) {
            return "mysqlwriter"; // MySQL 类型返回 "mysqlwriter"
        } else if (DbType.POSTGRE_SQL.getDb().equals(this.getDbType())) {
            return "postgresqlwriter"; // PostgreSQL 类型返回 "postgresqlwriter"
        } else if (DbType.SQL_SERVER.getDb().equals(this.getDbType())) {
            return "sqlserverwriter"; // SQLServer 类型返回 "sqlserverwriter"
        } else if (DbType.DM8.getDb().equals(this.getDbType())) {
            return "rdbmswriter"; // 达梦8 类型返回 "rdbmswriter"
        } else if (DbType.KINGBASE8.getDb().equals(this.getDbType())) {
            return "kingbaseeswriter"; // 人大金仓 类型返回 "rdbmswriter"
        } else {
            return "defaultwriter"; // 默认返回 "defaultwriter"
        }
    }

    public String trainToJdbcReaderName() {
        if (DbType.ORACLE.getDb().equals(this.getDbType())) {
            return "oraclereader"; // Oracle 类型返回 "oraclewriter"
        } else if (DbType.MYSQL.getDb().equals(this.getDbType())) {
            return "mysqlreader"; // MySQL 类型返回 "mysqlwriter"
        } else if (DbType.POSTGRE_SQL.getDb().equals(this.getDbType())) {
            return "postgresqlreader"; // PostgreSQL 类型返回 "postgresqlwriter"
        } else if (DbType.SQL_SERVER.getDb().equals(this.getDbType())) {
            return "sqlserverreader"; // SQLServer 类型返回 "sqlserverwriter"
        } else if (DbType.DM8.getDb().equals(this.getDbType())) {
            return "rdbmsreader"; // 达梦8 类型返回 "rdbmswriter"
        } else if (DbType.KINGBASE8.getDb().equals(this.getDbType())) {
            return "kingbaseesreader"; // 人大金仓 类型返回 "rdbmswriter"
        } else {
            return "defaultreader"; // 默认返回 "defaultwriter"
        }
    }

    public String getDbNameTableName(String tableName) {
        if (DbType.ORACLE.getDb().equals(this.getDbType())) {
            return this.dbName + "." + tableName;
        } else if (DbType.MYSQL.getDb().equals(this.getDbType())) {
            return tableName;
        } else if (DbType.POSTGRE_SQL.getDb().equals(this.getDbType())) {
            return tableName;
        } else if (DbType.KINGBASE8.getDb().equals(this.getDbType())) {
//            return this.sid + "." +  tableName;
            return tableName;
        } else if (DbType.SQL_SERVER.getDb().equals(this.getDbType())) {
            return tableName;
        } else if (DbType.DM8.getDb().equals(this.getDbType())) {
            return this.dbName + "." + tableName;
        } else {
            return tableName;
        }
    }


    public String trainToJdbcWriteMode(Object columns, String writeModeType, String dbType) {
        // writeModeType: 1 全量写，2 增量写，3 增更写
        if ("1".equals(writeModeType) || "2".equals(writeModeType)) {
            return "insert"; // 全量写 或 增量写 都是 insert
        } else if ("3".equals(writeModeType)) {
            List<String> columnList = (List<String>) columns;
            if (CollectionUtils.isNotEmpty(columnList) && DbType.DM8.getDb().equals(dbType)) {
                // 如果columns不为空，则返回update并包含字段名
                return "update-dm (" + String.join(",", columnList) + ")";
            } else if (CollectionUtils.isNotEmpty(columnList)) {
                // 如果columns不为空，则返回update并包含字段名
                return "update (" + String.join(",", columnList) + ")";
            } else {
                // 如果columns为空，则返回默认的update
                return "insert";
            }
        } else {
            return "insert"; // 无效的 writeModeType
        }
    }

    public String trainToJdbcTruncateTable(String tableName) {
        // 获取数据库类型
        DbType dbTypeEnum = DbType.getDbType(dbType);

        // 校验数据库类型是否存在
        if (dbTypeEnum == null) {
            throw new DataQueryException("不支持的数据库类型");
        }

        // 根据数据库类型生成清空表语句
        switch (dbTypeEnum) {
            case MYSQL:
            case MARIADB:
            case POSTGRE_SQL:
            case SQL_SERVER:
            case SQL_SERVER2008:
            case OTHER:
                return "DELETE FROM " + tableName + ""; // 通用的清空表语句（MySQL, MariaDB, PostgreSQL, SQLServer, 等等）
            case ORACLE:
            case ORACLE_12C:
                return "DELETE FROM " + tableName + ""; // Oracle 数据库的 TRUNCATE 语句（包括 CASCADE CONSTRAINTS）
            case DM8:
                return "DELETE FROM " + tableName + ""; // 达梦8的清空表语句
            case KINGBASE8:
                return "DELETE FROM " + tableName + ""; // 人大金仓数据库的 TRUNCATE 语句，可能需要加上 RESTART IDENTITY（清空自增字段）
            default:
                throw new DataQueryException("不支持的数据库类型");
        }
    }
}
