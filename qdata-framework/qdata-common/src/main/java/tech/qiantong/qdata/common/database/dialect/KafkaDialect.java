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
 * For brand customization, please contact the official team for authorization.
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
 * 如需定制品牌，请通过官方渠道申请品牌授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.common.database.dialect;

import cn.hutool.core.lang.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.jdbc.core.RowMapper;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.database.core.DbTable;
import tech.qiantong.qdata.common.database.exception.DataQueryException;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * <P>
 * 用途:
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
    public Boolean validConnection(DataSource dataSource, DbQueryProperty dbQueryProperty) {
        Properties props = new Properties();
        props.put("bootstrap.servers", dbQueryProperty.getHost() + ":" + dbQueryProperty.getPort()); // Kafka 集群地址
        props.put("default.api.timeout.ms", 10000); // api请求超时时间
        props.put("request.timeout.ms", 10000); // 设置请求超时时间为10秒
        props.put("admin.request.timeout.ms", 10000); // 设置管理请求超时时间为10秒
        if (dbQueryProperty.getConfig() != null && !dbQueryProperty.getConfig().isEmpty()) {
            dbQueryProperty.getConfig().forEach((k, v) -> props.put(k, v));
        }
        String topic = TEST_TOPIC + "-" + UUID.randomUUID().toString();
        AdminClient admin = AdminClient.create(props);
        try {
            NewTopic newTopic = new NewTopic(topic, 1, (short) 1);
            //创建主题
            admin.createTopics(Collections.singleton(newTopic)).all().get();
            //删除主题
            admin.deleteTopics(Collections.singleton(topic)).all().get();
            return true;
        } catch (Exception e) {
            throw new DataQueryException("数据库连接失败,稍后重试");
        } finally {
            try {
                admin.close();
            } catch (Exception e) {
                throw new DataQueryException("关闭kafka连接出错");
            }
        }
    }

    @Override
    public String getInsertOrUpdateSql(String tableName, String where, String tableFieldName, String tableFieldValue, String setValue) {
        return null;
    }
}
