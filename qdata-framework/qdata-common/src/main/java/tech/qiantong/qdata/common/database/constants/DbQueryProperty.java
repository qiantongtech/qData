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

package tech.qiantong.qdata.common.database.constants;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.common.database.DbDialect;
import tech.qiantong.qdata.common.database.DialectFactory;
import tech.qiantong.qdata.common.database.core.DbName;
import tech.qiantong.qdata.common.database.exception.DataQueryException;
import tech.qiantong.qdata.common.database.utils.AesEncryptUtil;

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
    //Kafka configuration or configuration needed when generating table SQL
    private Map<String, Object> config;

    /**
     * Datasource configuration
     */
    private Map<String, Object> datasourceConfig;

    /**
     * Constructor without decryption
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

    public DbQueryProperty copy() {
        DbQueryProperty p = new DbQueryProperty(
                this.dbType,
                this.host,
                this.username,
                this.password,
                this.port,
                this.dbName,
                this.sid
        );
        p.config = this.config;
        p.datasourceConfig = this.datasourceConfig;
        return p;
    }

    /**
     * Parameter validation
     */
    public void viald() {
        if (StringUtils.isBlank(dbType)) {
            throw new DataQueryException("db.error.params.incomplete", "Incomplete parameters");
        }
        DbType dbTypeEnum = DbType.getDbType(dbType);
        switch (dbTypeEnum) {
            case MYSQL:
            case ORACLE:
            case ORACLE_12C:
            case POSTGRE_SQL:
            case SQL_SERVER2008:
            case SQL_SERVER:
            case DM8:
            case KINGBASE8:
            case PHOENIX:
            case DORIS:
            case DB2:
            case OSCAR:
                if (StringUtils.isBlank(host)
                        || StringUtils.isBlank(username)
                        || StringUtils.isBlank(password)
                        || port == null) {
                    throw new DataQueryException("db.error.params.incomplete", "Incomplete parameters");
                }
                break;
            case REDIS:
                if (StringUtils.isBlank(host)
                        || port == null) {
                    throw new DataQueryException("db.error.params.incomplete", "Incomplete parameters");
                }
                break;
            case HIVE:
                if (StringUtils.isBlank(host) || port == null) {
                    throw new DataQueryException("db.error.params.incomplete", "Incomplete parameters");
                }
                break;
            case HDFS:
            case KAFKA:
            case RABBITMQ:
                if (StringUtils.isBlank(host) || port == null) {
                    throw new DataQueryException("db.error.params.incomplete", "Incomplete parameters");
                }
                break;
            case FTP:
                if (StringUtils.isAnyBlank(host, username, password) || port == null) {
                    throw new DataQueryException("db.error.params.incomplete", "Incomplete parameters");
                }
                break;
            case OSS_ALIYUN:
                if (datasourceConfig == null
                        || datasourceConfig.get("keyId") == null
                        || datasourceConfig.get("keySecret") == null
                        || datasourceConfig.get("bucket") == null
                        || datasourceConfig.get("endpoint") == null) {
                    throw new DataQueryException("db.error.params.incomplete", "Incomplete parameters");
                }
                break;
            case OTHER:
                throw new DataQueryException("db.error.unsupported.dbtype", "Unsupported database type");
        }
    }

    /**
     * @param datasourceType   type
     * @param ip               ip
     * @param port             port
     * @param datasourceConfig configuration info (JSON string)
     */
    public DbQueryProperty(String datasourceType, String ip, Long port, String datasourceConfig) {
        if (org.apache.commons.lang.StringUtils.isEmpty(datasourceType)) {
            throw new DataQueryException("db.error.datasource.type.empty", "Database type cannot be empty");
        }
        if (StringUtils.isEmpty(datasourceConfig)) {
            throw new DataQueryException("db.error.datasource.config.empty", "Datasource configuration cannot be empty");
        }
        if (DbType.getDbType(datasourceType) == null) {
            throw new DataQueryException("db.error.unsupported.dbtype", "Unsupported database type");
        }

        JSONObject configJson;
        try {
            configJson = JSON.parseObject(datasourceConfig);
        } catch (Exception e) {
            throw new DataQueryException("db.error.datasource.config.json", "Invalid datasource configuration format, must be valid JSON");
        }
        this.datasourceConfig = configJson;

        this.dbType = datasourceType;
        this.host = ip;
        if (port != null) {
            this.port = port.intValue();
        }

        this.username = configJson.getString("username");

        String passwordAes = configJson.getString("password");
        //Commercial release, temporarily commented out
        if (StringUtils.isNotBlank(passwordAes)) {
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

        if (StringUtils.equals(DbType.MONGODB.getDb(), dbType)) {
            this.sid = StringUtils.isNotEmpty(this.sid) ? this.sid : "admin";
        }

        if (!StringUtils.equals(DbType.KAFKA.getDb(), dbType) && !StringUtils.equals(DbType.HIVE.getDb(), dbType)
                && !StringUtils.equals(DbType.HDFS.getDb(), dbType)
                && !StringUtils.equals(DbType.REDIS.getDb(), dbType)
                && !StringUtils.equals(DbType.RABBITMQ.getDb(), dbType)
                && !StringUtils.equals(DbType.OSS_ALIYUN.getDb(), dbType)) {
            if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
                throw new DataQueryException("db.error.datasource.config.auth", "Datasource configuration must include username and password");
            }
        }
    }

    /**
     * Used to query information under dbName; some databases only support querying after switching connections
     *
     * @param dbNameVO
     */
    public void routeTo(DbName dbNameVO) {
        int level = dbNameVO == null ? 1 : dbNameVO.getLevel() + 1;
        if (level != 2) {
            return;
        }
        if (StringUtils.equals(DbType.KINGBASE8.getDb(), dbType)
                || StringUtils.equals(DbType.POSTGRE_SQL.getDb(), dbType)) {
            this.dbName = dbNameVO.getDbName();
            this.sid = "public";
        }
    }


    public String trainToJdbcUrl() {
        DbType dbType = DbType.getDbType(this.getDbType());
        if (dbType == null) {
            throw new DataQueryException("db.error.invalid.dbtype", "Invalid database type");
        }
        DbDialect dbDialect = DialectFactory.getDialect(dbType);
        return dbDialect.trainToJdbcUrl(this);
    }

    @Deprecated
    public String trainToJdbcWriterName() {
        if (DbType.ORACLE.getDb().equals(this.getDbType())) {
            return "oraclewriter"; // Oracle type returns "oraclewriter"
        } else if (DbType.MYSQL.getDb().equals(this.getDbType())) {
            return "mysqlwriter"; // MySQL type returns "mysqlwriter"
        } else if (DbType.POSTGRE_SQL.getDb().equals(this.getDbType())) {
            return "postgresqlwriter"; // PostgreSQL type returns "postgresqlwriter"
        } else if (DbType.SQL_SERVER.getDb().equals(this.getDbType())) {
            return "sqlserverwriter"; // SQLServer type returns "sqlserverwriter"
        } else if (DbType.DM8.getDb().equals(this.getDbType())) {
            return "rdbmswriter"; // DM8 type returns "rdbmswriter"
        } else if (DbType.KINGBASE8.getDb().equals(this.getDbType())) {
            return "kingbaseeswriter"; // KingbaseES type returns "kingbaseeswriter"
        } else {
            return "defaultwriter"; // Default returns "defaultwriter"
        }
    }

    @Deprecated
    public String trainToJdbcReaderName() {
        if (DbType.ORACLE.getDb().equals(this.getDbType())) {
            return "oraclereader"; // Oracle type returns "oraclereader"
        } else if (DbType.MYSQL.getDb().equals(this.getDbType())) {
            return "mysqlreader"; // MySQL type returns "mysqlreader"
        } else if (DbType.POSTGRE_SQL.getDb().equals(this.getDbType())) {
            return "postgresqlreader"; // PostgreSQL type returns "postgresqlreader"
        } else if (DbType.SQL_SERVER.getDb().equals(this.getDbType())) {
            return "sqlserverreader"; // SQLServer type returns "sqlserverreader"
        } else if (DbType.DM8.getDb().equals(this.getDbType())) {
            return "rdbmsreader"; // DM8 type returns "rdbmsreader"
        } else if (DbType.KINGBASE8.getDb().equals(this.getDbType())) {
            return "kingbaseesreader"; // KingbaseES type returns "kingbaseesreader"
        } else {
            return "defaultreader"; // Default returns "defaultreader"
        }
    }

    @Deprecated
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

    @Deprecated
    public String trainToJdbcWriteMode(Object columns, String writeModeType, String dbType) {
        // writeModeType: 1 full write, 2 incremental write, 3 update-or-insert write
        if ("1".equals(writeModeType) || "2".equals(writeModeType)) {
            return "insert"; // Full write or incremental write both use insert
        } else if ("3".equals(writeModeType)) {
            List<String> columnList = (List<String>) columns;
            if (CollectionUtils.isNotEmpty(columnList) && DbType.DM8.getDb().equals(dbType)) {
                // If columns is not empty, return update with field names
                return "update-dm (" + String.join(",", columnList) + ")";
            } else if (CollectionUtils.isNotEmpty(columnList)) {
                // If columns is not empty, return update with field names
                return "update (" + String.join(",", columnList) + ")";
            } else {
                // If columns is empty, return default update
                return "insert";
            }
        } else {
            return "insert"; // Invalid writeModeType
        }
    }

    @Deprecated
    public String trainToJdbcTruncateTable(String tableName) {
        // Get database type
        DbType dbTypeEnum = DbType.getDbType(dbType);

        // Validate whether the database type exists
        if (dbTypeEnum == null) {
            throw new DataQueryException("db.error.unsupported.dbtype", "Unsupported database type");
        }

        // Generate truncate table statement based on database type
        switch (dbTypeEnum) {
            case MYSQL:
            case MARIADB:
            case POSTGRE_SQL:
            case SQL_SERVER:
            case SQL_SERVER2008:
            case OTHER:
                return "DELETE FROM " + tableName + ""; // Generic truncate statement (MySQL, MariaDB, PostgreSQL, SQLServer, etc.)
            case ORACLE:
            case ORACLE_12C:
                return "DELETE FROM " + tableName + ""; // Oracle truncate statement (including CASCADE CONSTRAINTS)
            case DM8:
                return "DELETE FROM " + tableName + ""; // DM8 truncate statement
            case KINGBASE8:
                return "DELETE FROM " + tableName + ""; // KingbaseES truncate statement, may need RESTART IDENTITY (reset auto-increment fields)
            default:
                throw new DataQueryException("db.error.unsupported.dbtype", "Unsupported database type");
        }
    }

    public String trainToHostPort() {
        return host + ":" + port;
    }
}
