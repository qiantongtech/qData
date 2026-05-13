package tech.qiantong.qdata.module.mc.service.metadata.dialect;

import tech.qiantong.qdata.module.mc.dal.dataobject.metadata.McDbDO;

/**
 * 数据库方言接口
 * 用于根据不同数据库类型实现不同的元数据查询逻辑
 */
public interface DatabaseDialect {

    /**
     * 获取数据库存储引擎
     */
    String getStorageEngine(McDbDO mcDbDO);

    /**
     * 获取表的行数
     */
    Long getTableRowCount(McDbDO mcDbDO, String tableName);

    /**
     * 获取表的索引信息
     */
    String getTableIndexes(McDbDO mcDbDO, String tableName);

    /**
     * 获取表的分区字段信息
     */
    String getTablePartitionFields(McDbDO mcDbDO, String tableName);

    /**
     * 获取字段的自增信息
     */
    boolean isColumnAutoIncrement(McDbDO mcDbDO, String tableName, String columnName);

    /**
     * 获取字段是否为分区字段
     */
    boolean isPartitionField(McDbDO mcDbDO, String tableName, String columnName);

    /**
     * 获取数据库元数据信息
     */
    DbMetadata getDbMetadata(McDbDO mcDbDO);

    /**
     * 批量获取表的元数据信息
     */
    TableMetadata getTableMetadata(McDbDO mcDbDO, String tableName);

    /**
     * 批量获取字段的元数据信息
     */
    ColumnMetadata getColumnMetadata(McDbDO mcDbDO, String tableName, String columnName);

    /**
     * 数据库元数据信息类
     */
    class DbMetadata {
        private Integer storageSize;

        public Integer getStorageSize() {
            return storageSize;
        }

        public void setStorageSize(Integer storageSize) {
            this.storageSize = storageSize;
        }
    }


    /**
     * 表元数据信息类
     */
    class TableMetadata {
        private Long rowCount;
        private String indexes;
        private String partitionFields;
        private Integer tableSize; // 表存储大小（MB）
        private String storageEngine; // 存储引擎
        private String tableComment; // 表注释
        private String primaryKey; // 主键字段
        private String createTime; // 创建时间
        private String updateTime; // 修改时间

        public Long getRowCount() {
            return rowCount;
        }

        public void setRowCount(Long rowCount) {
            this.rowCount = rowCount;
        }

        public String getIndexes() {
            return indexes;
        }

        public void setIndexes(String indexes) {
            this.indexes = indexes;
        }

        public String getPartitionFields() {
            return partitionFields;
        }

        public void setPartitionFields(String partitionFields) {
            this.partitionFields = partitionFields;
        }

        public Integer getTableSize() {
            return tableSize;
        }

        public void setTableSize(Integer tableSize) {
            this.tableSize = tableSize;
        }

        public String getStorageEngine() {
            return storageEngine;
        }

        public void setStorageEngine(String storageEngine) {
            this.storageEngine = storageEngine;
        }
        public String getTableComment() {
            return tableComment;
        }

        public void setTableComment(String tableComment) {
            this.tableComment = tableComment;
        }

        public String getPrimaryKey() {
            return primaryKey;
        }

        public void setPrimaryKey(String primaryKey) {
            this.primaryKey = primaryKey;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
    }

    /**
     * 字段元数据信息类
     */
    class ColumnMetadata {
        private boolean autoIncrement;
        private boolean partitionField;
        private boolean unique; // 是否唯一

        public boolean isAutoIncrement() {
            return autoIncrement;
        }

        public void setAutoIncrement(boolean autoIncrement) {
            this.autoIncrement = autoIncrement;
        }

        public boolean isPartitionField() {
            return partitionField;
        }

        public void setPartitionField(boolean partitionField) {
            this.partitionField = partitionField;
        }

        public boolean isUnique() {
            return unique;
        }

        public void setUnique(boolean unique) {
            this.unique = unique;
        }
    }
}
