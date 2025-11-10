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
     * Double无法表示准确的小数数据，我们不推荐使用该方法保存Double数据，建议使用String作为构造入参
     */
    public DoubleDataColumn(final Double data) {
        this(data == null ? (String) null
                : new BigDecimal(String.valueOf(data)).toPlainString());
    }

    /**
     * Float无法表示准确的小数数据，我们不推荐使用该方法保存Float数据，建议使用String作为构造入参
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
