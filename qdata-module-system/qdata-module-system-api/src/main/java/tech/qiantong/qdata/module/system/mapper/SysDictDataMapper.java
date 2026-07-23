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

import org.apache.ibatis.annotations.Param;
import tech.qiantong.qdata.common.core.domain.entity.SysDictData;

import java.util.List;

/**
 * Dictionary data table data layer
 *
 * @author qdata
 */
public interface SysDictDataMapper
{
    /**
     * Query dictionary data with pagination by conditions
     *
     * @param dictData dictionary data information
     * @return dictionary data collection
     */
    public List<SysDictData> selectDictDataList(SysDictData dictData);

    /**
     * Query dictionary data by dictionary type
     *
     * @param dictType dictionary type
     * @return dictionary data collection
     */
    public List<SysDictData> selectDictDataByType(String dictType);

    /**
     * Query dictionary data by dictionary type and dictionary key value
     *
     * @param dictType dictionary type
     * @param dictValue dictionary key value
     * @return dictionary label
     */
    public String selectDictLabel(@Param("dictType") String dictType, @Param("dictValue") String dictValue);

    /**
     * Query information by dictionary data ID
     *
     * @param dictCode dictionary data ID
     * @return dictionary data
     */
    public SysDictData selectDictDataById(Long dictCode);

    /**
     * Query dictionary data
     *
     * @param dictType dictionary type
     * @return dictionary data
     */
    public int countDictDataByType(String dictType);

    /**
     * Delete dictionary data information by dictionary ID
     *
     * @param dictCode dictionary data ID
     * @return result
     */
    public int deleteDictDataById(Long dictCode);

    /**
     * Batch delete dictionary data information
     *
     * @param dictCodes dictionary data IDs to delete
     * @return result
     */
    public int deleteDictDataByIds(Long[] dictCodes);

    /**
     * Insert dictionary data information
     *
     * @param dictData dictionary data information
     * @return result
     */
    public int insertDictData(SysDictData dictData);

    /**
     * Update dictionary data information
     *
     * @param dictData dictionary data information
     * @return result
     */
    public int updateDictData(SysDictData dictData);

    /**
     * Update dictionary type synchronously
     *
     * @param oldDictType old dictionary type
     * @param newDictType new dictionary type
     * @return result
     */
    public int updateDictDataType(@Param("oldDictType") String oldDictType, @Param("newDictType") String newDictType);
}
