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

package tech.qiantong.qdata.quality.utils.qualityDB;

import tech.qiantong.qdata.common.database.constants.DbType;
import tech.qiantong.qdata.quality.utils.qualityDB.dialect.*;

import java.util.HashMap;
import java.util.Map;

public class ComponentRegistry {

    private final Map<String, ComponentItem> componentItemMap = new HashMap<>();
    private final ComponentItem defaultImpl = new DefaultQuality();

    public ComponentRegistry() {
        this.componentItemMap.put(DbType.MYSQL.getDb(), new MySqlQuality());
        this.componentItemMap.put(DbType.ORACLE_12C.getDb(), new Oracle12cQuality());
        this.componentItemMap.put(DbType.ORACLE.getDb(), new OracleQuality());
        this.componentItemMap.put(DbType.SQL_SERVER.getDb(), new SQLServerQuality());
        this.componentItemMap.put(DbType.DM8.getDb(), new DM8Quality());
    }

    public ComponentItem getComponentItem(String dbCode) {
        return componentItemMap.getOrDefault(dbCode, defaultImpl);
    }

}
