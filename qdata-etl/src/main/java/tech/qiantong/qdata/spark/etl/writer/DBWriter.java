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

package tech.qiantong.qdata.spark.etl.writer;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import tech.qiantong.qdata.common.database.DbQuery;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.constants.DbType;
import tech.qiantong.qdata.common.database.datasource.AbstractDataSourceFactory;
import tech.qiantong.qdata.common.database.datasource.DefaultDataSourceFactoryBean;
import tech.qiantong.qdata.common.enums.TaskComponentTypeEnum;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.spark.etl.utils.LogUtils;
import tech.qiantong.qdata.spark.etl.utils.db.DBUtils;
import tech.qiantong.qdata.spark.etl.utils.db.element.*;
import tech.qiantong.qdata.spark.etl.utils.db.exception.DBException;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.alibaba.fastjson2.JSONWriter.Feature.PrettyFormat;

/**
 * <P>
 * Purpose: database output
 * </p>
 *
 * @author: FXB
 * @create: 2025-04-25 09:35
 **/
@Slf4j
public class DBWriter implements Writer {

    AbstractDataSourceFactory dataSourceFactory = new DefaultDataSourceFactoryBean();

    @Override
    public Boolean writer(JSONObject config, Dataset<Row> dataset, JSONObject writer, LogUtils.Params logParams) {
        LogUtils.writeLog(logParams, "*********************************  Initialize task context  ***********************************");
        LogUtils.writeLog(logParams, MessageUtils.messageEn("etl.writer.db.start"));
        LogUtils.writeLog(logParams, MessageUtils.messageEn("etl.task.start.time", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS")));
        LogUtils.writeLog(logParams, MessageUtils.messageEn("etl.task.parameters", writer.toJSONString(PrettyFormat)));
        JSONObject parameter = writer.getJSONObject("parameter");
        //Encapsulate read information
        Map<String, String> writerOptions = DBUtils.getDbOptions(parameter);

        //Pre-sql
        Integer batchSize = parameter.getInteger("batchSize");

        //Pre-sql
        List<Object> preSql = parameter.getJSONArray("preSql");

        //Input field
        List<Object> column = parameter.getJSONArray("column");
        List<String> readerColumns = column.stream().map(c -> (String) c).collect(Collectors.toList());
        //Output fields
        List<Object> targetColumn = parameter.getJSONArray("target_column");


        //Post sql
        List<Object> postSql = parameter.getJSONArray("postSql");
        //Write type 1 full, 2 append write, 3 incremental update
        Integer writeModeType = parameter.getInteger("writeModeType");
        String writeMode = null;
        switch (writeModeType) {
            case 1:
                writeMode = MessageUtils.messageEn("etl.writer.mode.full");
                break;
            case 2:
                writeMode = MessageUtils.messageEn("etl.writer.mode.append");
                break;
            case 3:
                writeMode = MessageUtils.messageEn("etl.writer.mode.incremental");
                break;
            default:
                writeMode = "";
                break;
        }
        LogUtils.writeLog(logParams, MessageUtils.messageEn("etl.writer.mode.info", writeMode));
        //Incremental update of primary key
        List<Object> selectedColumns = parameter.getJSONArray("selectedColumns");
        //Target table name
        String tableName = parameter.getJSONObject("connection").getString("table");
        String tmpTableName = null;
        if (writeModeType == 1) {
            tmpTableName = tableName + "_" + DateUtil.format(new Date(), "yyyyMMddHHmmss");
            writerOptions.put("dbtable", tmpTableName);
        }
        DbQueryProperty writerProperty = JSONObject.parseObject(parameter.getJSONObject("writerProperty").toString(), DbQueryProperty.class);

        //Create connection
        DbQuery dbQuery = dataSourceFactory.createDbQuery(writerProperty);
        log.info(JSON.toJSONString(writerProperty));
        if (!dbQuery.valid()) {
            log.info(JSON.toJSONString(writerProperty));
            LogUtils.writeLog(logParams, MessageUtils.messageEn("etl.writer.connection.failed", JSON.toJSONString(writerProperty)));
            return false;
        }
        Boolean success = true;
        try {
            //Create temporary table
            if (StringUtils.isNotBlank(tmpTableName)) {
                LogUtils.writeLog(logParams, MessageUtils.messageEn("etl.writer.create.temp.table", tmpTableName));
                log.info(MessageUtils.message("etl.writer.create.temp.table", tmpTableName));
                if (!dbQuery.copyTable(null, writerProperty, tableName, tmpTableName)) {
                    LogUtils.writeLog(logParams, MessageUtils.messageEn("etl.writer.create.temp.table.failed", tmpTableName));
                    log.error(MessageUtils.message("etl.writer.create.temp.table.failed", tmpTableName));
                    return false;
                }
            }

            //Execute pre-sql
            if (preSql != null && preSql.size() > 0) {
                preSql.forEach(sql -> {
                    dbQuery.execute(sql.toString());
                });
                LogUtils.writeLog(logParams, MessageUtils.messageEn("etl.writer.pre.sql", JSON.toJSONString(preSql)));
            }

            //Field setting correspondence
            List<Column> cpColumnList = new ArrayList<>();
            for (int i = 0; i < readerColumns.size(); i++) {
                cpColumnList.add(new Column(readerColumns.get(i)).as(targetColumn.get(i).toString()));
            }
            dataset = dataset.select(cpColumnList.toArray(new Column[cpColumnList.size()]));

            Boolean flag = false;
            //Full write or append write
            if (writeModeType == 1 || writeModeType == 2) {
                try {
                    dataset.write()
                            .format("jdbc")
                            .options(writerOptions)
                            .mode("append")
                            .save();
                    flag = true;
                } catch (Exception e) {
                    log.info(MessageUtils.message("etl.writer.save.failed", e.getMessage()));
                    LogUtils.writeLog(logParams, MessageUtils.messageEn("etl.writer.save.failed", e.getMessage()));
                    success = false;
                }
            }

            //Incremental update
            if (writeModeType == 3) {
                updateOrInsertModeType(dataset, dbQuery, writerProperty, batchSize, selectedColumns, targetColumn, tableName);
            }

            //Determine whether a temporary table exists
            if (StringUtils.isNotBlank(tmpTableName)) {
                if (flag) {
                    if (StringUtils.equals(DbType.KINGBASE8.getDb(), writerProperty.getDbType())
                            || StringUtils.equals(DbType.SQL_SERVER.getDb(), writerProperty.getDbType())
                            || StringUtils.equals(DbType.SQL_SERVER2008.getDb(), writerProperty.getDbType())) {
                        tableName = StringUtils.isNotBlank(writerProperty.getDbName()) ? writerProperty.getDbName() + "." + writerProperty.getSid() + "." + tableName : tableName;
                        tmpTableName = StringUtils.isNotBlank(writerProperty.getDbName()) ? writerProperty.getDbName() + "." + writerProperty.getSid() + "." + tmpTableName : tmpTableName;
                    }  else {
                        tableName = StringUtils.isNotBlank(writerProperty.getDbName()) ? writerProperty.getDbName() + "." + tableName : tableName;
                        tmpTableName = StringUtils.isNotBlank(writerProperty.getDbName()) ? writerProperty.getDbName() + "." + tmpTableName : tmpTableName;
                    }
                    writerOptions.put("dbtable", tmpTableName);

                    //Delete target
                    dbQuery.execute("DROP TABLE " + tableName);
                    log.info(MessageUtils.message("etl.writer.drop.target.table", tableName));
                    LogUtils.writeLog(logParams, MessageUtils.messageEn("etl.writer.drop.target.table", tableName));

                    //Change the temporary table name to a formal table
                    String repTableName = (StringUtils.isNotBlank(writerProperty.getDbName()) ? StringUtils.replace(tableName, writerProperty.getDbName() + ".", "") : tableName);
                    if (StringUtils.isNotBlank(writerProperty.getSid())) {
                        repTableName = StringUtils.replace(repTableName, writerProperty.getSid() + ".", "");
                    }
                    if (StringUtils.equals(DbType.DORIS.getDb(), writerProperty.getDbType())) {
                        dbQuery.execute("ALTER TABLE " + tmpTableName + " RENAME " + repTableName);
                    } else {
                        dbQuery.execute("ALTER TABLE " + tmpTableName + " RENAME TO " + repTableName);
                    }
                    log.info(MessageUtils.message("etl.writer.rename.temp.table", tmpTableName, tableName));
                    LogUtils.writeLog(logParams, MessageUtils.messageEn("etl.writer.rename.temp.table", tmpTableName, tableName));
                } else {
                    //Delete temporary table
                    dbQuery.execute("DROP TABLE " + tmpTableName);
                    log.info(MessageUtils.message("etl.writer.drop.temp.table", tmpTableName));
                    LogUtils.writeLog(logParams, MessageUtils.messageEn("etl.writer.drop.temp.table", tmpTableName));
                }
            }
            //Execute post sql
            if (postSql != null) {
                postSql.forEach(sql -> {
                    dbQuery.execute(sql.toString());
                });
            }
        } catch (Exception e) {
            log.error(MessageUtils.message("etl.writer.write.failed"), e);
            LogUtils.writeLog(logParams, MessageUtils.messageEn("etl.failure.reason", e.getMessage()));
            success = false;
        } finally {
            if (dbQuery != null) {
                dbQuery.close();
            }
        }
        return success;
    }

    @Override
    public String code() {
        return TaskComponentTypeEnum.DB_WRITER.getCode();
    }

    /**
     * Get field information of specified database table
     *
     * @param conn
     * @param columns
     * @param tableName
     * @return
     */
    Triple<List<String>, List<Integer>, List<String>> getColumnMetaData(Connection conn, List<String> columns, DbQueryProperty writerProperty, String tableName) {
        Statement statement = null;
        ResultSet rs = null;
        if (StringUtils.equals(DbType.KINGBASE8.getDb(), writerProperty.getDbType())
                || StringUtils.equals(DbType.SQL_SERVER.getDb(), writerProperty.getDbType())
                || StringUtils.equals(DbType.SQL_SERVER2008.getDb(), writerProperty.getDbType())) {
            tableName = writerProperty.getDbName() + "." + writerProperty.getSid() + "." + tableName;
        } else if (StringUtils.isNotBlank(writerProperty.getDbName())) {
            tableName = writerProperty.getDbName() + "." + tableName;
        }

        Triple<List<String>, List<Integer>, List<String>> columnMetaData = new ImmutableTriple<List<String>, List<Integer>, List<String>>(
                new ArrayList<String>(), new ArrayList<Integer>(),
                new ArrayList<String>());

        try {
            statement = conn.createStatement();
            String queryColumnSql = "select " + String.join(",", columns) + " from " + tableName
                    + " where 1=2";

            rs = statement.executeQuery(queryColumnSql);
            ResultSetMetaData rsMetaData = rs.getMetaData();
            for (int i = 0, len = rsMetaData.getColumnCount(); i < len; i++) {
                columnMetaData.getLeft().add(rsMetaData.getColumnName(i + 1));
                columnMetaData.getMiddle().add(rsMetaData.getColumnType(i + 1));
                columnMetaData.getRight().add(
                        rsMetaData.getColumnTypeName(i + 1));
            }
            return columnMetaData;
        } catch (SQLException throwables) {
            log.error(MessageUtils.messageEn("etl.writer.get.column.type.failed"));
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException throwables) {
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException throwables) {
                }
            }
        }
        return columnMetaData;
    }

