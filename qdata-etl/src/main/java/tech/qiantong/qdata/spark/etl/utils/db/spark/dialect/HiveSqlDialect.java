package tech.qiantong.qdata.spark.etl.utils.db.spark.dialect;

import org.apache.spark.sql.jdbc.JdbcDialect;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * <P>
 * 用途:对spark做hive方言的支持
 * </p>
 *
 * @author: FXB
 * @create: 2025-05-12 15:32
 **/
public class HiveSqlDialect extends JdbcDialect {
    @Override
    public boolean canHandle(String url) {
        return url.toLowerCase(Locale.ROOT).startsWith("jdbc:hive2");
    }

    @Override
    public String quoteIdentifier(String colName) {
        return Arrays.stream(colName.split("\\."))
                .map(part -> "`" + part + "`")
                .collect(Collectors.joining("."));
    }

}
