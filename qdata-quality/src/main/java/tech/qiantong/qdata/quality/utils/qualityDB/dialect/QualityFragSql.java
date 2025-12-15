package tech.qiantong.qdata.quality.utils.qualityDB.dialect;

import cn.hutool.core.lang.Assert;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import tech.qiantong.qdata.quality.dal.dataobject.quality.QualityRuleEntity;
import tech.qiantong.qdata.quality.utils.SqlBuilderUtils;

import java.util.*;
import java.util.stream.Collectors;

public interface QualityFragSql {

    default boolean ignoreNullValue(QualityRuleEntity rule) {
        Map<String, Object> ruleConfig = rule.getConfig();
        Boolean ignoreNullValue = SqlBuilderUtils.parseBoolean(ruleConfig.get("ignoreNullValue"));
        return ignoreNullValue != null && ignoreNullValue;
    }

    default String pos(String frag, QualityRuleEntity rule) {
        if (ignoreNullValue(rule)) {
            return String.format("(%s is null or (%s))", rule.getRuleColumn(), frag);
        }
        return frag;
    }

    default String neg(String frag, QualityRuleEntity rule) {
        if (ignoreNullValue(rule)) {
            return String.format("not (%s)", frag);
        }
        return String.format("(%s is null or not (%s))", rule.getRuleColumn(), frag);
    }

    default String fragCharacter(QualityRuleEntity rule) {
        String column = rule.getRuleColumn();
        String regex = (String) rule.getConfig().get("regex");
        return regex(column, regex);
    }

    default String regex(String column, String regex) {
        return String.format("REGEXP_LIKE(%s, '%s')", column, regex);
    }

    default String fragLength(QualityRuleEntity rule) {
        String column = rule.getRuleColumn();
        Map<String, Object> ruleConfig = rule.getConfig();
        Integer min = MapUtils.getInteger(ruleConfig, "minLength");
        Integer max = MapUtils.getInteger(ruleConfig, "maxLength");
        List<String> tmp = new ArrayList<>();
        if (min != null) {
            tmp.add(String.format("LENGTH(%s)>=%d", column, min));
        }
        if (max != null) {
            tmp.add(String.format("LENGTH(%s)<=%d", column, max));
        }
        return String.join(" and ", tmp);
    }

    default String fragDecimalPrecision(QualityRuleEntity rule) {
        String column = rule.getRuleColumn();
        Map<String, Object> ruleConfig = rule.getConfig();
        boolean skipInteger = SqlBuilderUtils.parseBoolean(ruleConfig.get("skipInteger"));
        int scale = Integer.parseInt(ruleConfig.get("scale").toString());
        String frag = String.format("(LENGTH(%s)-INSTR(%s, '.'))=%d AND INSTR(%s,'.')>0", column, column, scale, column);
        if (skipInteger) {
            frag = String.format("INSTR(%s,'.')=0 OR %s", column, frag);
        }
        return frag;
    }

    default String fragEnum(QualityRuleEntity rule) {
        String column = rule.getRuleColumn();
        Map<String, Object> ruleConfig = rule.getConfig();
        List<String> values = (List<String>) ruleConfig.get("validValues");
        boolean ignoreCase = SqlBuilderUtils.parseBoolean(rule.getConfig().get("ignoreCase"));
        String frag;
        if (ignoreCase) {
            Set<String> collect = values.stream().map(String::toUpperCase).collect(Collectors.toSet());
            frag = String.format("UPPER(%s) in('%s')", column, String.join("','", collect));
        } else {
            frag = String.format("%s in('%s')", column, String.join("','", new HashSet<>(values)));
        }
        return frag;
    }

    default String fragNumericRange(QualityRuleEntity rule) {
        Map<String, Object> ruleConfig = rule.getConfig();
        String column = rule.getRuleColumn();
        Double min = MapUtils.getDouble(ruleConfig, "minValue");
        Double max = MapUtils.getDouble(ruleConfig, "maxValue");
        Assert.isFalse(min == null && max == null);
        boolean include = SqlBuilderUtils.parseBoolean(ruleConfig.get("includeBoundary"));
        List<String> exp = new ArrayList<>();
        if (include) {
            if (max != null) {
                exp.add(String.format("%s<=%f", column, max));
            }
            if (min != null) {
                exp.add(String.format("%s>=%f", column, min));
            }
        } else {
            if (max != null) {
                exp.add(String.format("%s<%f", column, max));
            }
            if (min != null) {
                exp.add(String.format("%s>%f", column, min));
            }
        }
        return String.join(" AND ", exp);
    }

    default String fragFieldCompleteness(List<String> columns, int fillStrategy) {
        String frag;
        if (fillStrategy == 1) {
            frag = columns.stream()
                    .map(col -> String.format("%s IS NOT NULL", col))
                    .collect(Collectors.joining(" AND "));
        } else if (fillStrategy == 2) {
            String allNull = columns.stream()
                    .map(col -> String.format("%s IS NULL", col))
                    .collect(Collectors.joining(" AND "));
            String allNotNull = columns.stream()
                    .map(col -> String.format("%s IS NOT NULL", col))
                    .collect(Collectors.joining(" AND "));
            frag = String.format("(%s) OR (%s)", allNull, allNotNull);
        } else {
            throw new IllegalArgumentException("Unsupported fillStrategy: " + fillStrategy);
        }
        return frag;
    }

    default String fragDatetime(QualityRuleEntity rule) {
        String column = rule.getRuleColumn();
        Collection<?> dateformat = (Collection<?>) rule.getConfig().get("dateformat");
        Collection<?> dateformatError = (Collection<?>) rule.getConfig().get("dateformatError");

        // 格式模式映射
        Map<String, String> formatPatterns = new HashMap<>(8);
        formatPatterns.put("yyyy-MM-dd", "^[0-9]{4}-[0-9]{2}-[0-9]{2}$");
        formatPatterns.put("yyyy-MM-dd HH:mm:ss", "^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}$");

        // 构建格式条件
        List<String> formatConditions = CollectionUtils.isEmpty(dateformat) ?
                Collections.emptyList() :
                dateformat.stream()
                        .map(Object::toString)
                        .map(formatPatterns::get)
                        .filter(Objects::nonNull)
                        .map(pattern -> regex(column, pattern))
                        .collect(Collectors.toList());

        // 构建错误格式排除条件
        List<String> errorConditions = CollectionUtils.isEmpty(dateformatError) ?
                Collections.emptyList() :
                dateformatError.stream()
                        .map(error -> String.format("%s <> '%s'", column, error.toString()))
                        .collect(Collectors.toList());

        // 组合所有条件
        List<String> allConditions = new ArrayList<>();
        if (!formatConditions.isEmpty()) {
            allConditions.add("(" + String.join(" or ", formatConditions) + ")");
        }
        if (!errorConditions.isEmpty()) {
            allConditions.addAll(errorConditions);
        }
        return allConditions.isEmpty() ? "" : String.join(" and ", allConditions);
    }

    default String fragMasterExist(QualityRuleEntity rule) {
        String column = rule.getRuleColumn();
        String masterTable = (String) rule.getConfig().get("relatedTable");
        String masterColumn = (String) rule.getConfig().get("relatedColumn");
        Assert.notBlank(masterTable);
        Assert.notBlank(masterColumn);
        return String.format("EXISTS (SELECT 1 FROM %s WHERE %s.%s = %s.%s)", masterTable, masterTable, masterColumn, rule.getTableName(), column);
    }

}
