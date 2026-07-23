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

package tech.qiantong.qdata.common.database.datasource;


import cn.hutool.db.ds.simple.SimpleDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;
import tech.qiantong.qdata.common.database.DataSourceFactory;
import tech.qiantong.qdata.common.database.DbDialect;
import tech.qiantong.qdata.common.database.DbQuery;
import tech.qiantong.qdata.common.database.DialectFactory;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.constants.DbType;
import tech.qiantong.qdata.common.database.exception.DataQueryException;
import tech.qiantong.qdata.common.database.query.AbstractDbQueryFactory;
import tech.qiantong.qdata.common.database.query.CacheDbQueryFactoryBean;

import javax.sql.DataSource;

public abstract class AbstractDataSourceFactory implements DataSourceFactory {

    @Override
    public DbQuery createDbQuery(DbQueryProperty property) {
        property.viald();
        DbType dbType = DbType.getDbType(property.getDbType());
        DataSource dataSource = null;
        //Judgment is not kafka
        if (!dbType.getDb().equals(DbType.KAFKA.getDb())) {
            dataSource = createDataSource(property);
        }
        DbQuery dbQuery = createDbQuery(property, dataSource, dbType);
        return dbQuery;
    }

    public DbQuery createDbQuery(DbQueryProperty dbQueryProperty, DataSource dataSource, DbType dbType) {
        DbDialect dbDialect = DialectFactory.getDialect(dbType);
        if (dbDialect == null) {
            throw new DataQueryException("db.error.datasource.dev", "This database type is under development");
        }
        AbstractDbQueryFactory dbQuery = new CacheDbQueryFactoryBean();
        dbQuery.setDbQueryProperty(dbQueryProperty);
        dbQuery.setDataSource(dataSource);
        dbQuery.setDbDialect(dbDialect);
        //Judgment is not kafka
        if (!dbType.getDb().equals(DbType.KAFKA.getDb())) {
            dbQuery.setJdbcTemplate(new JdbcTemplate(dataSource));
        }
        return dbQuery;
    }

    public DataSource createDataSource(DbQueryProperty property) {
        SimpleDataSource dataSource = null;
        if (DbType.SQL_SERVER2008.getDb().equals(property.getDbType()) && tech.qiantong.qdata.common.utils.StringUtils.startsWith(property.trainToJdbcUrl(), "jdbc:jtds:sqlserver")) {
            dataSource = new SimpleDataSource(property.trainToJdbcUrl(), property.getUsername(), property.getPassword(), "net.sourceforge.jtds.jdbc.Driver");
        } else {
            dataSource = new SimpleDataSource(property.trainToJdbcUrl(), property.getUsername(), property.getPassword());
        }
        return dataSource;
    }

    protected String trainToJdbcUrl(DbQueryProperty property) {
        String url = DbType.getDbType(property.getDbType()).getUrl();
        if (StringUtils.isEmpty(url)) {
            throw new DataQueryException("db.error.invalid.dbtype", "Invalid database type");
        }
        url = url.replace("${host}", property.getHost());
        url = url.replace("${port}", String.valueOf(property.getPort()));
        if (DbType.ORACLE.getDb().equals(property.getDbType()) || DbType.ORACLE_12C.getDb().equals(property.getDbType())) {
            url = url.replace("${sid}", property.getSid());
        } else if (!StringUtils.isEmpty(property.getDbName())) {
            url = url.replace("${dbName}", property.getDbName());
        }
        return url;
    }
}
