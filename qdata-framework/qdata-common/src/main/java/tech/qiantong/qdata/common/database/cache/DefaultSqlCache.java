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
        // Capacity
        this.capacity = capacity;
        // Fixed expiration time
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
            // Wrap as expiration time node
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
        // Lazy deletion of expired entries
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
            // Clear the cache entry for the given key
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
        // LRU eviction
        return size() > this.capacity;
    }

    /**
     * Clean up expired data
     */
    private void clean() {
        try {
            lock.writeLock().lock();
            Iterator<Map.Entry<String, ExpireNode<Object>>> iterator = super.entrySet().iterator();
            long now = System.currentTimeMillis();
            while (iterator.hasNext()) {
                Map.Entry<String, ExpireNode<Object>> next = iterator.next();
                // Check if expired
                if (next.getValue().expire < now) {
                    iterator.remove();
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }


    /**
     * Expiration time node
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