    private DataColumn getColumn(Object value) {
        DataColumn col;
        if (value == null) {
            col = new StringDataColumn();
        } else if (value instanceof String) {
            col = new StringDataColumn((String) value);
        } else if (value instanceof Integer) {
            col = new LongDataColumn(((Integer) value).longValue());
        } else if (value instanceof Long) {
            col = new LongDataColumn((Long) value);
        } else if (value instanceof Byte) {
            col = new LongDataColumn(((Byte) value).longValue());
        } else if (value instanceof Short) {
            col = new LongDataColumn(((Short) value).longValue());
        } else if (value instanceof Double) {
            col = new DoubleDataColumn(BigDecimal.valueOf((Double) value));
        } else if (value instanceof Float) {
            col = new DoubleDataColumn(BigDecimal.valueOf(((Float) value).doubleValue()));
        } else if (value instanceof BigDecimal) {
            col = new DoubleDataColumn((BigDecimal) value);
        } else if (value instanceof Date) {
            col = new DateDataColumn((Date) value);
        } else if (value instanceof Boolean) {
            col = new BoolDataColumn((Boolean) value);
        } else if (value instanceof byte[]) {
            col = new BytesDataColumn((byte[]) value);
        } else if (value instanceof List) {
            col = new StringDataColumn(JSON.toJSONString(value));
        } else if (value instanceof Map) {
            col = new StringDataColumn(JSON.toJSONString(value));
        } else if (value instanceof Array) {
            col = new StringDataColumn(JSON.toJSONString(value));
        } else {
            throw DBException.asDataXException(MessageUtils.messageEn("etl.error.unknown.type", value.getClass().getName()));
        }
        return col;
    }

