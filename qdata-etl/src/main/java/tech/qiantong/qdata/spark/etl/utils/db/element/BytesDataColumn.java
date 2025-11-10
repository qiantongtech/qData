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
