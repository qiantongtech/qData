/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 */

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
