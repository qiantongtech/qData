package tech.qiantong.qdata.redis.service;

import java.util.List;
import java.util.Map;

public interface IRedisService {

    /**
     * 设置
     *
     * @param key
     * @param value
     */
    void set(String key, String value);

    /**
     * 设置，带超时
     *
     * @param key
     * @param value
     * @param timeout
     */
    void set(String key, String value, long timeout);

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
     * 判断 Hash 中是否存在指定字段
     *
     * @param key     Redis 键
     * @param hashKey Hash 字段
     * @return 是否存在
     */
    Boolean hashHasKey(String key, String hashKey);
}
