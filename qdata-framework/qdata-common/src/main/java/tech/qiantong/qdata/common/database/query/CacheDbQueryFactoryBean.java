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

package tech.qiantong.qdata.common.database.query;

import tech.qiantong.qdata.common.database.cache.DefaultSqlCache;
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.database.core.DbTable;
import tech.qiantong.qdata.common.database.core.PageResult;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CacheDbQueryFactoryBean extends AbstractDbQueryFactory {

    /**
     * Default cache is 5 minutes
     */
    private static long DEFAULT_EXPIRE = 5 * 60 * 1000;
    private static DefaultSqlCache sqlCache = new DefaultSqlCache(100, DEFAULT_EXPIRE);

    private <T> T putCacheValue(String key, T value, long ttl) {
        sqlCache.put(key, value, ttl);
        return value;
    }

    @Override
    public List<DbColumn> getTableColumns(String dbName, String tableName) {
        Object[] args = new Object[]{dbName, tableName};
        Optional.ofNullable(sqlCache.get(sqlCache.buildSqlCacheKey(super.dataSource.toString() + ":getTableColumns", args)));
        return super.getTableColumns(dbName, tableName);
    }

    @Override
    public List<DbTable> getTables(String dbName) {
        Object[] args = new Object[]{dbName};
        String cacheKey = sqlCache.buildSqlCacheKey(super.dataSource.toString() + ":getTables", args);
        return (List<DbTable>) Optional.ofNullable(sqlCache.get(cacheKey))
                .orElse(putCacheValue(cacheKey, super.getTables(dbName), DEFAULT_EXPIRE));
    }

    @Override
    public int count(String sql) {
        String cacheKey = sqlCache.buildSqlCacheKey(super.dataSource.toString() + ":" + sql, null);
        return (int) Optional.ofNullable(sqlCache.get(cacheKey))
                .orElse(putCacheValue(cacheKey, super.count(sql), DEFAULT_EXPIRE));
    }

    @Override
    public int count(String sql, Object[] args) {
        String cacheKey = sqlCache.buildSqlCacheKey(super.dataSource.toString() + ":" + sql, args);
        return (int) Optional.ofNullable(sqlCache.get(cacheKey))
                .orElse(putCacheValue(cacheKey, super.count(sql, args), DEFAULT_EXPIRE));
    }

    @Override
    public int count(String sql, Map<String, Object> params) {
        String cacheKey = sqlCache.buildSqlCacheKey(super.dataSource.toString() + ":" + sql, params.values().toArray());
        return (int) Optional.ofNullable(sqlCache.get(cacheKey))
                .orElse(putCacheValue(cacheKey, super.count(sql, params), DEFAULT_EXPIRE));
    }
    @Override
    public int countNew(String sql, Map<String, Object> params) {
        String cacheKey = sqlCache.buildSqlCacheKey(super.dataSource.toString() + ":" + sql, params.values().toArray());
        return (int) Optional.ofNullable(sqlCache.get(cacheKey))
                .orElse(putCacheValue(cacheKey, super.countNew(sql, params), DEFAULT_EXPIRE));
    }

    @Override
    public List<Map<String, Object>> queryList(String sql) {
        String cacheKey = sqlCache.buildSqlCacheKey(super.dataSource.toString() + ":" + sql, null);
        return (List<Map<String, Object>>) Optional.ofNullable(sqlCache.get(cacheKey))
                .orElse(putCacheValue(cacheKey, super.queryList(sql), DEFAULT_EXPIRE));
    }

    @Override
    public List<Map<String, Object>> queryList(String sql, Object[] args) {
        String cacheKey = sqlCache.buildSqlCacheKey(super.dataSource.toString() + ":" + sql, args);
        return (List<Map<String, Object>>) Optional.ofNullable(sqlCache.get(cacheKey))
                .orElse(putCacheValue(cacheKey, super.queryList(sql, args), DEFAULT_EXPIRE));
    }

    @Override
    public List<Map<String, Object>> queryList(String sql, Map<String, Object> params, Integer cache) {
        if (cache != 1) {
            return super.queryList(sql, params, null);
        }
        Object[] args = params.values().toArray();
        String cacheKey = sqlCache.buildSqlCacheKey(super.dataSource.toString() + ":" + sql, args);
        return (List<Map<String, Object>>) Optional.ofNullable(sqlCache.get(cacheKey))
                .orElse(putCacheValue(cacheKey, super.queryList(sql, params, null), DEFAULT_EXPIRE));
    }

    @Override
    public Map<String, Object> queryOne(String sql, Map<String, Object> params, Integer cache) {
        if (cache != 1) {
            return super.queryOne(sql, params, null);
        }
        Object[] args = params.values().toArray();
        String cacheKey = sqlCache.buildSqlCacheKey(super.dataSource.toString() + ":" + sql, args);
        return (Map<String, Object>) Optional.ofNullable(sqlCache.get(cacheKey))
                .orElse(putCacheValue(cacheKey, super.queryOne(sql, params, null), DEFAULT_EXPIRE));
    }

    @Override
    public PageResult<Map<String, Object>> queryByPage(String sql, long offset, long size) {
        return super.queryByPage(sql, offset, size);
    }

    @Override
    public PageResult<Map<String, Object>> queryByPage(String sql, Object[] args, long offset, long size) {
        Object[] objects = Arrays.copyOf(args, args.length + 2);
        objects[args.length] = offset;
        objects[args.length + 1] = size;
        String cacheKey = sqlCache.buildSqlCacheKey(super.dataSource.toString() + ":" + sql, objects);
        return (PageResult<Map<String, Object>>) Optional.ofNullable(sqlCache.get(cacheKey))
                .orElse(putCacheValue(cacheKey, super.queryByPage(sql, args, offset, size), DEFAULT_EXPIRE));
    }

    @Override
    public PageResult<Map<String, Object>> queryByPage(String sql, Map<String, Object> params, long offset, long size, Integer cache) {
        if (cache != 1) {
            return super.queryByPage(sql, params, offset, size, null);
        }
        Object[] args = params.values().toArray();
        Object[] objects = Arrays.copyOf(args, args.length + 2);
        objects[args.length] = offset;
        objects[args.length + 1] = size;
        String cacheKey = sqlCache.buildSqlCacheKey(super.dataSource.toString() + ":" + sql, objects);
        return (PageResult<Map<String, Object>>) Optional.ofNullable(sqlCache.get(cacheKey))
                .orElse(putCacheValue(cacheKey, super.queryByPage(sql, params, offset, size, null), DEFAULT_EXPIRE));
    }
}
