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
 * For brand customization, please contact the official team for authorization.
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
 * 如需定制品牌，请通过官方渠道申请品牌授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.common.database.cache;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DefaultSqlCache extends LinkedHashMap<String, DefaultSqlCache.ExpireNode<Object>> implements SqlCache {

    private int capacity;

    private long expire;

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public DefaultSqlCache(int capacity, long expire) {
        super((int) Math.ceil(capacity / 0.75) + 1, 0.75f, true);
        // 容量
        this.capacity = capacity;
        // 固定过期时间
        this.expire = expire;
    }

    @Override
    public void put(String key, Object value, long ttl) {
        long expireTime = Long.MAX_VALUE;
        if (ttl >= 0) {
            expireTime = System.currentTimeMillis() + (ttl == 0 ? this.expire : ttl);
        }
        lock.writeLock().lock();
        try {
            // 封装成过期时间节点
            put(key, new ExpireNode<>(expireTime, value));
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Object get(String key) {
        lock.readLock().lock();
        ExpireNode<Object> expireNode;
        try {
            expireNode = super.get(key);
        } finally {
            lock.readLock().unlock();
        }
        if (expireNode == null) {
            return null;
        }
        // 惰性删除过期的
        if (this.expire > -1L && expireNode.expire < System.currentTimeMillis()) {
            try {
                lock.writeLock().lock();
                super.remove(key);
            } finally {
                lock.writeLock().unlock();
            }
            return null;
        }
        return expireNode.value;
    }

    @Override
    public void delete(String key) {
        try {
            lock.writeLock().lock();
            Iterator<Map.Entry<String, ExpireNode<Object>>> iterator = super.entrySet().iterator();
            // 清除key的缓存
            while (iterator.hasNext()) {
                Map.Entry<String, ExpireNode<Object>> entry = iterator.next();
                if (entry.getKey().equals(key)) {
                    iterator.remove();
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }


    @Override
    protected boolean removeEldestEntry(Map.Entry<String, ExpireNode<Object>> eldest) {
        if (this.expire > -1L && size() > capacity) {
            clean();
        }
        // lru淘汰
        return size() > this.capacity;
    }

    /**
     * 清理已过期的数据
     */
    private void clean() {
        try {
            lock.writeLock().lock();
            Iterator<Map.Entry<String, ExpireNode<Object>>> iterator = super.entrySet().iterator();
            long now = System.currentTimeMillis();
            while (iterator.hasNext()) {
                Map.Entry<String, ExpireNode<Object>> next = iterator.next();
                // 判断是否过期
                if (next.getValue().expire < now) {
                    iterator.remove();
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }


    /**
     * 过期时间节点
     */
    static class ExpireNode<V> {
        long expire;
        Object value;
        public ExpireNode(long expire, Object value) {
            this.expire = expire;
            this.value = value;
        }
    }
}
