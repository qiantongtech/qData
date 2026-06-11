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
