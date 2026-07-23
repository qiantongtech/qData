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

import java.util.List;

/**
 * Dictionary service layer
 *
 * @author qdata
 */
public interface ISysDictDataService
{
    /**
     * Query dictionary data by conditions with pagination
     *
     * @param dictData dictionary data information
     * @return dictionary data collection
     */
    public List<SysDictData> selectDictDataList(SysDictData dictData);

    /**
     * Query dictionary data information by dictionary type and key value
     *
     * @param dictType dictionary type
     * @param dictValue dictionary key value
     * @return dictionary label
     */
    public String selectDictLabel(String dictType, String dictValue);

    /**
     * Query dictionary data information by ID
     *
     * @param dictCode dictionary data ID
     * @return dictionary data
     */
    public SysDictData selectDictDataById(Long dictCode);

    /**
     * Batch delete dictionary data information
     *
     * @param dictCodes dictionary data IDs to delete
     */
    public void deleteDictDataByIds(Long[] dictCodes);

    /**
     * Insert and save dictionary data information
     *
     * @param dictData dictionary data information
     * @return result
     */
    public int insertDictData(SysDictData dictData);

    /**
     * Update and save dictionary data information
     *
     * @param dictData dictionary data information
     * @return result
     */
    public int updateDictData(SysDictData dictData);
}
