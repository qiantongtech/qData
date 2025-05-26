package tech.qiantong.qdata.common.database.datasource;


import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary // 标记为默认使用的 DataSourceFactory
public class DefaultDataSourceFactoryBean extends AbstractDataSourceFactory {
}
