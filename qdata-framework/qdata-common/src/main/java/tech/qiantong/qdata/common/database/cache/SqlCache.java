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

package tech.qiantong.qdata.common.database.cache;


import tech.qiantong.qdata.common.database.utils.MD5Util;

import java.util.Arrays;

/**
 * SQL缓存接口
 */
public interface SqlCache {

    /**
     * 计算key
     */
    default String buildSqlCacheKey(String sql, Object[] args) {
        return MD5Util.encrypt(sql + ":" + Arrays.toString(args));
    }

    /**
     * 存入缓存
     * @param key   key
     * @param value 值
     */
    void put(String key, Object value, long ttl);

    /**
     * 获取缓存
     * @param key   key
     * @return
     */
    <T> T get(String key);

    /**
     * 删除缓存
     * @param key  key
     */
    void delete(String key);
}
