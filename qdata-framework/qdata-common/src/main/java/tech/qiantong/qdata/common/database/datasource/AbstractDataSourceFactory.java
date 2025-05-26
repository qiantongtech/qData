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
        SimpleDataSource dataSource = new SimpleDataSource(trainToJdbcUrl(property), property.getUsername(), property.getPassword());
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
