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
            throw DBException.asDataXException(String.format("Converting [%s] to Long caused an overflow.", integer.toString()));
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
            throw DBException.asDataXException(String.format("Converting [%s] to Double caused an overflow.",
                    decimal.toPlainString()));
        }
    }
}
