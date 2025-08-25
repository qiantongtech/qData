package tech.qiantong.qdata.server;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import tech.qiantong.qdata.common.database.constants.DbType;
import tech.qiantong.qdata.quality.dal.dataobject.datasource.DaDatasourceDO;
import tech.qiantong.qdata.quality.dal.dataobject.quality.QualityRuleEntity;
import tech.qiantong.qdata.quality.utils.quality.enums.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class QualitySqlGenerateDM8Test {

    @Test
    public void generateCharacterSql() {
        QualityRuleEntity qualityRule = new QualityRuleEntity();
        qualityRule.setId("2");
        qualityRule.setRuleType("CHARACTER_VALIDATION");
        qualityRule.setDataId("56");
        qualityRule.setTableName("TEST3");
        qualityRule.setRuleColumn("name");
        qualityRule.setWhereClause("id>1000");

        Map<String, Object> map = new HashMap<>();
        map.put("regex", "Ma");
        map.put("ignoreNullValue", true);
        qualityRule.setConfig(map);
        DaDatasourceDO daDatasourceDO = new DaDatasourceDO();
        daDatasourceDO.setDatasourceType(DbType.DM8.getDb());
        qualityRule.setDaDatasourceById(daDatasourceDO);

        CharacterValidationGenerator generator = new CharacterValidationGenerator();
        String sql = generator.generateSql(qualityRule);
        System.out.println(sql + ";");
        sql = generator.generateErrorSql(qualityRule);
        System.out.println(sql + ";");
        sql = generator.generateValidDataSql(qualityRule, 100, 0);
        System.out.println(sql + ";");
    }


    @Test
    public void generateDecimalPrecisionSql() {
        QualityRuleEntity qualityRule = new QualityRuleEntity();
        qualityRule.setId("2");
        qualityRule.setRuleType("CHARACTER_VALIDATION");
        qualityRule.setDataId("56");
        qualityRule.setTableName("TEST3");
        qualityRule.setRuleColumn("age");

        Map<String, Object> map = new HashMap<>();
        map.put("scale", 3);
        map.put("ignoreNullValue", false);
        map.put("skipInteger", true);
        qualityRule.setConfig(map);
        DaDatasourceDO daDatasourceDO = new DaDatasourceDO();
        daDatasourceDO.setDatasourceType(DbType.DM8.getDb());
        qualityRule.setDaDatasourceById(daDatasourceDO);

        DecimalPrecisionGenerator generator = new DecimalPrecisionGenerator();
        String sql = generator.generateSql(qualityRule);
        System.out.println(sql + ";");
        sql = generator.generateErrorSql(qualityRule);
        System.out.println(sql + ";");
        sql = generator.generateValidDataSql(qualityRule, 100, 0);
        System.out.println(sql + ";");
    }

    @Test
    public void generateCompositeUniquenessSql() {
        QualityRuleEntity qualityRule = new QualityRuleEntity();
        qualityRule.setId("2");
        qualityRule.setRuleType("CHARACTER_VALIDATION");
        qualityRule.setDataId("56");
        qualityRule.setTableName("TEST3");
        qualityRule.setRuleColumn("AGE");
        qualityRule.setWhereClause("id>100");

        Map<String, Object> map = new HashMap<>();
        qualityRule.setRuleColumns(Lists.newArrayList("age", "name"));
        qualityRule.setConfig(map);
        DaDatasourceDO daDatasourceDO = new DaDatasourceDO();
        daDatasourceDO.setDatasourceType(DbType.DM8.getDb());
        qualityRule.setDaDatasourceById(daDatasourceDO);

        CompositeUniquenessGenerator generator = new CompositeUniquenessGenerator();
        String sql = generator.generateSql(qualityRule);
        System.out.println(sql + ";");
        sql = generator.generateErrorSql(qualityRule);
        System.out.println(sql + ";");
        sql = generator.generateValidDataSql(qualityRule, 100, 0);
        System.out.println(sql + ";");
    }

}
