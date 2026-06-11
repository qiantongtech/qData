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
 */

package tech.qiantong.qdata.spark.etl.utils.db.element;

import tech.qiantong.qdata.spark.etl.utils.db.exception.DBException;

import java.math.BigDecimal;
import java.math.BigInteger;

public final class OverFlowUtil {
    public static final BigInteger MAX_LONG = BigInteger
            .valueOf(Long.MAX_VALUE);

    public static final BigInteger MIN_LONG = BigInteger
            .valueOf(Long.MIN_VALUE);

    public static final BigDecimal MIN_DOUBLE_POSITIVE = new BigDecimal(
            String.valueOf(Double.MIN_VALUE));

    public static final BigDecimal MAX_DOUBLE_POSITIVE = new BigDecimal(
            String.valueOf(Double.MAX_VALUE));

    public static boolean isLongOverflow(final BigInteger integer) {
        return (integer.compareTo(OverFlowUtil.MAX_LONG) > 0 || integer
                .compareTo(OverFlowUtil.MIN_LONG) < 0);

    }

    public static void validateLongNotOverFlow(final BigInteger integer) {
        boolean isOverFlow = OverFlowUtil.isLongOverflow(integer);

        if (isOverFlow) {
            throw DBException.asDataXException(String.format("[%s] 转为Long类型出现溢出 .", integer.toString()));
        }
    }

    public static boolean isDoubleOverFlow(final BigDecimal decimal) {
        if (decimal.signum() == 0) {
            return false;
        }

        BigDecimal newDecimal = decimal;
        boolean isPositive = decimal.signum() == 1;
        if (!isPositive) {
            newDecimal = decimal.negate();
        }

        return (newDecimal.compareTo(MIN_DOUBLE_POSITIVE) < 0 || newDecimal
                .compareTo(MAX_DOUBLE_POSITIVE) > 0);
    }

    public static void validateDoubleNotOverFlow(final BigDecimal decimal) {
        boolean isOverFlow = OverFlowUtil.isDoubleOverFlow(decimal);
        if (isOverFlow) {
            throw DBException.asDataXException(String.format("[%s]转为Double类型出现溢出 .",
                    decimal.toPlainString()));
        }
    }
}
