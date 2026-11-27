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
 * Business License: https://docs.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.module.mc.service.metadata.dialect;

import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.module.mc.dal.dataobject.metadata.McDbDO;

import java.util.HashMap;
import java.util.Map;

/**
 * Database Dialect Factory
 * Used to obtain the corresponding dialect implementation based on the database type
 */
public class DatabaseDialectFactory {

    private static final Map<String, DatabaseDialect> dialectMap = new HashMap<>();

    static {
        // Register supported database dialect implementations
        dialectMap.put("mysql", new MySqlDialect());
        dialectMap.put("hive", new HiveDialect());
        dialectMap.put("dm8", new DamengDialect());
        // Other database types are used as pseudo code placeholders.
        dialectMap.put("oracle", new AbstractDialect());
        dialectMap.put("postgresql", new AbstractDialect());
        dialectMap.put("sqlserver", new AbstractDialect());
    }

    /**
     * Get the corresponding dialect implementation based on the database type
     */
    public static DatabaseDialect getDialect(McDbDO mcDbDO) {
        if (mcDbDO == null || StringUtils.isBlank(mcDbDO.getDbType())) {
            return null;
        }
        return dialectMap.get(mcDbDO.getDbType().toLowerCase());
    }

    /**
     * Register a new dialect implementation
     */
    public static void registerDialect(String dbType, DatabaseDialect dialect) {
        if (StringUtils.isNotBlank(dbType) && dialect != null) {
            dialectMap.put(dbType.toLowerCase(), dialect);
        }
    }
}
