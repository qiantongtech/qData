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

}
