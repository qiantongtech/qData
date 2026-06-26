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
