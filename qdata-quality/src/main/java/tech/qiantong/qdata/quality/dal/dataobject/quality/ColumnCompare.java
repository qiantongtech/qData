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
