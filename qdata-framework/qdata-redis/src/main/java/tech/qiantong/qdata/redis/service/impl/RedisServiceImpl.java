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

package tech.qiantong.qdata.redis.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tech.qiantong.qdata.redis.service.IRedisService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Redis接口
 *
 * @author: tzh
 * @date: 2018年6月3日 下午4:41:13
 */
@Service
public class RedisServiceImpl implements IRedisService {
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
