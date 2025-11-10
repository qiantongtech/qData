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
     * 构建值为time(java.sql.Time)的DateColumn，使用Date子类型为TIME，只有时间，没有日期
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
     * 构建值为null的DateColumn，使用Date子类型为DATETIME
     */
    public DateDataColumn() {
        this((Long) null);
    }

    /**
     * 构建值为stamp(Unix时间戳)的DateColumn，使用Date子类型为DATETIME
     * 实际存储有date改为long的ms，节省存储
     */
    public DateDataColumn(final Long stamp) {
        super(stamp, Type.DATE, (null == stamp ? 0 : 8));
    }

    /**
     * 构建值为date(java.util.Date)的DateColumn，使用Date子类型为DATETIME
     */
    public DateDataColumn(final Date date) {
        this(date == null ? null : date.getTime());
    }

    /**
     * 构建值为date(java.sql.Date)的DateColumn，使用Date子类型为DATE，只有日期，没有时间
     */
    public DateDataColumn(final java.sql.Date date) {
        this(date == null ? null : date.getTime());
        this.setSubType(DateType.DATE);
    }

    /**
     * 构建值为time(java.sql.Time)的DateColumn，使用Date子类型为TIME，只有时间，没有日期
     */
    public DateDataColumn(final Time time) {
        this(time == null ? null : time.getTime());
        this.setSubType(DateType.TIME);
    }

    /**
     * 构建值为ts(java.sql.Timestamp)的DateColumn，使用Date子类型为DATETIME
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
