package tech.qiantong.qdata.common.database.query;

import com.kingbase8.util.KBobject;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

/**
 * <P>
 * 用途:
 * </p>
 *
 * @author: FXB
 * @create: 2025-07-16 09:56
 **/
public class MyRowMapper implements RowMapper<Map<String, Object>> {
    @Override
    public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        int columnCount = meta.getColumnCount();
        Map<String, Object> row = this.createColumnMap(columnCount);
        for (int i = 1; i <= columnCount; i++) {
            String colName = meta.getColumnLabel(i);
            Object value = rs.getObject(i);
            if (value instanceof KBobject) {
                value = ((KBobject) value).getValue();
            } else {
                value = this.getColumnValue(rs, i);
            }
            row.putIfAbsent(colName, value);
        }
        return row;
    }

    protected Map<String, Object> createColumnMap(int columnCount) {
        return new LinkedCaseInsensitiveMap(columnCount);
    }

    protected String getColumnKey(String columnName) {
        return columnName;
    }

    @Nullable
    protected Object getColumnValue(ResultSet rs, int index) throws SQLException {
        return JdbcUtils.getResultSetValue(rs, index);
    }
}
