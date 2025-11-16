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
 * For brand customization, please apply for brand customization authorization via official channels.
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
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
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
            throw DBException.asDataXException(String.format("String[\"%s\"]属于Double特殊类型，不能转为其他类型 .", data));
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
            throw DBException.asDataXException(String.format("String[\"%s\"]不能转为BigInteger .", this.asString()));
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
            throw DBException.asDataXException(String.format("String[\"%s\"]不能转为Long .", this.asString()));
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
                    "String [\"%s\"] 不能转为BigDecimal .", this.asString()));
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

        throw DBException.asDataXException(String.format("String[\"%s\"]不能转为Bool .", this.asString()));
    }

    @Override
    public Date asDate() {
        try {
            return ColumnCast.string2Date(this);
        } catch (Exception e) {
            throw DBException.asDataXException(String.format("String[\"%s\"]不能转为Date .", this.asString()));
        }
    }

    @Override
    public Date asDate(String dateFormat) {
        try {
            return ColumnCast.string2Date(this, dateFormat);
        } catch (Exception e) {
            throw DBException.asDataXException(String.format("String[\"%s\"]不能转为Date .", this.asString()));
        }
    }

    @Override
    public byte[] asBytes() {
        try {
            return ColumnCast.string2Bytes(this);
        } catch (Exception e) {
            throw DBException.asDataXException(String.format("String[\"%s\"]不能转为Bytes .", this.asString()));
        }
    }
}
