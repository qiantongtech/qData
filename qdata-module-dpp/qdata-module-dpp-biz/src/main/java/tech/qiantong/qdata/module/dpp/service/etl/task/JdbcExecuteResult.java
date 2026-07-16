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
 * 数据开发 JDBC SQL 执行结果
 * <p>
 * 用于在执行器和调用方之间传递单个 SQL 节点的执行统计信息
 *
 * @author qdata
 */
@Getter
public class JdbcExecuteResult {

    /**
     * DML 语句累计影响行数
     * -- GETTER --
     *  获取影响行数
     */
    private final int updateCount;

    /**
     * 查询语句或带结果集调用返回的结果行数
     * -- GETTER --
     *  获取结果行数
     */
    private final int resultCount;

    /**
     * 创建 JDBC 执行结果。
     *
     * @param updateCount 影响行数
     * @param resultCount 结果行数
     */
    public JdbcExecuteResult(int updateCount, int resultCount) {
        this.updateCount = updateCount;
        this.resultCount = resultCount;
    }

}
