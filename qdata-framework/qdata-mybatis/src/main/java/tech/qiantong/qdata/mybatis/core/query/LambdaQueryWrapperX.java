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

package tech.qiantong.qdata.mybatis.core.query;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Locale;
import java.util.Set;

/**
 * Expand the MyBatis Plus QueryWrapper class, mainly adding the following functions:
 * <p>
 * 1. For the method of splicing conditions, add the xxxIfPresent method. When it is used to judge that the value does not exist, do not splice it into the condition.
 *
 * @param <T> data type
 */
public class LambdaQueryWrapperX<T> extends LambdaQueryWrapper<T> {

    public LambdaQueryWrapperX<T> likeIfPresent(SFunction<T, ?> column, String val) {
        if (StringUtils.hasText(val)) {
            return (LambdaQueryWrapperX<T>) super.like(column, val);
        }
        return this;
    }

    public LambdaQueryWrapperX<T> likeRightIfPresent(SFunction<T, ?> column, String val) {
        if (StringUtils.hasText(val)) {
            return (LambdaQueryWrapperX<T>) super.likeRight(column, val);
        }
        return this;
    }

    public LambdaQueryWrapperX<T> inIfPresent(SFunction<T, ?> column, Collection<?> values) {
        if (ObjectUtil.isAllNotEmpty(values) && !ArrayUtil.isEmpty(values)) {
            return (LambdaQueryWrapperX<T>) super.in(column, values);
        }
        return this;
    }

    public LambdaQueryWrapperX<T> inIfPresent(SFunction<T, ?> column, Object... values) {
        if (ObjectUtil.isAllNotEmpty(values) && !ArrayUtil.isEmpty(values)) {
            return (LambdaQueryWrapperX<T>) super.in(column, values);
        }
        return this;
    }
    public LambdaQueryWrapperX<T> notInIfPresent(SFunction<T, ?> column, Object... values) {
        if (ObjectUtil.isAllNotEmpty(values) && !ArrayUtil.isEmpty(values)) {
            return (LambdaQueryWrapperX<T>) super.notIn(column, values);
        }
        return this;
    }

    public LambdaQueryWrapperX<T> eqIfPresent(SFunction<T, ?> column, Object val) {
        if (ObjectUtil.isNotEmpty(val)) {
            return (LambdaQueryWrapperX<T>) super.eq(column, val);
        }
        return this;
    }

    public LambdaQueryWrapperX<T> neIfPresent(SFunction<T, ?> column, Object val) {
        if (ObjectUtil.isNotEmpty(val)) {
            return (LambdaQueryWrapperX<T>) super.ne(column, val);
        }
        return this;
    }

    public LambdaQueryWrapperX<T> gtIfPresent(SFunction<T, ?> column, Object val) {
        if (val != null) {
            return (LambdaQueryWrapperX<T>) super.gt(column, val);
        }
        return this;
    }

    public LambdaQueryWrapperX<T> geIfPresent(SFunction<T, ?> column, Object val) {
        if (val != null) {
            return (LambdaQueryWrapperX<T>) super.ge(column, val);
        }
        return this;
    }

    public LambdaQueryWrapperX<T> ltIfPresent(SFunction<T, ?> column, Object val) {
        if (val != null) {
            return (LambdaQueryWrapperX<T>) super.lt(column, val);
        }
        return this;
    }

    public LambdaQueryWrapperX<T> leIfPresent(SFunction<T, ?> column, Object val) {
        if (val != null) {
            return (LambdaQueryWrapperX<T>) super.le(column, val);
        }
        return this;
    }

    public LambdaQueryWrapperX<T> betweenIfPresent(SFunction<T, ?> column, Object val1, Object val2) {
        if (val1 != null && val2 != null) {
            return (LambdaQueryWrapperX<T>) super.between(column, val1, val2);
        }
        if (val1 != null) {
            return (LambdaQueryWrapperX<T>) ge(column, val1);
        }
        if (val2 != null) {
            return (LambdaQueryWrapperX<T>) le(column, val2);
        }
        return this;
    }


