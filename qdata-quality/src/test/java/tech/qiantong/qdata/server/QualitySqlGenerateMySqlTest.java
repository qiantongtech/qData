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
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
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
