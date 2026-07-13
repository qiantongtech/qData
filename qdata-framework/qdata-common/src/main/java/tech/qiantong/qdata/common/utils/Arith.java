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

package tech.qiantong.qdata.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Precise floating point arithmetic
 *
 * @author qdata
 */
public class Arith
{

    /**Default division operation precision */
    private static final int DEF_DIV_SCALE = 10;

    /** This class cannot be instantiated */
    private Arith()
    {
    }

    /**
     * Provides precise addition operations.
     * @param v1 summand
     * @param v2 addend
     * @return the sum of the two parameters
     */
    public static double add(double v1, double v2)
    {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * Provides precise subtraction operations.
     * @param v1 minuend
     * @param v2 subtrahend
     * @return the difference between the two parameters
     */
    public static double sub(double v1, double v2)
    {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * Provides precise multiplication operations.
     * @param v1 multiplicand
     * @param v2 multiplier
     * @return product of two parameters
     */
    public static double mul(double v1, double v2)
    {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * Provides (relatively) accurate division operations. When the division cannot be completed, the division operation is accurate to
     * There are 10 decimal places after the decimal point, and subsequent numbers are rounded off.
     * @param v1 dividend
     * @param v2 divisor
     * @return the quotient of the two parameters
     */
    public static double div(double v1, double v2)
    {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    /**
     * Provides (relatively) accurate division operations. When inexhaustible division occurs, the scale parameter indicates
     * To determine the precision, subsequent numbers will be rounded off.
     * @param v1 dividend
     * @param v2 divisor
     * @param scale means that it needs to be accurate to several decimal places.
     * @return the quotient of the two parameters
     */
    public static double div(double v1, double v2, int scale)
    {
        if (scale < 0)
        {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        if (b1.compareTo(BigDecimal.ZERO) == 0)
        {
            return BigDecimal.ZERO.doubleValue();
        }
        return b1.divide(b2, scale, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * Provides precise decimal rounding processing.
     * @param v The number that needs to be rounded
     * @param scale How many decimal places to keep after the decimal point?
     * @return the rounded result
     */
    public static double round(double v, int scale)
    {
        if (scale < 0)
        {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = BigDecimal.ONE;
        return b.divide(one, scale, RoundingMode.HALF_UP).doubleValue();
    }
}
