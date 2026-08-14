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

package tech.qiantong.qdata.redis.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import tech.qiantong.qdata.redis.service.IRedisService;

import java.math.BigInteger;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Redis interface
 *
 * @author: tzh
 * @date: June 3, 2018 4:41:13 pm
 */
@Service
public class RedisServiceImpl implements IRedisService {
    /**
     * Atomically compares non-negative decimal strings by normalized length and
     * lexicographical order, avoiding precision loss in the Redis Lua number type.
     */
    private static final DefaultRedisScript<Long> SET_IF_GREATER_SCRIPT =
            new DefaultRedisScript<Long>(
                    "local old = redis.call('get', KEYS[1]); "
                            + "local new = ARGV[1]; "
                            + "if (not old) or (not string.match(old, '^%d+$')) then "
                            + "redis.call('set', KEYS[1], new); return 1; end; "
                            + "old = string.gsub(old, '^0+', ''); if old == '' then old = '0'; end; "
                            + "if (#new > #old) or (#new == #old and new > old) then "
                            + "redis.call('set', KEYS[1], new); return 1; end; return 0;",
                    Long.class);
    /**
     * Compares all time cursors first and then applies the eligible updates in one Lua call.
     * ARGV contains value, candidate epoch and legacy epoch for each pair of Redis keys.
     */
    private static final DefaultRedisScript<Long> SET_DATES_IF_LATER_SCRIPT =
            new DefaultRedisScript<Long>(
                    "local updates = {}; local changed = 0; "
                            + "for i = 1, #KEYS, 2 do "
                            + "local arg = ((i - 1) / 2) * 3; "
                            + "local oldEpoch = redis.call('get', KEYS[i + 1]); "
                            + "if not oldEpoch then oldEpoch = ARGV[arg + 3]; end; "
                            + "local newEpoch = ARGV[arg + 2]; "
                            + "if (not oldEpoch) or (#newEpoch > #oldEpoch) "
                            + "or (#newEpoch == #oldEpoch and newEpoch > oldEpoch) then "
                            + "table.insert(updates, {KEYS[i], KEYS[i + 1], ARGV[arg + 1], newEpoch}); "
                            + "end; end; "
                            + "for _, update in ipairs(updates) do "
                            + "redis.call('set', update[1], update[3]); "
                            + "redis.call('set', update[2], update[4]); changed = changed + 1; end; "
                            + "return changed;",
                    Long.class);
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void set(String key, String value, long timeout) {
        stringRedisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    /** Validates and atomically advances a non-negative integer cursor. */
    @Override
    public boolean setIfGreater(String key, String value) {
        // Normalize the input before Lua compares it with the stored decimal string.
        BigInteger integerValue = new BigInteger(value);
        // ID cursors are non-negative; reject invalid callers before touching Redis.
        if (integerValue.signum() < 0) {
            throw new IllegalArgumentException("value must be a non-negative integer");
        }
        Long result = stringRedisTemplate.execute(
                SET_IF_GREATER_SCRIPT, Collections.singletonList(key), integerValue.toString());
        return Long.valueOf(1L).equals(result);
    }

    /** Validates all candidates and submits one atomic multi-cursor comparison to Redis. */
    @Override
    public boolean setDatesIfLater(Map<String, String> values, Map<String, Long> epochMillis,
                                   Map<String, Long> legacyEpochMillis) {
        // An empty candidate set requires no Redis call and cannot advance a cursor.
        if (values.isEmpty()) {
            return false;
        }
        List<String> keys = new ArrayList<>();
        List<String> arguments = new ArrayList<>();
        for (Map.Entry<String, String> entry : values.entrySet()) {
            Long candidateEpoch = epochMillis.get(entry.getKey());
            // Every formatted value must have a valid numeric comparison value.
            if (candidateEpoch == null || candidateEpoch < 0) {
                throw new IllegalArgumentException("epochMillis must contain a non-negative value for every key");
            }
            keys.add(entry.getKey());
            keys.add(entry.getKey() + ":epoch");
            arguments.add(entry.getValue());
            arguments.add(String.valueOf(candidateEpoch));
            Long legacyEpoch = legacyEpochMillis.get(entry.getKey());
            // An empty legacy epoch tells Lua that no pre-migration cursor exists.
            arguments.add(legacyEpoch == null ? "" : String.valueOf(legacyEpoch));
        }
        Long result = stringRedisTemplate.execute(SET_DATES_IF_LATER_SCRIPT, keys,
                arguments.toArray(new String[0]));
        return result != null && result > 0;
    }

    @Override
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    @Override
    public boolean delete(String key) {
        return stringRedisTemplate.delete(key);
    }

    @Override
    public boolean hasKey(String key) {
        return stringRedisTemplate.hasKey(key);
    }

    @Override
    public void leftPush(String key, String value) {
        stringRedisTemplate.opsForList().leftPush(key, value);
    }

    @Override
    public void rightPush(String key, String value) {
        stringRedisTemplate.opsForList().rightPush(key, value);
    }

    @Override
    public void leftPushAll(String key, List<String> values) {
        stringRedisTemplate.opsForList().leftPushAll(key, values);
    }

    @Override
    public String rightPop(String key) {
        return stringRedisTemplate.opsForList().rightPop(key);
    }

    @Override
    public String leftPop(String key) {
        return stringRedisTemplate.opsForList().leftPop(key);
    }

    @Override
    public String rightRead(String key) {
        Long length = stringRedisTemplate.opsForList().size(key);
        return stringRedisTemplate.opsForList().index(key,length - 1);
    }

    @Override
    public List<String> range(String key, Integer start, Integer end) {
        return stringRedisTemplate.opsForList().range(key, start, end);
    }

    @Override
    public Long getListSize(String key) {
        return stringRedisTemplate.opsForList().size(key);
    }

    @Override
    public void hashPut(String key, String hashKey, String value) {
        stringRedisTemplate.opsForHash().put(key, hashKey, value);
    }

    @Override
    public String hashGet(String key, String hashKey) {
        return (String) stringRedisTemplate.opsForHash().get(key, hashKey);
    }

    @Override
    public Long hashIncrement(String key, String hashKey, long delta) {
        return stringRedisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    @Override
    public Long hashDelete(String key, Object... hashKeys) {
        return stringRedisTemplate.opsForHash().delete(key, hashKeys);
    }

    @Override
    public Map<String, Object> hashGetAll(String key) {
        return stringRedisTemplate.<String, Object>opsForHash().entries(key);
    }

    @Override
    public List<Object> hashMultiGet(String key, List<String> hashKeys) {
        return stringRedisTemplate.<String, Object>opsForHash().multiGet(key, hashKeys);
    }

    @Override
    public Boolean hashHasKey(String key, String hashKey) {
        return stringRedisTemplate.opsForHash().hasKey(key, hashKey);
    }
}