    void fillPreparedStatementColumnType(PreparedStatement pstmt, Integer columnIndex, int columnSqltype, String typeName, DataColumn dataColumn, Triple<List<String>, List<Integer>, List<String>> resultSetMetaData, String dbType) throws SQLException {
//        Integer add = 1;
//        if (StringUtils.equals(DbType.DORIS.getDb(), dbType)) {
//            add = 0;
//        }
        Date utilDate;
        switch (columnSqltype) {
            case Types.CHAR:
            case Types.NCHAR:
            case Types.CLOB:
            case Types.NCLOB:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
            case Types.NVARCHAR:
            case Types.LONGNVARCHAR:
                pstmt.setString(columnIndex + 1, dataColumn
                        .asString());
                break;

            case Types.SMALLINT:
            case Types.INTEGER:
            case Types.BIGINT:
            case Types.NUMERIC:
            case Types.DECIMAL:
            case Types.FLOAT:
            case Types.REAL:
            case Types.DOUBLE:
                String strValue = dataColumn.asString();
                if ("".equals(strValue)) {
                    pstmt.setString(columnIndex + 1, null);
                } else {
                    pstmt.setString(columnIndex + 1, strValue);
                }
                break;

            //tinyint is a little special in some database like mysql {boolean->tinyint(1)}
            case Types.TINYINT:
                Long longValue = dataColumn.asLong();
                if (null == longValue) {
                    pstmt.setString(columnIndex + 1, null);
                } else {
                    pstmt.setString(columnIndex + 1, longValue.toString());
                }
                break;

            // for mysql bug, see http://bugs.mysql.com/bug.php?id=35115
            case Types.DATE:
                if (typeName == null) {
                    typeName = resultSetMetaData.getRight().get(columnIndex);
                }

                if (typeName.equalsIgnoreCase("year")) {
                    if (dataColumn.asBigInteger() == null) {
                        pstmt.setString(columnIndex + 1, null);
                    } else {
                        pstmt.setInt(columnIndex + 1, dataColumn.asBigInteger().intValue());
                    }
                } else {
                    java.sql.Date sqlDate = null;
                    try {
                        utilDate = dataColumn.asDate();
                    } catch (DBException e) {
                        throw new SQLException(MessageUtils.messageEn("etl.error.date.conversion", dataColumn));
                    }

                    if (null != utilDate) {
                        sqlDate = new java.sql.Date(utilDate.getTime());
                    }
                    pstmt.setDate(columnIndex + 1, sqlDate);
                }
                break;

            case Types.TIME:
                Time sqlTime = null;
                try {
                    utilDate = dataColumn.asDate();
                } catch (DBException e) {
                    throw new SQLException(MessageUtils.messageEn("etl.error.time.conversion", dataColumn));
                }

                if (null != utilDate) {
                    sqlTime = new Time(utilDate.getTime());
                }
                pstmt.setTime(columnIndex + 1, sqlTime);
                break;

            case Types.TIMESTAMP:
                Timestamp sqlTimestamp = null;
                try {
                    utilDate = dataColumn.asDate();
                } catch (DBException e) {
                    throw new SQLException(MessageUtils.messageEn("etl.error.timestamp.conversion", dataColumn));
                }

                if (null != utilDate) {
                    sqlTimestamp = new Timestamp(
                            utilDate.getTime());
                }
                pstmt.setTimestamp(columnIndex + 1, sqlTimestamp);
                break;

            case Types.BINARY:
            case Types.VARBINARY:
            case Types.BLOB:
            case Types.LONGVARBINARY:
                pstmt.setBytes(columnIndex + 1, dataColumn
                        .asBytes());
                break;

            case Types.BOOLEAN:
                pstmt.setBoolean(columnIndex + 1, dataColumn.asBoolean());
                break;

            // warn: bit(1) -> Types.BIT You can use setBoolean
            // warn: bit(>1) -> Types.VARBINARY can use setBytes
            case Types.BIT:
                if (DbType.MYSQL.getDb().equals(dbType)) {
                    pstmt.setBoolean(columnIndex + 1, dataColumn.asBoolean());
                } else {
                    pstmt.setString(columnIndex + 1, dataColumn.asString());
                }
                break;
        }
    }


