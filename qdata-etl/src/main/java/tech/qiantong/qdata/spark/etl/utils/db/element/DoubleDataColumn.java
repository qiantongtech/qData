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
import java.util.Date;

public class DoubleDataColumn extends DataColumn {

    public DoubleDataColumn(final String data) {
        this(data, null == data ? 0 : data.length());
        this.validate(data);
    }

    public DoubleDataColumn(Long data) {
        this(data == null ? (String) null : String.valueOf(data));
    }

    public DoubleDataColumn(Integer data) {
        this(data == null ? (String) null : String.valueOf(data));
    }

    /**
     * Double cannot represent accurate decimal data. We do not recommend using this method to save Double data. It is recommended to use String as the constructor input parameter.
     */
    public DoubleDataColumn(final Double data) {
        this(data == null ? (String) null
                : new BigDecimal(String.valueOf(data)).toPlainString());
    }

    /**
     * Float cannot represent accurate decimal data. We do not recommend using this method to save Float data. It is recommended to use String as the construction input parameter.
     */
    public DoubleDataColumn(final Float data) {
        this(data == null ? (String) null
                : new BigDecimal(String.valueOf(data)).toPlainString());
    }

    public DoubleDataColumn(final BigDecimal data) {
        this(null == data ? (String) null : data.toPlainString());
    }

    public DoubleDataColumn(final BigInteger data) {
        this(null == data ? (String) null : data.toString());
    }

    public DoubleDataColumn() {
        this((String) null);
    }

    private DoubleDataColumn(final String data, int byteSize) {
        super(data, Type.DOUBLE, byteSize);
    }

    @Override
    public BigDecimal asBigDecimal() {
        if (null == this.getRawData()) {
            return null;
        }

        try {
            return new BigDecimal((String) this.getRawData());
        } catch (NumberFormatException e) {
            throw DBException.asDataXException(String.format("String[%s] 无法转换为Double类型 .",
                    (String) this.getRawData()));
        }
    }

    @Override
    public Double asDouble() {
        if (null == this.getRawData()) {
            return null;
        }

        String string = (String) this.getRawData();

        boolean isDoubleSpecific = string.equals("NaN")
                || string.equals("-Infinity") || string.equals("+Infinity");
        if (isDoubleSpecific) {
            return Double.valueOf(string);
        }

        BigDecimal result = this.asBigDecimal();
        OverFlowUtil.validateDoubleNotOverFlow(result);

        return result.doubleValue();
    }

    @Override
    public Long asLong() {
        if (null == this.getRawData()) {
            return null;
        }

        BigDecimal result = this.asBigDecimal();
        OverFlowUtil.validateLongNotOverFlow(result.toBigInteger());

        return result.longValue();
    }

    @Override
    public BigInteger asBigInteger() {
        if (null == this.getRawData()) {
            return null;
        }

        return this.asBigDecimal().toBigInteger();
    }

    @Override
    public String asString() {
        if (null == this.getRawData()) {
            return null;
        }
        return (String) this.getRawData();
    }

    @Override
    public Boolean asBoolean() {
        throw DBException.asDataXException("Double类型无法转为Bool .");
    }

    @Override
    public Date asDate() {
        throw DBException.asDataXException("Double类型无法转为Date类型 .");
    }

    @Override
    public Date asDate(String dateFormat) {
        throw DBException.asDataXException("Double类型无法转为Date类型 .");
    }

    @Override
    public byte[] asBytes() {
        throw DBException.asDataXException("Double类型无法转为Bytes类型 .");
    }

    private void validate(final String data) {
        if (null == data) {
            return;
        }

        if (data.equalsIgnoreCase("NaN") || data.equalsIgnoreCase("-Infinity")
                || data.equalsIgnoreCase("Infinity")) {
            return;
        }

        try {
            new BigDecimal(data);
        } catch (Exception e) {
            throw DBException.asDataXException(String.format("String[%s]无法转为Double类型 .", data));
        }
    }

}
