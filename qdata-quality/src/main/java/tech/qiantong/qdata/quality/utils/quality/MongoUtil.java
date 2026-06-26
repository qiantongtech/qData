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
