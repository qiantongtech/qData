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

package tech.qiantong.qdata.module.system.mapper;

import tech.qiantong.qdata.common.core.domain.entity.SysDictType;

import java.util.List;

/**
 * Dictionary table data layer
 *
 * @author qdata
 */
public interface SysDictTypeMapper
{
    /**
     * Query dictionary types with pagination by conditions
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
     * Query information by dictionary type ID
     *
     * @param dictId dictionary type ID
     * @return dictionary type
     */
    public SysDictType selectDictTypeById(Long dictId);

    /**
     * Query information by dictionary type
     *
     * @param dictType dictionary type
     * @return dictionary type
     */
    public SysDictType selectDictTypeByType(String dictType);

    /**
     * Delete dictionary information by dictionary ID
     *
     * @param dictId dictionary ID
     * @return result
     */
    public int deleteDictTypeById(Long dictId);

    /**
     * Batch delete dictionary type information
     *
     * @param dictIds dictionary IDs to delete
     * @return result
     */
    public int deleteDictTypeByIds(Long[] dictIds);

    /**
     * Insert dictionary type information
     *
     * @param dictType dictionary type information
     * @return result
     */
    public int insertDictType(SysDictType dictType);

    /**
     * Update dictionary type information
     *
     * @param dictType dictionary type information
     * @return result
     */
    public int updateDictType(SysDictType dictType);

    /**
     * Validate whether dictionary type name is unique
     *
     * @param dictType dictionary type
     * @return result
     */
    public SysDictType checkDictTypeUnique(String dictType);
}
