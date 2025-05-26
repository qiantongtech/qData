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
