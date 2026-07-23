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

import org.apache.commons.lang3.math.NumberUtils;
import tech.qiantong.qdata.spark.etl.utils.db.exception.DBException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class LongDataColumn extends DataColumn {

    /**
     * Convert from integer string representation to LongColumn, support Java scientific notation
     * <p>
     * NOTE: <br>
     * If the data is a floating-point string representation, the data will be distorted. Please use DoubleColumn to connect the floating-point string.
     */
    public LongDataColumn(final String data) {
        super(null, Type.LONG, 0);
        if (null == data) {
            return;
        }

        try {
            BigInteger rawData = NumberUtils.createBigDecimal(data)
                    .toBigInteger();
            super.setRawData(rawData);

            // When rawData is [0-127], rawData.bitLength() < 8, resulting in its byteSize = 0. For simplicity, its length is directly considered to be data.length()
            // super.setByteSize(rawData.bitLength() / 8);
            super.setByteSize(data.length());
        } catch (Exception e) {
//			throw DataXException.asDataXException(
//					CommonErrorCode.CONVERT_NOT_SUPPORT,
//	String.format("String[%s] cannot be converted to Long.", data));
        }
    }

    public LongDataColumn(Long data) {
        this(null == data ? (BigInteger) null : BigInteger.valueOf(data));
    }

    public LongDataColumn(Integer data) {
        this(null == data ? (BigInteger) null : BigInteger.valueOf(data));
    }

    public LongDataColumn(BigInteger data) {
        this(data, null == data ? 0 : 8);
    }

    private LongDataColumn(BigInteger data, int byteSize) {
        super(data, Type.LONG, byteSize);
    }

    public LongDataColumn() {
        this((BigInteger) null);
    }

    @Override
    public BigInteger asBigInteger() {
        if (null == this.getRawData()) {
            return null;
        }

        return (BigInteger) this.getRawData();
    }

    @Override
    public Long asLong() {
        BigInteger rawData = (BigInteger) this.getRawData();
        if (null == rawData) {
            return null;
        }

        OverFlowUtil.validateLongNotOverFlow(rawData);

        return rawData.longValue();
    }

    @Override
    public Double asDouble() {
        if (null == this.getRawData()) {
            return null;
        }

        BigDecimal decimal = this.asBigDecimal();
        OverFlowUtil.validateDoubleNotOverFlow(decimal);

        return decimal.doubleValue();
    }

    @Override
    public Boolean asBoolean() {
        if (null == this.getRawData()) {
            return null;
        }

        return this.asBigInteger().compareTo(BigInteger.ZERO) != 0 ? true
                : false;
    }

    @Override
    public BigDecimal asBigDecimal() {
        if (null == this.getRawData()) {
            return null;
        }

        return new BigDecimal(this.asBigInteger());
    }

    @Override
    public String asString() {
        if (null == this.getRawData()) {
            return null;
        }
        return ((BigInteger) this.getRawData()).toString();
    }

    @Override
    public Date asDate() {
        if (null == this.getRawData()) {
            return null;
        }
        return new Date(this.asLong());
    }

    @Override
    public Date asDate(String dateFormat) {
        return this.asDate();
    }

    @Override
    public byte[] asBytes() {
        throw DBException.asDataXException("Long cannot be converted to Bytes.");
    }

}
