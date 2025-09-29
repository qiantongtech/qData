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
