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

/**
 * Created by jingxing on 14-8-24.
 */

public class StringDataColumn extends DataColumn {

    public StringDataColumn() {
        this((String) null);
    }

    public StringDataColumn(final String rawData) {
        super(rawData, Type.STRING, (null == rawData ? 0 : rawData
                .length()));
    }

    @Override
    public String asString() {
        if (null == this.getRawData()) {
            return null;
        }

        return (String) this.getRawData();
    }

    private void validateDoubleSpecific(final String data) {
        if ("NaN".equals(data) || "Infinity".equals(data)
                || "-Infinity".equals(data)) {
            throw DBException.asDataXException(String.format("String[\"%s\"] is a special Double value and cannot be converted to another type.", data));
        }

        return;
    }

    @Override
    public BigInteger asBigInteger() {
        if (null == this.getRawData()) {
            return null;
        }

        this.validateDoubleSpecific((String) this.getRawData());

        try {
            return this.asBigDecimal().toBigInteger();
        } catch (Exception e) {
            throw DBException.asDataXException(String.format("String[\"%s\"] cannot be converted to BigInteger.", this.asString()));
        }
    }

    @Override
    public Long asLong() {
        if (null == this.getRawData()) {
            return null;
        }

        this.validateDoubleSpecific((String) this.getRawData());

        try {
            BigInteger integer = this.asBigInteger();
            OverFlowUtil.validateLongNotOverFlow(integer);
            return integer.longValue();
        } catch (Exception e) {
            throw DBException.asDataXException(String.format("String[\"%s\"] cannot be converted to Long.", this.asString()));
        }
    }

    @Override
    public BigDecimal asBigDecimal() {
        if (null == this.getRawData()) {
            return null;
        }

        this.validateDoubleSpecific((String) this.getRawData());

        try {
            return new BigDecimal(this.asString());
        } catch (Exception e) {
            throw DBException.asDataXException(String.format(
                    "String [\"%s\"] cannot be converted to BigDecimal.", this.asString()));
        }
    }

    @Override
    public Double asDouble() {
        if (null == this.getRawData()) {
            return null;
        }

        String data = (String) this.getRawData();
        if ("NaN".equals(data)) {
            return Double.NaN;
        }

        if ("Infinity".equals(data)) {
            return Double.POSITIVE_INFINITY;
        }

        if ("-Infinity".equals(data)) {
            return Double.NEGATIVE_INFINITY;
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

        if ("true".equalsIgnoreCase(this.asString())) {
            return true;
        }

        if ("false".equalsIgnoreCase(this.asString())) {
            return false;
        }

        throw DBException.asDataXException(String.format("String[\"%s\"] cannot be converted to Bool.", this.asString()));
    }

    @Override
    public Date asDate() {
        try {
            return ColumnCast.string2Date(this);
        } catch (Exception e) {
            throw DBException.asDataXException(String.format("String[\"%s\"] cannot be converted to Date.", this.asString()));
        }
    }

    @Override
    public Date asDate(String dateFormat) {
        try {
            return ColumnCast.string2Date(this, dateFormat);
        } catch (Exception e) {
            throw DBException.asDataXException(String.format("String[\"%s\"] cannot be converted to Date.", this.asString()));
        }
    }

    @Override
    public byte[] asBytes() {
        try {
            return ColumnCast.string2Bytes(this);
        } catch (Exception e) {
            throw DBException.asDataXException(String.format("String[\"%s\"] cannot be converted to Bytes.", this.asString()));
        }
    }
}
