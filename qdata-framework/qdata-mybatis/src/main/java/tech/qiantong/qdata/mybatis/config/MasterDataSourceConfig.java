package tech.qiantong.qdata.mybatis.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 主数据源配置文件
 * @author qdata
 */
@Component
public class MasterDataSourceConfig {

    private static String databaseType;

    @Value("${datasource.type}")
    public void setDatabaseType(String databaseType) {
        MasterDataSourceConfig.databaseType = databaseType;
    }

    public static String getDatabaseType() {
        return databaseType;
    }
}
