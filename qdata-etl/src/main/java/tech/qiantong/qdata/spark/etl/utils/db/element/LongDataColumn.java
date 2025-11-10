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

import org.apache.commons.lang3.math.NumberUtils;
import tech.qiantong.qdata.spark.etl.utils.db.exception.DBException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class LongDataColumn extends DataColumn {

    /**
     * 从整形字符串表示转为LongColumn，支持Java科学计数法
     * <p>
     * NOTE: <br>
     * 如果data为浮点类型的字符串表示，数据将会失真，请使用DoubleColumn对接浮点字符串
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

            // 当 rawData 为[0-127]时，rawData.bitLength() < 8，导致其 byteSize = 0，简单起见，直接认为其长度为 data.length()
            // super.setByteSize(rawData.bitLength() / 8);
            super.setByteSize(data.length());
        } catch (Exception e) {
//			throw DataXException.asDataXException(
//					CommonErrorCode.CONVERT_NOT_SUPPORT,
//					String.format("String[%s]不能转为Long .", data));
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
        throw DBException.asDataXException("Long类型不能转为Bytes .");
    }

}
