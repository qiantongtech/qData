package tech.qiantong.qdata.quality.utils.quality;


import tech.qiantong.qdata.quality.dal.dataobject.quality.QualityRuleEntity;

public interface QualitySqlGenerator {
    String generateSql(QualityRuleEntity rule);
    String generateErrorSql(QualityRuleEntity rule);
    String generateValidDataSql(QualityRuleEntity rule, int limit, int offset);
}
