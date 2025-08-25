package tech.qiantong.qdata.quality.utils.qualityDB.dialect;


import tech.qiantong.qdata.quality.dal.dataobject.quality.QualityRuleEntity;
import tech.qiantong.qdata.quality.utils.qualityDB.ComponentItem;

public class MySqlQuality implements ComponentItem {

    @Override
    public String fragCharacter(QualityRuleEntity rule) {
        String column = rule.getRuleColumn();
        String regex = (String) rule.getConfig().get("regex");
        return String.format("BINARY %s REGEXP '%s'", column, regex);
    }

}
