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

package tech.qiantong.qdata.spark.etl.utils.db.element;


import tech.qiantong.qdata.spark.etl.utils.db.exception.DBException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.util.Date;

/**
 * Created by jingxing on 14-8-24.
 */
public class DateDataColumn extends DataColumn {

    private DateType subType = DateType.DATETIME;

    private int nanos = 0;

    private int precision = -1;

    public static enum DateType {
        DATE, TIME, DATETIME
    }

    /**
     * Construct a DateColumn with value time(java.sql.Time), using Date subtype as TIME, only time, no date
     */
    public DateDataColumn(Time time, int nanos, int jdbcPrecision) {
        this(time);
        if (time != null) {
            setNanos(nanos);
        }
        if (jdbcPrecision == 10) {
            setPrecision(0);
        }
        if (jdbcPrecision >= 12 && jdbcPrecision <= 17) {
            setPrecision(jdbcPrecision - 11);
        }
    }

    public long getNanos() {
        return nanos;
    }

    public void setNanos(int nanos) {
        this.nanos = nanos;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    /**
     * Construct a DateColumn with a null value, using the Date subtype DATETIME
     */
    public DateDataColumn() {
        this((Long) null);
    }

    /**
     * Construct a DateColumn whose value is stamp (Unix timestamp), using the Date subtype as DATETIME
     * The actual stored date is changed to long ms, which saves storage.
     */
    public DateDataColumn(final Long stamp) {
        super(stamp, Type.DATE, (null == stamp ? 0 : 8));
    }

    /**
     * Construct a DateColumn whose value is date(java.util.Date), using the Date subtype as DATETIME
     */
    public DateDataColumn(final Date date) {
        this(date == null ? null : date.getTime());
    }

    /**
     * Construct a DateColumn whose value is date(java.sql.Date), using the Date subtype as DATE, with only date and no time
     */
    public DateDataColumn(final java.sql.Date date) {
        this(date == null ? null : date.getTime());
        this.setSubType(DateType.DATE);
    }

    /**
     * Construct a DateColumn with value time(java.sql.Time), using Date subtype as TIME, only time, no date
     */
    public DateDataColumn(final Time time) {
        this(time == null ? null : time.getTime());
        this.setSubType(DateType.TIME);
    }

    /**
     * Construct a DateColumn with value ts(java.sql.Timestamp), using Date subtype as DATETIME
     */
    public DateDataColumn(final java.sql.Timestamp ts) {
        this(ts == null ? null : ts.getTime());
        this.setSubType(DateType.DATETIME);
    }

    @Override
    public Long asLong() {

        return (Long) this.getRawData();
    }

    @Override
    public String asString() {
        try {
            return ColumnCast.date2String(this);
        } catch (Exception e) {
            throw DBException.asDataXException(String.format("Date[%s]类型不能转为String .", this.toString()));
        }
    }

    @Override
    public Date asDate() {
        if (null == this.getRawData()) {
            return null;
        }

        return new Date((Long) this.getRawData());
    }

    @Override
    public Date asDate(String dateFormat) {
        return asDate();
    }

    @Override
    public byte[] asBytes() {
        throw DBException.asDataXException("Date类型不能转为Bytes .");
    }

    @Override
    public Boolean asBoolean() {
        throw DBException.asDataXException("Date类型不能转为Boolean .");
    }

    @Override
    public Double asDouble() {
        throw DBException.asDataXException("Date类型不能转为Double .");
    }

    @Override
    public BigInteger asBigInteger() {
        throw DBException.asDataXException("Date类型不能转为BigInteger .");
    }

    @Override
    public BigDecimal asBigDecimal() {
        throw DBException.asDataXException("Date类型不能转为BigDecimal .");
    }

    public DateType getSubType() {
        return subType;
    }

    public void setSubType(DateType subType) {
        this.subType = subType;
    }
}
