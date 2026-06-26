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

import org.apache.commons.lang3.ArrayUtils;
import tech.qiantong.qdata.spark.etl.utils.db.exception.DBException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * Created by jingxing on 14-8-24.
 */
public class BytesDataColumn extends DataColumn {

    public BytesDataColumn() {
        this(null);
    }

    public BytesDataColumn(byte[] bytes) {
        super(ArrayUtils.clone(bytes), Type.BYTES, null == bytes ? 0
                : bytes.length);
    }

    @Override
    public byte[] asBytes() {
        if (null == this.getRawData()) {
            return null;
        }

        return (byte[]) this.getRawData();
    }

    @Override
    public String asString() {
        if (null == this.getRawData()) {
            return null;
        }

        try {
            return ColumnCast.bytes2String(this);
        } catch (Exception e) {
            throw DBException.asDataXException(String.format("Bytes[%s]不能转为String .", this.toString()));
        }
    }

    @Override
    public Long asLong() {
        throw DBException.asDataXException("Bytes类型不能转为Long .");
    }

    @Override
    public BigDecimal asBigDecimal() {
        throw DBException.asDataXException("Bytes类型不能转为BigDecimal .");
    }

    @Override
    public BigInteger asBigInteger() {
        throw DBException.asDataXException("Bytes类型不能转为BigInteger .");
    }

    @Override
    public Double asDouble() {
        throw DBException.asDataXException("Bytes类型不能转为Long .");
    }

    @Override
    public Date asDate() {
        throw DBException.asDataXException("Bytes类型不能转为Date .");
    }

    @Override
    public Date asDate(String dateFormat) {
        throw DBException.asDataXException("Bytes类型不能转为Date .");
    }

    @Override
    public Boolean asBoolean() {
        throw DBException.asDataXException("Bytes类型不能转为Boolean .");
    }
}
