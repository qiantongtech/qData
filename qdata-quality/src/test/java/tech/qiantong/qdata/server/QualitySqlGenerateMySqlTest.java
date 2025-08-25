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
public class QualitySqlGenerateMySqlTest {

    @Test
    public void generateCharacterSql() {
        QualityRuleEntity qualityRule = new QualityRuleEntity();
        qualityRule.setId("2");
        qualityRule.setRuleType("CHARACTER_VALIDATION");
        qualityRule.setDataId("56");
        qualityRule.setTableName("user8");
        qualityRule.setRuleColumn("name");
        qualityRule.setWhereClause("id<1000");

        Map<String, Object> map = new HashMap<>();
        map.put("regex", "^[a-z0-9_ ]+$");
        map.put("ignoreNullValue", true);
        qualityRule.setConfig(map);
        DaDatasourceDO daDatasourceDO = new DaDatasourceDO();
        daDatasourceDO.setDatasourceType(DbType.MYSQL.getDb());
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
    public void generateLengthSql() {
        QualityRuleEntity qualityRule = new QualityRuleEntity();
        qualityRule.setId("2");
        qualityRule.setRuleType("CHARACTER_VALIDATION");
        qualityRule.setDataId("56");
        qualityRule.setTableName("user8");
        qualityRule.setRuleColumn("name");

        Map<String, Object> map = new HashMap<>();
        map.put("minLength", 7);
        map.put("maxLength", 8);
        map.put("ignoreNullValue", false);
        qualityRule.setConfig(map);
        DaDatasourceDO daDatasourceDO = new DaDatasourceDO();
        daDatasourceDO.setDatasourceType(DbType.MYSQL.getDb());
        qualityRule.setDaDatasourceById(daDatasourceDO);

        LengthValidationGenerator generator = new LengthValidationGenerator();
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
        qualityRule.setTableName("user8");
        qualityRule.setRuleColumn("fraction");

        Map<String, Object> map = new HashMap<>();
        map.put("scale", 2);
        map.put("ignoreNullValue", false);
        map.put("skipInteger", true);
        qualityRule.setConfig(map);
        DaDatasourceDO daDatasourceDO = new DaDatasourceDO();
        daDatasourceDO.setDatasourceType(DbType.MYSQL.getDb());
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
    public void generateEnumSql() {
        QualityRuleEntity qualityRule = new QualityRuleEntity();
        qualityRule.setId("2");
        qualityRule.setRuleType("CHARACTER_VALIDATION");
        qualityRule.setDataId("56");
        qualityRule.setTableName("user8");
        qualityRule.setRuleColumn("age");

        Map<String, Object> map = new HashMap<>();
        map.put("validValues", Lists.newArrayList("21", "22"));
        map.put("ignoreNullValue", false);
        map.put("ignoreCase", true);
        qualityRule.setConfig(map);
        DaDatasourceDO daDatasourceDO = new DaDatasourceDO();
        daDatasourceDO.setDatasourceType(DbType.MYSQL.getDb());
        qualityRule.setDaDatasourceById(daDatasourceDO);

        EnumValidationGenerator generator = new EnumValidationGenerator();
        String sql = generator.generateSql(qualityRule);
        System.out.println(sql + ";");
        sql = generator.generateErrorSql(qualityRule);
        System.out.println(sql + ";");
        sql = generator.generateValidDataSql(qualityRule, 100, 0);
        System.out.println(sql + ";");
    }

    @Test
    public void generateNumericRangeSql() {
        QualityRuleEntity qualityRule = new QualityRuleEntity();
        qualityRule.setId("2");
        qualityRule.setRuleType("CHARACTER_VALIDATION");
        qualityRule.setDataId("56");
        qualityRule.setTableName("user8");
        qualityRule.setRuleColumn("AGE");

        Map<String, Object> map = new HashMap<>();
        map.put("minValue", 3.2);
        map.put("ignoreNullValue", false);
        map.put("maxValue", 100);
        map.put("includeRangeBound", true);
        qualityRule.setConfig(map);
        DaDatasourceDO daDatasourceDO = new DaDatasourceDO();
        daDatasourceDO.setDatasourceType(DbType.MYSQL.getDb());
        qualityRule.setDaDatasourceById(daDatasourceDO);

        NumericRangeValidationGenerator generator = new NumericRangeValidationGenerator();
        String sql = generator.generateSql(qualityRule);
        System.out.println(sql + ";");
        sql = generator.generateErrorSql(qualityRule);
        System.out.println(sql + ";");
        sql = generator.generateValidDataSql(qualityRule, 100, 0);
        System.out.println(sql + ";");
    }

    @Test
    public void generateOrderSql() {
        QualityRuleEntity qualityRule = new QualityRuleEntity();
        qualityRule.setId("2");
        qualityRule.setRuleType("CHARACTER_VALIDATION");
        qualityRule.setDataId("56");
        qualityRule.setTableName("user8");
        qualityRule.setRuleColumn("AGE");

        Map<String, Object> map = new HashMap<>();
        map.put("leftField", "id");
        map.put("rightField", "age");
        map.put("operator", ">");
        Map<String, Object> map1 = new HashMap<>();
        map1.put("allowPartialNull", false);
        map1.put("conditions", Lists.newArrayList(map));
        qualityRule.setConfig(map1);
        DaDatasourceDO daDatasourceDO = new DaDatasourceDO();
        daDatasourceDO.setDatasourceType(DbType.MYSQL.getDb());
        qualityRule.setDaDatasourceById(daDatasourceDO);

        TimeOrderValidationGenerator generator = new TimeOrderValidationGenerator();
        String sql = generator.generateSql(qualityRule);
        System.out.println(sql + ";");
        sql = generator.generateErrorSql(qualityRule);
        System.out.println(sql + ";");
        sql = generator.generateValidDataSql(qualityRule, 100, 0);
        System.out.println(sql + ";");
    }

    @Test
    public void generateFieldCompletenessSql() {
        QualityRuleEntity qualityRule = new QualityRuleEntity();
        qualityRule.setId("2");
        qualityRule.setRuleType("CHARACTER_VALIDATION");
        qualityRule.setDataId("56");
        qualityRule.setTableName("user8");
        qualityRule.setRuleColumn("AGE");

        Map<String, Object> map = new HashMap<>();
        map.put("fillStrategy", 2);
        map.put("columns", Lists.newArrayList("age", "name"));
        qualityRule.setConfig(map);
        DaDatasourceDO daDatasourceDO = new DaDatasourceDO();
        daDatasourceDO.setDatasourceType(DbType.MYSQL.getDb());
        qualityRule.setDaDatasourceById(daDatasourceDO);

        GroupFieldCompletenessGenerator generator = new GroupFieldCompletenessGenerator();
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
        qualityRule.setTableName("user8");
        qualityRule.setRuleColumn("AGE");
        qualityRule.setWhereClause("id>100");

        Map<String, Object> map = new HashMap<>();
        qualityRule.setRuleColumns(Lists.newArrayList("age", "name"));
        qualityRule.setConfig(map);
        DaDatasourceDO daDatasourceDO = new DaDatasourceDO();
        daDatasourceDO.setDatasourceType(DbType.MYSQL.getDb());
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
