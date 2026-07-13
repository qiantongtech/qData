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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.qiantong.qdata.common.core.text.Convert;
import tech.qiantong.qdata.generator.domain.GenTableColumn;
import tech.qiantong.qdata.generator.mapper.GenTableColumnMapper;

import java.util.List;

/**
 * Business field service layer implementation
 *
 * @author qdata
 */
@Service
public class GenTableColumnServiceImpl implements IGenTableColumnService
{
	@Autowired
	private GenTableColumnMapper genTableColumnMapper;

	/**
     * Query business field list
     *
     * @param tableId business field number
     * @return business field collection
     */
	@Override
	public List<GenTableColumn> selectGenTableColumnListByTableId(Long tableId)
	{
	    return genTableColumnMapper.selectGenTableColumnListByTableId(tableId);
	}

    /**
     * Add new business fields
     *
     * @param genTableColumn business field information
     * @return result
     */
	@Override
	public int insertGenTableColumn(GenTableColumn genTableColumn)
	{
	    return genTableColumnMapper.insertGenTableColumn(genTableColumn);
	}

	/**
     * Modify business fields
     *
     * @param genTableColumn business field information
     * @return result
     */
	@Override
	public int updateGenTableColumn(GenTableColumn genTableColumn)
	{
	    return genTableColumnMapper.updateGenTableColumn(genTableColumn);
	}

	/**
     * Delete business field object
     *
     * @param ids data ID to be deleted
     * @return result
     */
	@Override
	public int deleteGenTableColumnByIds(String ids)
	{
		return genTableColumnMapper.deleteGenTableColumnByIds(Convert.toLongArray(ids));
	}
}
