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

package tech.qiantong.qdata.module.dpp.service.etl.task;

import lombok.Getter;

/**
 * JDBC SQL execution result for data development.
 * <p>
 * Transfers execution statistics for a single SQL node between the executor and caller.
 *
 * @author qdata
 */
@Getter
public class JdbcExecuteResult {

    /**
     * Total rows affected by DML statements.
     * -- GETTER --
     * Returns the affected row count.
     */
    private final int updateCount;

    /**
     * Number of rows returned by a query or an invocation with a result set.
     * -- GETTER --
     * Returns the result row count.
     */
    private final int resultCount;

    /**
     * Creates a JDBC execution result.
     *
     * @param updateCount affected row count
     * @param resultCount result row count
     */
    public JdbcExecuteResult(int updateCount, int resultCount) {
        this.updateCount = updateCount;
        this.resultCount = resultCount;
    }

}
