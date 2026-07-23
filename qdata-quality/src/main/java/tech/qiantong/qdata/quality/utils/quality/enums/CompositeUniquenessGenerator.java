/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.quality.utils.quality.enums;

import org.springframework.stereotype.Component;
import tech.qiantong.qdata.quality.dal.dataobject.quality.QualityRuleEntity;
import tech.qiantong.qdata.quality.utils.quality.QualitySqlGenerator;
import tech.qiantong.qdata.quality.utils.qualityDB.ComponentItem;
import tech.qiantong.qdata.quality.utils.qualityDB.ComponentRegistry;

// Multi-field combination uniqueness check
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
