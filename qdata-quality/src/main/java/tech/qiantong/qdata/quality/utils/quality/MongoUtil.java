package tech.qiantong.qdata.quality.utils.quality;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoUtil {
    private static final Logger log = LoggerFactory.getLogger(MongoUtil.class);

    public static void safeSave(MongoTemplate mongoTemplate, Object doc, String collectionName) {
        if (mongoTemplate != null) {
            try {
                if (!mongoTemplate.collectionExists(collectionName)) {
                    log.info("⚠️ Mongo 集合 '{}' 不存在，将自动创建（由 save 自动完成）", collectionName);
                } else {
                    log.debug("✅ Mongo 集合 '{}' 已存在", collectionName);
                }


                mongoTemplate.save(doc, collectionName);
            } catch (Exception e) {
                log.warn("Mongo 存储失败: {}", e.getMessage());
            }
        } else {
            log.info("MongoTemplate 未启用，跳过写入");
        }
    }
}
