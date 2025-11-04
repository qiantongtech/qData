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
import tech.qiantong.qdata.spark.etl.utils.LogUtils;
import tech.qiantong.qdata.spark.etl.utils.RedisUtils;
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
 * 用途:数据库输出
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
        LogUtils.writeLog(logParams, "开始数据库输出节点");
        LogUtils.writeLog(logParams, "开始任务时间: " + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
        LogUtils.writeLog(logParams, "任务参数：" + writer.toJSONString(PrettyFormat));
        JSONObject parameter = writer.getJSONObject("parameter");
        //封装读取信息
        Map<String, String> writerOptions = DBUtils.getDbOptions(parameter);

        //前置sql
        Integer batchSize = parameter.getInteger("batchSize");

        //前置sql
        List<Object> preSql = parameter.getJSONArray("preSql");

        //输入字段
        List<Object> column = parameter.getJSONArray("column");
        List<String> readerColumns = column.stream().map(c -> (String) c).collect(Collectors.toList());
        //输出字段
        List<Object> targetColumn = parameter.getJSONArray("target_column");


        //后置sql
        List<Object> postSql = parameter.getJSONArray("postSql");
        //写入类型 1 全量，2 追加写，3 增量更新
        Integer writeModeType = parameter.getInteger("writeModeType");
        String writeMode = null;
        switch (writeModeType) {
            case 1:
                writeMode = "全量";
                break;
            case 2:
                writeMode = "追加写";
                break;
            case 3:
                writeMode = "增量更新";
                break;
            default:
                writeMode = "";
                break;
        }
        LogUtils.writeLog(logParams, "写入方式为：" + writeMode);
        //增量更新主键
        List<Object> selectedColumns = parameter.getJSONArray("selectedColumns");
        //目标表名称
        String tableName = parameter.getJSONObject("connection").getString("table");
        String tmpTableName = null;
        if (writeModeType == 1) {
            tmpTableName = tableName + "_" + DateUtil.format(new Date(), "yyyyMMddHHmmss");
            writerOptions.put("dbtable", tmpTableName);
        }
        String datasource = RedisUtils.hget("datasource", parameter.getString("datasourceId"));
        DbQueryProperty writerProperty;
        // 替换存储在 redis 中最新的数据源连接信息
        if (datasource != null && !"".equals(datasource)) {
            writerProperty = JSONObject.parseObject(datasource, DbQueryProperty.class);
        }else {
            writerProperty = JSONObject.parseObject(parameter.getJSONObject("writerProperty").toString(), DbQueryProperty.class);
        }

        //创建连接
        DbQuery dbQuery = dataSourceFactory.createDbQuery(writerProperty);
        log.info(JSON.toJSONString(writerProperty));
        if (!dbQuery.valid()) {
            log.info(JSON.toJSONString(writerProperty));
            LogUtils.writeLog(logParams, "数据库连接失败：" + JSON.toJSONString(writerProperty));
            return false;
        }
        Boolean success = true;
        try {
            //创建临时表
            if (StringUtils.isNotBlank(tmpTableName)) {
                LogUtils.writeLog(logParams, "创建临时表：" + tmpTableName);
                log.info("创建临时表");
                if (!dbQuery.copyTable(null, writerProperty, tableName, tmpTableName)) {
                    LogUtils.writeLog(logParams, "创建临时表失败：" + tmpTableName);
                    log.error("创建临时表失败");
                    return false;
                }
            }

            //执行前置sql
            if (preSql != null && preSql.size() > 0) {
                preSql.forEach(sql -> {
                    dbQuery.execute(sql.toString());
                });
                LogUtils.writeLog(logParams, "执行前置sql：" + JSON.toJSONString(preSql));
            }

            //字段设置对应关系
            List<Column> cpColumnList = new ArrayList<>();
            for (int i = 0; i < readerColumns.size(); i++) {
                cpColumnList.add(new Column(readerColumns.get(i)).as(targetColumn.get(i).toString()));
            }
            dataset = dataset.select(cpColumnList.toArray(new Column[cpColumnList.size()]));

            Boolean flag = false;
            //全量写或追加写
            if (writeModeType == 1 || writeModeType == 2) {
                try {
                    dataset.write()
                            .format("jdbc")
                            .options(writerOptions)
                            .mode("append")
                            .save();
                    flag = true;
                } catch (Exception e) {
                    log.info("保存失败:{}", e.getMessage());
                    LogUtils.writeLog(logParams, "保存失败:" + e.getMessage());
                    success = false;
                }
            }

            //增量更新
            if (writeModeType == 3) {
                updateOrInsertModeType(dataset, dbQuery, writerProperty, batchSize, selectedColumns, targetColumn, tableName);
            }

            //判断是否存在临时表
            if (StringUtils.isNotBlank(tmpTableName)) {
                if (flag) {
                    if (StringUtils.equals(DbType.KINGBASE8.getDb(), writerProperty.getDbType())
                            || StringUtils.equals(DbType.SQL_SERVER.getDb(), writerProperty.getDbType())) {
                        tableName = StringUtils.isNotBlank(writerProperty.getDbName()) ? writerProperty.getDbName() + "." + writerProperty.getSid() + "." + tableName : tableName;
                        tmpTableName = StringUtils.isNotBlank(writerProperty.getDbName()) ? writerProperty.getDbName() + "." + writerProperty.getSid() + "." + tmpTableName : tmpTableName;
                    }  else {
                        tableName = StringUtils.isNotBlank(writerProperty.getDbName()) ? writerProperty.getDbName() + "." + tableName : tableName;
                        tmpTableName = StringUtils.isNotBlank(writerProperty.getDbName()) ? writerProperty.getDbName() + "." + tmpTableName : tmpTableName;
                    }
                    writerOptions.put("dbtable", tmpTableName);

                    //删除目标
                    dbQuery.execute("DROP TABLE " + tableName);
                    log.info("删除目标表:{}", tableName);
                    LogUtils.writeLog(logParams, "删除目标表:" + tableName);

                    //临时表名改为正式表
                    String repTableName = (StringUtils.isNotBlank(writerProperty.getDbName()) ? StringUtils.replace(tableName, writerProperty.getDbName() + ".", "") : tableName);
                    if (StringUtils.isNotBlank(writerProperty.getSid())) {
                        repTableName = StringUtils.replace(repTableName, writerProperty.getSid() + ".", "");
                    }
                    dbQuery.execute("ALTER TABLE " + tmpTableName + " RENAME TO " + repTableName);
                    log.info("临时表：{}改为目标表:{}", tmpTableName, tableName);
                    LogUtils.writeLog(logParams, "临时表：" + tmpTableName + "改为目标表:" + tableName);
                } else {
                    //删除临时表
                    dbQuery.execute("DROP TABLE " + tmpTableName);
                    log.info("删除临时表:{}", tableName);
                    LogUtils.writeLog(logParams, "删除临时表:" + tmpTableName);
                }
            }
            //执行后置sql
            if (postSql != null) {
                postSql.forEach(sql -> {
                    dbQuery.execute(sql.toString());
                });
            }
        } catch (Exception e) {
            log.error("写入失败: ", e);
            LogUtils.writeLog(logParams, "失败原因:" + e.getMessage());
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
     * 获取指定库表字段信息
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
                || StringUtils.equals(DbType.SQL_SERVER.getDb(), writerProperty.getDbType())) {
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
            log.error("获取字段类型失败");
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
            throw DBException.asDataXException("未知类型type:" + value.getClass().getName());
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
                        throw new SQLException(String.format(
                                "Date 类型转换错误：[%s]", dataColumn));
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
                    throw new SQLException(String.format(
                            "TIME 类型转换错误：[%s]", dataColumn));
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
                    throw new SQLException(String.format(
                            "TIMESTAMP 类型转换错误：[%s]", dataColumn));
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

            // warn: bit(1) -> Types.BIT 可使用setBoolean
            // warn: bit(>1) -> Types.VARBINARY 可使用setBytes
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
     * 处理追加模式
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
        // 获取批处理数据
        List<Row> rows = dataset.collectAsList();
        // 如果没有数据则跳过
        if (rows.isEmpty()) {
            return false;
        }
        // 连接数据库
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(writerProperty.trainToJdbcUrl(), writerProperty.getUsername(), writerProperty.getPassword());
            conn.setAutoCommit(false); // 禁用自动提交
            List<Row> writeBuffer = new ArrayList<>(batchSize);
            //获取目标库字段类型列表
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
            log.error("写入失败",e);
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    log.error("关闭数据库连接失败", throwables);
                }
            }
        }
        return true;
    }

    void doBatchInsert(DbQueryProperty writerProperty, List<Row> writeBuffer, Connection conn, String updateSql, List<String> columnList, Triple<List<String>, List<Integer>, List<String>> resultSetMetaData) throws SQLException {
        // 创建 PreparedStatement
        try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {

            for (Row row : writeBuffer) {
                Integer columnIndex = 0;//sql?下标
                // 设置数据
                for (String o : columnList) {
                    //当字段在row的下标
                    Integer index = row.fieldIndex(o);
                    Object value = row.get(index);
                    //封装成 DataColumn
                    DataColumn dataColumn = getColumn(value);
                    //当前字段在目标字段下标
                    Integer metaIndex = resultSetMetaData.getLeft().indexOf(o);
                    //获取目标字段类型
                    int columnSqltype = resultSetMetaData.getMiddle().get(metaIndex);
                    //获取目标字段类型名称
                    String typeName = resultSetMetaData.getRight().get(metaIndex);
                    fillPreparedStatementColumnType(pstmt, columnIndex, columnSqltype, typeName, dataColumn, resultSetMetaData, writerProperty.getDbType());
                    columnIndex++;
                }
                pstmt.addBatch(); // 添加到批量更新
            }
            // 执行批量更新
            pstmt.executeBatch();
            conn.commit(); // 提交事务
        }
    }
}