    /***
     * Handle append mode
     * @param dataset
     * @param dbQuery
     * @param writerProperty
     * @param selectedColumns
     * @param column
     * @param tableName
     * @return
     */
    boolean updateOrInsertModeType(Dataset<Row> dataset, DbQuery dbQuery, DbQueryProperty writerProperty, Integer batchSize, List<Object> selectedColumns, List<Object> column, String tableName) {
        List<String> selectedColumnList = selectedColumns.stream().map(Object::toString).collect(Collectors.toList());
        List<String> columnList = column.stream().map(Object::toString).collect(Collectors.toList());
        String updateSql = dbQuery.getInsertOrUpdateSql(writerProperty, tableName, selectedColumnList, columnList);
        if (!writerProperty.getDbType().equals(DbType.MYSQL.getDb())) {
            List<String> recordOne = new ArrayList<>();
            for (int j = 0; j < columnList.size(); j++) {
                if (selectedColumnList.contains(columnList.get(j))) {
                    recordOne.add(columnList.get(j));
                }
            }

            for (int j = 0; j < columnList.size(); j++) {
                if (!selectedColumnList.contains(columnList.get(j))) {
                    recordOne.add(columnList.get(j));
                }
            }

            for (int j = 0; j < columnList.size(); j++) {
                recordOne.add(columnList.get(j));
            }

            columnList = recordOne;
        }
        // Get batch data
        List<Row> rows = dataset.collectAsList();
        // Skip if no data
        if (rows.isEmpty()) {
            return false;
        }
        // Connect to database
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(writerProperty.trainToJdbcUrl(), writerProperty.getUsername(), writerProperty.getPassword());
            conn.setAutoCommit(false); // Disable autocommit
            List<Row> writeBuffer = new ArrayList<>(batchSize);
            //Get the target database field type list
            Triple<List<String>, List<Integer>, List<String>> resultSetMetaData = getColumnMetaData(conn, columnList, writerProperty, tableName);
            for (Row row : rows) {
                writeBuffer.add(row);
                if (writeBuffer.size() >= batchSize) {
                    doBatchInsert(writerProperty, writeBuffer, conn, updateSql, columnList, resultSetMetaData);
                    writeBuffer.clear();
                }
            }
            if (!writeBuffer.isEmpty()) {
                doBatchInsert(writerProperty, writeBuffer, conn, updateSql, columnList, resultSetMetaData);
                writeBuffer.clear();
            }
        } catch (SQLException e) {
            log.error(MessageUtils.messageEn("etl.writer.write.failed"), e);
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    log.error(MessageUtils.messageEn("etl.writer.close.connection.failed"), throwables);
                }
            }
        }
        return true;
    }

    void doBatchInsert(DbQueryProperty writerProperty, List<Row> writeBuffer, Connection conn, String updateSql, List<String> columnList, Triple<List<String>, List<Integer>, List<String>> resultSetMetaData) throws SQLException {
        // Create PreparedStatement
        try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {

            for (Row row : writeBuffer) {
                Integer columnIndex = 0;//sql?subscript
                // Set data
                for (String o : columnList) {
                    //When the field is at the subscript of row
                    Integer index = row.fieldIndex(o);
                    Object value = row.get(index);
                    //Encapsulated into DataColumn
                    DataColumn dataColumn = getColumn(value);
                    //The current field is subscripted into the target field
                    Integer metaIndex = resultSetMetaData.getLeft().indexOf(o);
                    //Get target field type
                    int columnSqltype = resultSetMetaData.getMiddle().get(metaIndex);
                    //Get the target field type name
                    String typeName = resultSetMetaData.getRight().get(metaIndex);
                    fillPreparedStatementColumnType(pstmt, columnIndex, columnSqltype, typeName, dataColumn, resultSetMetaData, writerProperty.getDbType());
                    columnIndex++;
                }
                pstmt.addBatch(); // Add to batch update
            }
            // Perform batch updates
            pstmt.executeBatch();
            conn.commit(); // Commit transaction
        }
    }
}
