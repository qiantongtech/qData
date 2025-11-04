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
