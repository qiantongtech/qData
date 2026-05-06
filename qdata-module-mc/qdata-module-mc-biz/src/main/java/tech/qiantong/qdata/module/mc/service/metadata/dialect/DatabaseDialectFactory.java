package tech.qiantong.qdata.module.mc.service.metadata.dialect;

import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.module.mc.dal.dataobject.metadata.McDbDO;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据库方言工厂
 * 用于根据数据库类型获取对应的方言实现
 */
public class DatabaseDialectFactory {

    private static final Map<String, DatabaseDialect> dialectMap = new HashMap<>();

    static {
        // 注册支持的数据库方言实现
        dialectMap.put("mysql", new MySqlDialect());
        dialectMap.put("hive", new HiveDialect());
        dialectMap.put("dm8", new DamengDialect());
        // 其他数据库类型做伪代码占位
        dialectMap.put("oracle", new AbstractDialect());
        dialectMap.put("postgresql", new AbstractDialect());
        dialectMap.put("sqlserver", new AbstractDialect());
    }

    /**
     * 根据数据库类型获取对应的方言实现
     */
    public static DatabaseDialect getDialect(McDbDO mcDbDO) {
        if (mcDbDO == null || StringUtils.isBlank(mcDbDO.getDbType())) {
            return null;
        }
        return dialectMap.get(mcDbDO.getDbType().toLowerCase());
    }

    /**
     * 注册新的方言实现
     */
    public static void registerDialect(String dbType, DatabaseDialect dialect) {
        if (StringUtils.isNotBlank(dbType) && dialect != null) {
            dialectMap.put(dbType.toLowerCase(), dialect);
        }
    }
}
