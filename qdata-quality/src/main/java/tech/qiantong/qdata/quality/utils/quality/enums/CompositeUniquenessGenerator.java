package tech.qiantong.qdata.quality.utils.quality.enums;

import org.springframework.stereotype.Component;
import tech.qiantong.qdata.quality.dal.dataobject.quality.QualityRuleEntity;
import tech.qiantong.qdata.quality.utils.quality.QualitySqlGenerator;
import tech.qiantong.qdata.quality.utils.qualityDB.ComponentItem;
import tech.qiantong.qdata.quality.utils.qualityDB.ComponentRegistry;

// 多字段组合唯一性校验
@Component("COMPOSITE_UNIQUENESS_VALIDATION")
public class CompositeUniquenessGenerator implements QualitySqlGenerator {
    @Override
    public String generateSql(QualityRuleEntity rule) {
        ComponentRegistry registry = new ComponentRegistry();
        ComponentItem componentItem = registry.getComponentItem(rule.getDaDatasourceById().getDatasourceType());
        return componentItem.generateCompositeUniquenessValidationSql(rule);
    }

    @Override
    public String generateErrorSql(QualityRuleEntity rule) {
        ComponentRegistry registry = new ComponentRegistry();
        ComponentItem componentItem = registry.getComponentItem(rule.getDaDatasourceById().getDatasourceType());
        return componentItem.generateCompositeUniquenessValidationErrorSql(rule);
    }

    @Override
    public String generateValidDataSql(QualityRuleEntity rule, int limit, int offset) {
        ComponentRegistry registry = new ComponentRegistry();
        ComponentItem componentItem = registry.getComponentItem(rule.getDaDatasourceById().getDatasourceType());
        return componentItem.generateCompositeUniquenessValidationValidDataSql(rule,limit,offset);
    }
}
