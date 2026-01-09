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
        //判断不为kafka
        if (!dbType.getDb().equals(DbType.KAFKA.getDb())) {
            dataSource = createDataSource(property);
        }
        DbQuery dbQuery = createDbQuery(property, dataSource, dbType);
        return dbQuery;
    }

    public DbQuery createDbQuery(DbQueryProperty dbQueryProperty, DataSource dataSource, DbType dbType) {
        DbDialect dbDialect = DialectFactory.getDialect(dbType);
        if (dbDialect == null) {
            throw new DataQueryException("该数据库类型正在开发中");
        }
        AbstractDbQueryFactory dbQuery = new CacheDbQueryFactoryBean();
        dbQuery.setDbQueryProperty(dbQueryProperty);
        dbQuery.setDataSource(dataSource);
        dbQuery.setDbDialect(dbDialect);
        //判断不为kafka
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
            throw new DataQueryException("无效数据库类型!");
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
