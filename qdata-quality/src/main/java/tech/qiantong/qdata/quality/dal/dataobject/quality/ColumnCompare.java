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

package tech.qiantong.qdata.quality.dal.dataobject.quality;

import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ColumnCompare {

    private String leftField;
    private String operator;
    private String rightField;

    public String toExpression() {
        return String.format("%s%s%s", leftField, operator, rightField);
    }

    public String toExpressionIgnoreNullValue() {
        return String.format("(%s IS NULL OR %s IS NULL OR %s%s%s)", leftField, rightField, leftField, operator, rightField);
    }

    public static String toExpressions(List<ColumnCompare> compares, boolean ignoreNullValue) {
        if (ignoreNullValue) {
            return compares.stream().map(ColumnCompare::toExpressionIgnoreNullValue).collect(Collectors.joining(" AND "));
        }
        return compares.stream().map(ColumnCompare::toExpression).collect(Collectors.joining(" AND "));
    }

    public static String toExpressionsNeg(List<ColumnCompare> compares, boolean ignoreNullValue) {
        if (ignoreNullValue) {
            return compares.stream().map(it -> String.format("(NOT %s)", it.toExpression())).collect(Collectors.joining(" OR "));
        }
        return compares.stream().map(it -> String.format("(%s IS NULL OR %s IS NULL OR NOT %s)", it.getLeftField(), it.getRightField(), it.toExpression()))
                .collect(Collectors.joining(" OR "));
    }

}
