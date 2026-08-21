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

package tech.qiantong.qdata.redis.service;

import java.util.List;
import java.util.Map;

public interface IRedisService {

    /**
     * Settings
     *
     * @param key
     * @param value
     */
    void set(String key, String value);

    /**
     * Settings, with timeout
     *
     * @param key
     * @param value
     * @param timeout
     */
    void set(String key, String value, long timeout);

    /** Atomically sets a non-negative integer value only when it is greater. */
    boolean setIfGreater(String key, String value);

    /** Atomically compares and stores a group of formatted time cursors. */
    boolean setDatesIfLater(Map<String, String> values, Map<String, Long> epochMillis,
                            Map<String, Long> legacyEpochMillis);

    String get(String key);

    boolean delete(String key);

    boolean hasKey(String key);

    void leftPush(String key, String value);

    void rightPush(String key, String value);

    void leftPushAll(String key, List<String> value);

    String rightPop(String key);

    String leftPop(String key);

    String rightRead(String key);

    List<String> range(String key, Integer start, Integer end);

    Long getListSize(String key);

    void hashPut(String key, String hashKey, String value);

    String hashGet(String key, String hashKey);

    Long hashIncrement(String key, String hashKey, long delta);

    Long hashDelete(String key, Object... hashKeys);

    Map<String, Object> hashGetAll(String key);

    List<Object> hashMultiGet(String key, List<String> hashKeys);

    /**
     * Determine whether the specified field exists in the Hash
     *
     * @param key Redis key
     * @param hashKey Hash field
     * @return does it exist
     */
    Boolean hashHasKey(String key, String hashKey);
}
