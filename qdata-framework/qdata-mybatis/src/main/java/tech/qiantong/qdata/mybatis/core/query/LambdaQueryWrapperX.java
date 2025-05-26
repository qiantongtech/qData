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
 * 拓展 MyBatis Plus QueryWrapper 类，主要增加如下功能：
 * <p>
 * 1. 拼接条件的方法，增加 xxxIfPresent 方法，用于判断值不存在的时候，不要拼接到条件中。
 *
 * @param <T> 数据类型
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
     * 添加基于字符串字段名的多列排序方法，使用允许的字段集合防止 SQL 注入。
     *
     * @param columns        逗号分隔的列名（驼峰命名）
     * @param isAsc          逗号分隔的排序方向（"asc", "desc"），或单一方向应用于所有列
     * @param allowedColumns 允许排序的字段集合（下划线命名）
     * @return this，保持链式调用
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
                // 校验字段名是否合法
                if (!allowedColumns.contains(columnName)) {
                    throw new IllegalArgumentException("非法的排序字段：" + column);
                }

                boolean ascending = true; // 默认升序

                if (isAscArray.length > 0) {
                    if (i < isAscArray.length) {
                        String direction = isAscArray[i].trim().toLowerCase(Locale.ROOT);
                        if (direction.equals("desc")) {
                            ascending = false;
                        } else if (!direction.equals("asc")) {
                            // 如果方向无效，默认降序
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
                        // 如果没有提供对应的排序方向，默认降序
                        ascending = false;
                    }
                } else {
                    // 如果未提供 isAsc，默认降序
                    ascending = false;
                }

                orderClause.append(columnName).append(ascending ? " ASC, " : " DESC, ");
            }


            // 移除末尾的逗号和空格
            if (orderClause.length() > 0) {
                orderClause.setLength(orderClause.length() - 2);
                // 检查是否已经存在 ORDER BY 子句
                String existingOrderBy = this.getSqlSegment().toLowerCase(Locale.ROOT);
                if (existingOrderBy.contains("order by")) {
                    // 追加到现有的 ORDER BY 子句
                    this.last(", " + orderClause.toString());
                } else {
                    // 添加新的 ORDER BY 子句
                    this.last(" ORDER BY " + orderClause.toString());
                }
            }
        }
        return this;
    }

    /**
     * 将驼峰命名转换为下划线命名
     *
     * @param param 驼峰命名字符串
     * @return 下划线命名字符串
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

    // ========== 重写父类方法，方便链式调用 ==========

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
