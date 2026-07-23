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

package tech.qiantong.qdata.common.database.dialect;

import cn.hutool.core.lang.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.jdbc.core.RowMapper;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.database.core.DbName;
import tech.qiantong.qdata.common.database.core.DbTable;
import tech.qiantong.qdata.common.database.exception.DataQueryException;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * <P>
 * Purpose:
 * </p>
 *
 * @author: FXB
 * @create: 2025-03-20 16:44
 **/
@Slf4j
public class KafkaDialect extends AbstractDbDialect {

    private static final String TEST_TOPIC = "test-connection-topic";

    @Override
    public RowMapper<DbTable> tableMapper() {
        return null;
    }

    @Override
    public RowMapper<DbColumn> columnMapper() {
        return null;
    }

    @Override
    public String columns(DbQueryProperty dbQueryProperty, String tableName) {
        return null;
    }

    @Override
    public String generateCheckTableExistsSQL(DbQueryProperty dbQueryProperty, String tableName) {
        return null;
    }

    @Override
    public List<String> someInternalSqlGenerator(DbQueryProperty dbQueryProperty, String tableName, String tableComment, List<DbColumn> dbColumnList) {
        return null;
    }

    @Override
    public List<String> validateSpecification(String tableName, String tableComment, List<DbColumn> columns) {
        return null;
    }

    @Override
    public String tables(DbQueryProperty dbQueryProperty) {
        return null;
    }

    @Override
    public String buildQuerySqlFields(List<DbColumn> columns, String tableName, DbQueryProperty dbQueryProperty) {
        return null;
    }

    @Override
    public String getDataStorageSize(String dbName) {
        return null;
    }

    @Override
    public String getDbName() {
        return null;
    }

    @Override
    public String getDbName(DbName dbName) {
        return null;
    }

    @Override
    public Boolean validConnection(DataSource dataSource, DbQueryProperty dbQueryProperty) {
        Properties props = new Properties();
        props.put("bootstrap.servers", dbQueryProperty.getHost() + ":" + dbQueryProperty.getPort()); // Kafka cluster address
        props.put("default.api.timeout.ms", 10000); // api request timeout
        props.put("request.timeout.ms", 10000); // Set the request timeout to 10 seconds
        props.put("admin.request.timeout.ms", 10000); // Set the management request timeout to 10 seconds
        if (dbQueryProperty.getConfig() != null && !dbQueryProperty.getConfig().isEmpty()) {
            dbQueryProperty.getConfig().forEach((k, v) -> props.put(k, v));
        }
        String topic = TEST_TOPIC + "-" + UUID.randomUUID().toString();
        AdminClient admin = AdminClient.create(props);
        try {
            NewTopic newTopic = new NewTopic(topic, 1, (short) 1);
            //Create topic
            admin.createTopics(Collections.singleton(newTopic)).all().get();
            //Delete topic
            admin.deleteTopics(Collections.singleton(topic)).all().get();
            return true;
        } catch (Exception e) {
            throw new DataQueryException("db.error.connection.retry", "Database connection failed, please try again later");
        } finally {
            try {
                admin.close();
            } catch (Exception e) {
                throw new DataQueryException("db.error.close.kafka", "Failed to close Kafka connection");
            }
        }
    }

    @Override
    public String getInsertOrUpdateSql(String tableName, String where, String tableFieldName, String tableFieldValue, String setValue) {
        return null;
    }
}
