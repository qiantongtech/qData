package tech.qiantong.qdata.quality.utils.quality.enums;

import org.springframework.stereotype.Component;
import tech.qiantong.qdata.quality.dal.dataobject.quality.QualityRuleEntity;
import tech.qiantong.qdata.quality.utils.quality.QualitySqlGenerator;
import tech.qiantong.qdata.quality.utils.qualityDB.ComponentItem;
import tech.qiantong.qdata.quality.utils.qualityDB.ComponentRegistry;

// 数值精度校验（如金额最多保留两位）
@Component("DECIMAL_PRECISION_VALIDATION")
public class DecimalPrecisionGenerator implements QualitySqlGenerator {
    @Override
    public String generateSql(QualityRuleEntity rule) {
        ComponentRegistry registry = new ComponentRegistry();
        ComponentItem componentItem = registry.getComponentItem(rule.getDaDatasourceById().getDatasourceType());
        return componentItem.generateDecimalPrecisionValidationSql(rule);
    }

    @Override
    public String generateErrorSql(QualityRuleEntity rule) {
        ComponentRegistry registry = new ComponentRegistry();
        ComponentItem componentItem = registry.getComponentItem(rule.getDaDatasourceById().getDatasourceType());
        return componentItem.generateDecimalPrecisionValidationErrorSql(rule);
    }

    @Override
    public String generateValidDataSql(QualityRuleEntity rule, int limit, int offset) {
        ComponentRegistry registry = new ComponentRegistry();
        ComponentItem componentItem = registry.getComponentItem(rule.getDaDatasourceById().getDatasourceType());
        return componentItem.generateDecimalPrecisionValidationValidDataSql(rule,limit,offset);
    }
}
