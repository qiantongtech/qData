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
