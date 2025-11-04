package tech.qiantong.qdata.quality.utils.qualityDB.dialect;

import org.apache.commons.collections4.MapUtils;
import tech.qiantong.qdata.quality.dal.dataobject.quality.QualityRuleEntity;
import tech.qiantong.qdata.quality.utils.SqlBuilderUtils;

import java.util.*;
import java.util.stream.Collectors;

public interface QualityFragSql {

    default String fragCharacter(QualityRuleEntity rule) {
        String column = rule.getRuleColumn();
        String regex = (String) rule.getConfig().get("regex");
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

    default String fragOrderNeg(List<Map<String, String>> conditions, boolean allowPartialNull) {
        if (!allowPartialNull) {
            return conditions.stream()
                    .map(c -> {
                        String leftField = c.get("leftField");
                        String rightField = c.get("rightField");
                        return String.format("(%s IS NULL OR %s IS NULL OR NOT %s %s %s)", leftField, rightField, leftField, c.get("operator"), rightField);
                    })
                    .collect(Collectors.joining(" OR "));
        }
        return conditions.stream()
                .map(c -> String.format("(NOT %s %s %s)", c.get("leftField"), c.get("operator"), c.get("rightField")))
                .collect(Collectors.joining(" OR "));
    }

    /**
     * @param fillStrategy {@link QualityRuleEntity}
     */
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

}
