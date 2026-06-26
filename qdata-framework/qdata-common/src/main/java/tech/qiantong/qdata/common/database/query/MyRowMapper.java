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

package tech.qiantong.qdata.common.database.query;

import com.kingbase8.util.KBobject;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.math.BigDecimal;
import java.math.BigInteger;
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

            if (value instanceof Map) {
                String digits = value.toString().replaceAll("\\D", "");
                if (!digits.isEmpty()) {
                    value = Long.parseLong(digits);
                }
            }

            if (value instanceof BigInteger) {
                value = ((BigInteger) value).longValue();
            } else if (value instanceof BigDecimal) {
                value = ((BigDecimal) value).doubleValue();
            } else if (value instanceof Double) {
                value = ((Double) value).doubleValue();
            } else if (value instanceof Float) {
                value = ((Float) value).doubleValue();
            } else if (value instanceof Number) {
                value = ((Number) value).longValue();
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
