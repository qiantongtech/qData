package tech.qiantong.qdata.quality.dal.dataobject.quality;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColumnExpression {

    private String columnName;

    /**
     * IS_NULL
     * NOT_NULL
     * "=", "<", ">", "<=", ">=", "<>"
     */
    private String operator;

    private Object value;

    private static final Set<String> OP = Sets.newHashSet("=", "<", ">", "<=", ">=", "<>");

    public String toExpression() {
        if ("IS_NULL".equals(operator)) {
            return String.format("%s IS NULL", columnName);
        }
        if ("NOT_NULL".equals(operator)) {
            return String.format("%s IS NOT NULL", columnName);
        }
        if (OP.contains(operator)) {
            return String.format("%s%s'%s'", columnName, operator, value);
        }
        throw new IllegalArgumentException("operator is not supported: " + operator);
    }

    public String toExpressionNeg() {
        if ("IS_NULL".equals(operator)) {
            return String.format("%s IS NOT NULL", columnName);
        }
        if ("NOT_NULL".equals(operator)) {
            return String.format("%s IS NULL", columnName);
        }
        if (OP.contains(operator)) {
            return String.format("(%s is null or not %s%s'%s')", columnName, columnName, operator, value);
        }
        throw new IllegalArgumentException("operator is not supported: " + operator);
    }

    public static String toExpressions(List<ColumnExpression> expressions) {
        return expressions.stream().map(ColumnExpression::toExpression).collect(Collectors.joining(" AND "));
    }

    public static String toExpressionsNeg(List<ColumnExpression> expressions) {
        return expressions.stream().map(ColumnExpression::toExpressionNeg).collect(Collectors.joining(" or "));
    }

    public ColumnExpression(String columnName, String operator) {
        this.columnName = columnName;
        this.operator = operator;
    }

}
