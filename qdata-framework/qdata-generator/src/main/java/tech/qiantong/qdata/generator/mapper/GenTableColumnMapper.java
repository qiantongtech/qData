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

package tech.qiantong.qdata.generator.mapper;

import tech.qiantong.qdata.generator.domain.GenTableColumn;

import java.util.List;

/**
 * Business field data layer
 *
 * @author qdata
 */
public interface GenTableColumnMapper
{
    /**
     * Query column information based on table name
     *
     * @param tableName table name
     * @return column information
     */
    public List<GenTableColumn> selectDbTableColumnsByName(String tableName);

    /**
     * Query business field list
     *
     * @param tableId business field number
     * @return business field collection
     */
    public List<GenTableColumn> selectGenTableColumnListByTableId(Long tableId);

    /**
     * Add new business fields
     *
     * @param genTableColumn business field information
     * @return result
     */
    public int insertGenTableColumn(GenTableColumn genTableColumn);

    /**
     * Modify business fields
     *
     * @param genTableColumn business field information
     * @return result
     */
    public int updateGenTableColumn(GenTableColumn genTableColumn);

    /**
     * Delete business fields
     *
     * @param genTableColumns column data
     * @return result
     */
    public int deleteGenTableColumns(List<GenTableColumn> genTableColumns);

    /**
     * Delete business fields in batches
     *
     * @param ids data ID to be deleted
     * @return result
     */
    public int deleteGenTableColumnByIds(Long[] ids);
}
