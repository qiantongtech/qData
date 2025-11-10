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