    /**
     * Added multi-column sort method based on string field names, using allowed fields set to prevent SQL injection.
     *
     * @param columns comma separated column names (camel case naming)
     * @param isAsc Comma separated sort directions ("asc", "desc"), or a single direction applied to all columns
     * @param allowedColumns Set of fields that are allowed to be sorted (underlined naming)
     * @return this, keep the chain of calls
     */
    public LambdaQueryWrapperX<T> orderBy(String columns, String isAsc, Set<String> allowedColumns) {
        if (columns != null && !columns.trim().isEmpty()) {
            String[] columnArray = columns.split(",");
            String[] isAscArray = (isAsc != null && !isAsc.trim().isEmpty()) ? isAsc.split(",") : new String[0];
            StringBuilder orderClause = new StringBuilder();

            for (int i = 0; i < columnArray.length; i++) {
                String column = columnArray[i].trim();
                if (column.isEmpty()) {
                    continue;
                }

                String columnName = camelToUnderline(column);
                // Verify whether the field name is legal
                if (!allowedColumns.contains(columnName)) {
                    throw new IllegalArgumentException("非法的排序字段：" + column);
                }

                boolean ascending = true; // Default ascending order

                if (isAscArray.length > 0) {
                    if (i < isAscArray.length) {
                        String direction = isAscArray[i].trim().toLowerCase(Locale.ROOT);
                        if (direction.equals("desc")) {
                            ascending = false;
                        } else if (!direction.equals("asc")) {
                            // If the direction is invalid, the default is descending order.
                            ascending = false;
                        }
                    } else if (isAscArray.length == 1) {
                        String direction = isAscArray[0].trim().toLowerCase(Locale.ROOT);
                        if (direction.equals("desc")) {
                            ascending = false;
                        } else if (!direction.equals("asc")) {
                            ascending = false;
                        }
                    } else {
                        // If no corresponding sorting direction is provided, the default is descending order.
                        ascending = false;
                    }
                } else {
                    // If isAsc is not provided, defaults to descending order
                    ascending = false;
                }

                orderClause.append(columnName).append(ascending ? " ASC, " : " DESC, ");
            }


            // Remove trailing commas and spaces
            if (orderClause.length() > 0) {
                orderClause.setLength(orderClause.length() - 2);
                // Check if ORDER BY clause already exists
                String existingOrderBy = this.getSqlSegment().toLowerCase(Locale.ROOT);
                if (existingOrderBy.contains("order by")) {
                    // Append to existing ORDER BY clause
                    this.last(", " + orderClause.toString());
                } else {
                    // Add new ORDER BY clause
                    this.last(" ORDER BY " + orderClause.toString());
                }
            }
        }
        return this;
    }

    /**
     * Convert camelCase naming to underscore naming
     *
     * @param param camel case named string
     * @return underscore named string
     */
    public static String camelToUnderline(String param) {
        if (param == null || param.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder(param.length());
        char[] chars = param.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (Character.isUpperCase(c) && i > 0) {
                sb.append('_');
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }



    public LambdaQueryWrapperX<T> betweenIfPresent(SFunction<T, ?> column, Object[] values) {
        Object val1 = ArrayUtils.get(values, 0);
        Object val2 = ArrayUtils.get(values, 1);
        return betweenIfPresent(column, val1, val2);
    }

    // ========== Rewrite the parent class method to facilitate chain calls ==========

    @Override
    public LambdaQueryWrapperX<T> eq(boolean condition, SFunction<T, ?> column, Object val) {
        super.eq(condition, column, val);
        return this;
    }

    @Override
    public LambdaQueryWrapperX<T> eq(SFunction<T, ?> column, Object val) {
        super.eq(column, val);
        return this;
    }

    @Override
    public LambdaQueryWrapperX<T> or() {
        super.or();
        return this;
    }
    @Override
    public LambdaQueryWrapperX<T> isNull(SFunction<T, ?> column) {
        super.isNull(column);
        return this;
    }

    @Override
    public LambdaQueryWrapperX<T> orderByDesc(SFunction<T, ?> column) {
        super.orderByDesc(true, column);
        return this;
    }

    @Override
    public LambdaQueryWrapperX<T> orderByAsc(SFunction<T, ?> column) {
        super.orderByAsc(true, column);
        return this;
    }

    @Override
    public LambdaQueryWrapperX<T> last(String lastSql) {
        super.last(lastSql);
        return this;
    }

    @Override
    public LambdaQueryWrapperX<T> in(SFunction<T, ?> column, Collection<?> coll) {
        super.in(column, coll);
        return this;
    }

}
