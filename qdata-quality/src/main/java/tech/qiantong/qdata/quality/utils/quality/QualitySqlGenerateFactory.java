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

package tech.qiantong.qdata.quality.utils.quality;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class QualitySqlGenerateFactory {

    @Autowired
    private Map<String, QualitySqlGenerator> generatorMap;

    public QualitySqlGenerator getGenerator(String ruleType) {
        QualitySqlGenerator generator = generatorMap.get(ruleType);
        if (generator == null) {
            throw new IllegalArgumentException("不支持的规则类型：" + ruleType);
        }
        return generator;
    }
}
