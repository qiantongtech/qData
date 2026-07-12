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

package tech.qiantong.qdata.module.system.service;

import tech.qiantong.qdata.common.core.domain.entity.SysDictData;
import tech.qiantong.qdata.common.core.domain.entity.SysDictType;

import java.util.List;

/**
 * Dictionary service layer
 *
 * @author qdata
 */
public interface ISysDictTypeService
{
    /**
     * Query dictionary types by conditions with pagination
     *
     * @param dictType dictionary type information
     * @return dictionary type collection
     */
    public List<SysDictType> selectDictTypeList(SysDictType dictType);

    /**
     * Query all dictionary types
     *
     * @return dictionary type collection
     */
    public List<SysDictType> selectDictTypeAll();

    /**
     * Query dictionary data by dictionary type
     *
     * @param dictType dictionary type
     * @return dictionary data collection
     */
    public List<SysDictData> selectDictDataByType(String dictType);

    /**
     * Query dictionary type information by ID
     *
     * @param dictId dictionary type ID
     * @return dictionary type
     */
    public SysDictType selectDictTypeById(Long dictId);

    /**
     * Query dictionary type information by type
     *
     * @param dictType dictionary type
     * @return dictionary type
     */
    public SysDictType selectDictTypeByType(String dictType);

    /**
     * Batch delete dictionary information
     *
     * @param dictIds dictionary IDs to delete
     */
    public void deleteDictTypeByIds(Long[] dictIds);

    /**
     * Load dictionary cache data
     */
    public void loadingDictCache();

    /**
     * Clear dictionary cache data
     */
    public void clearDictCache();

    /**
     * Reset dictionary cache data
     */
    public void resetDictCache();

    /**
     * Insert and save dictionary type information
     *
     * @param dictType dictionary type information
     * @return result
     */
    public int insertDictType(SysDictType dictType);

    /**
     * Update and save dictionary type information
     *
     * @param dictType dictionary type information
     * @return result
     */
    public int updateDictType(SysDictType dictType);

    /**
     * Check if dictionary type name is unique
     *
     * @param dictType dictionary type
     * @return result
     */
    public boolean checkDictTypeUnique(SysDictType dictType);

    // Get dictionary type and dictionary data
    List<SysDictType> getDictTypeAndDataList(String[] dictTypesArr);
}
