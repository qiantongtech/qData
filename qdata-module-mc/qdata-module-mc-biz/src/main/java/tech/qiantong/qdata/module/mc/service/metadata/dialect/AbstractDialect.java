package tech.qiantong.qdata.module.mc.service.metadata.dialect;

import lombok.extern.slf4j.Slf4j;
import tech.qiantong.qdata.module.mc.dal.dataobject.metadata.McDbDO;

/**
 * Abstract database dialect implementation
 * Implemented as a placeholder for other database types
 */
@Slf4j
public class AbstractDialect implements DatabaseDialect {

    @Override
    public String getStorageEngine(McDbDO mcDbDO) {
        log.info("获取数据库存储引擎（占位实现）");
        return null;
    }

    @Override
    public Long getTableRowCount(McDbDO mcDbDO, String tableName) {
        log.info("获取表 {} 的行数（占位实现）", tableName);
        return 0L;
    }

    @Override
    public String getTableIndexes(McDbDO mcDbDO, String tableName) {
        log.info("获取表 {} 的索引信息（占位实现）", tableName);
        return "";
    }

    @Override
    public String getTablePartitionFields(McDbDO mcDbDO, String tableName) {
        log.info("获取表 {} 的分区字段信息（占位实现）", tableName);
        return "";
    }

    @Override
    public boolean isColumnAutoIncrement(McDbDO mcDbDO, String tableName, String columnName) {
        log.info("获取表 {} 字段 {} 的自增信息（占位实现）", tableName, columnName);
        return false;
    }

    @Override
    public boolean isPartitionField(McDbDO mcDbDO, String tableName, String columnName) {
        log.info("判断表 {} 字段 {} 是否为分区字段（占位实现）", tableName, columnName);
        return false;
    }

    @Override
    public DbMetadata getDbMetadata(McDbDO mcDbDO) {
        return null;
    }

    @Override
    public TableMetadata getTableMetadata(McDbDO mcDbDO, String tableName) {
        log.info("批量获取表 {} 的元数据信息（占位实现）", tableName);
        TableMetadata metadata = new TableMetadata();
        metadata.setRowCount(getTableRowCount(mcDbDO, tableName));
        metadata.setIndexes(getTableIndexes(mcDbDO, tableName));
        metadata.setPartitionFields(getTablePartitionFields(mcDbDO, tableName));
        metadata.setTableSize(0); // Default is 0
        metadata.setStorageEngine(getStorageEngine(mcDbDO));
        return metadata;
    }

    @Override
    public ColumnMetadata getColumnMetadata(McDbDO mcDbDO, String tableName, String columnName) {
        log.info("批量获取字段 {} 的元数据信息（占位实现）", columnName);
        ColumnMetadata metadata = new ColumnMetadata();
        metadata.setAutoIncrement(isColumnAutoIncrement(mcDbDO, tableName, columnName));
        metadata.setPartitionField(isPartitionField(mcDbDO, tableName, columnName));
        return metadata;
    }
}
