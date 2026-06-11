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
 */

package tech.qiantong.qdata.common.database.dialect;


import tech.qiantong.qdata.common.database.DbDialect;
import tech.qiantong.qdata.common.database.constants.DbType;

import java.util.EnumMap;
import java.util.Map;

public class DialectRegistry {

    private final Map<DbType, DbDialect> dialect_enum_map = new EnumMap<>(DbType.class);

    public DialectRegistry() {
        dialect_enum_map.put(DbType.MARIADB, new MariaDBDialect());
        dialect_enum_map.put(DbType.MYSQL, new MySqlDialect());
        dialect_enum_map.put(DbType.ORACLE_12C, new Oracle12cDialect());
        dialect_enum_map.put(DbType.ORACLE, new OracleDialect());
        dialect_enum_map.put(DbType.POSTGRE_SQL, new PostgreDialect());
        dialect_enum_map.put(DbType.SQL_SERVER2008, new SQLServer2008Dialect());
        dialect_enum_map.put(DbType.SQL_SERVER, new SQLServerDialect());
        dialect_enum_map.put(DbType.DM8, new DM8Dialect());
        dialect_enum_map.put(DbType.OTHER, new UnknownDialect());
        dialect_enum_map.put(DbType.KINGBASE8, new Kingbase8Dialect());
        dialect_enum_map.put(DbType.KAFKA, new KafkaDialect());
        dialect_enum_map.put(DbType.DORIS, new DorisDialect());
    }

    public DbDialect getDialect(DbType dbType) {
        return dialect_enum_map.get(dbType);
    }
}
