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

import tech.qiantong.qdata.generator.domain.GenTable;

import java.util.List;

/**
 * Business data layer
 *
 * @author qdata
 */
public interface GenTableMapper
{
    /**
     * Query business list
     *
     * @param genTable business information
     * @return business collection
     */
    public List<GenTable> selectGenTableList(GenTable genTable);

    /**
     * Query database list
     *
     * @param genTable business information
     * @return database table collection
     */
    public List<GenTable> selectDbTableList(GenTable genTable);

    /**
     * Query database list
     *
     * @param tableNames table name group
     * @return database table collection
     */
    public List<GenTable> selectDbTableListByNames(String[] tableNames);

    /**
     * Query all table information
     *
     * @return table information collection
     */
    public List<GenTable> selectGenTableAll();

    /**
     * Query table ID business information
     *
     * @param id business ID
     * @return business information
     */
    public GenTable selectGenTableById(Long id);

    /**
     * Query table name business information
     *
     * @param tableName table name
     * @return business information
     */
    public GenTable selectGenTableByName(String tableName);

    /**
     * New business
     *
     * @param genTable business information
     * @return result
     */
    public int insertGenTable(GenTable genTable);

    /**
     * Modify business
     *
     * @param genTable business information
     * @return result
     */
    public int updateGenTable(GenTable genTable);

    /**
     * Delete businesses in batches
     *
     * @param ids data ID to be deleted
     * @return result
     */
    public int deleteGenTableByIds(Long[] ids);

    /**
     * Create table
     *
     * @param sql table structure
     * @return result
     */
    public int createTable(String sql);
}
