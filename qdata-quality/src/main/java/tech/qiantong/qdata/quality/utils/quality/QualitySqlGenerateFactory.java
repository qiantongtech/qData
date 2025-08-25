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
