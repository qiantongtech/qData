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
