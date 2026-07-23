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

package tech.qiantong.qdata.generator.service;

import tech.qiantong.qdata.common.core.domain.entity.SysDictType;
import tech.qiantong.qdata.generator.domain.GenTable;

import java.util.List;
import java.util.Map;

/**
 * Business service layer
 *
 * @author qdata
 */
public interface IGenTableService
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
     * Query business information
     *
     * @param id business ID
     * @return business information
     */
    public GenTable selectGenTableById(Long id);

    /**
     * Modify business
     *
     * @param genTable business information
     * @return result
     */
    public void updateGenTable(GenTable genTable);

    /**
     * Delete business information
     *
     * @param tableIds table data ID to be deleted
     * @return result
     */
    public void deleteGenTableByIds(Long[] tableIds);

    /**
     * Create table
     *
     * @param sql create table statement
     * @return result
     */
    public boolean createTable(String sql);

    /**
     * Import table structure
     *
     * @param tableList import table list
     * @param operName operator
     */
    public void importGenTable(List<GenTable> tableList, String operName);

    /**
     * Preview code
     *
     * @param tableId table number
     * @return preview data list
     */
    public Map<String, String> previewCode(Long tableId);

    /**
     * Generate code (download method)
     *
     * @param tableName table name
     * @return data
     */
    public byte[] downloadCode(String tableName);

    /**
     * Generate code (custom path)
     *
     * @param tableName table name
     * @return data
     */
    public void generatorCode(String tableName);

    /**
     * Sync database
     *
     * @param tableName table name
     */
    public void synchDb(String tableName);

    /**
     * Generate codes in batches (download method)
     *
     * @param tableNames table array
     * @return data
     */
    public byte[] downloadCode(String[] tableNames);

    /**
     * Modify and save parameter verification
     *
     * @param genTable business information
     */
    public void validateEdit(GenTable genTable);

    /**
     * Batch generation of dictionary enumeration classes (download method)
     *
     * @param dictTypesList dictionary type array
     * @return data
     */
    byte[] downloadEnums(List<SysDictType> dictTypesList);
}
