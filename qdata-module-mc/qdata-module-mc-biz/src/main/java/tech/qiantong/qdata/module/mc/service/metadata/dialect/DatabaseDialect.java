package tech.qiantong.qdata.module.mc.service.metadata.dialect;

import tech.qiantong.qdata.module.mc.dal.dataobject.metadata.McDbDO;

/**
 * Database dialect interface
 * Used to implement different metadata query logic according to different database types
 */
public interface DatabaseDialect {

    /**
     * Get database storage engine
     */
    String getStorageEngine(McDbDO mcDbDO);

    /**
     * Get the number of rows in the table
     */
    Long getTableRowCount(McDbDO mcDbDO, String tableName);

    /**
     * Get the index information of the table
     */
    String getTableIndexes(McDbDO mcDbDO, String tableName);

    /**
     * Get table partition field information
     */
    String getTablePartitionFields(McDbDO mcDbDO, String tableName);

    /**
     * Get the auto-increment information of a field
     */
    boolean isColumnAutoIncrement(McDbDO mcDbDO, String tableName, String columnName);

    /**
     * Get whether the field is a partition field
     */
    boolean isPartitionField(McDbDO mcDbDO, String tableName, String columnName);

    /**
     * Get database metadata information
     */
    DbMetadata getDbMetadata(McDbDO mcDbDO);

    /**
     * Get table metadata information in batches
     */
    TableMetadata getTableMetadata(McDbDO mcDbDO, String tableName);

    /**
     * Get field metadata information in batches
     */
    ColumnMetadata getColumnMetadata(McDbDO mcDbDO, String tableName, String columnName);

    /**
     * Database metadata information class
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
     * Table metadata information class
     */
    class TableMetadata {
        private Long rowCount;
        private String indexes;
        private String partitionFields;
        private Integer tableSize; // Table storage size (MB)
        private String storageEngine; // Storage engine
        private String tableComment; // Table annotation
        private String primaryKey; // Primary key field
        private String createTime; // Creation time
        private String updateTime; // Modification time

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
     * Field metadata information class
     */
    class ColumnMetadata {
        private boolean autoIncrement;
        private boolean partitionField;
        private boolean unique; // Is it unique?

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
