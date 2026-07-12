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

package tech.qiantong.qdata.module.system.ca.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.qiantong.qdata.common.utils.DateUtils;
import tech.qiantong.qdata.module.system.ca.domain.CaSubject;
import tech.qiantong.qdata.module.system.ca.mapper.CaSubjectMapper;
import tech.qiantong.qdata.module.system.ca.service.ICaSubjectService;

import java.util.List;

/**
 * Subject management service implementation
 *
 * @author qdata
 * @date 2024-08-18
 */
@Service
public class CaSubjectServiceImpl implements ICaSubjectService
{
    @Autowired
    private CaSubjectMapper caSubjectMapper;

    /**
     * Query subject
     *
     * @param id subject primary key
     * @return subject
     */
    @Override
    public CaSubject selectCaSubjectById(Long id)
    {
        return caSubjectMapper.selectCaSubjectById(id);
    }

    /**
     * Query subject list
     *
     * @param caSubject subject
     * @return subject collection
     */
    @Override
    public List<CaSubject> selectCaSubjectList(CaSubject caSubject)
    {
        return caSubjectMapper.selectCaSubjectList(caSubject);
    }

    /**
     * Insert subject
     *
     * @param caSubject subject
     * @return result
     */
    @Override
    public int insertCaSubject(CaSubject caSubject)
    {
        caSubject.setCreateTime(DateUtils.getNowDate());
        return caSubjectMapper.insertCaSubject(caSubject);
    }

    /**
     * Update subject
     *
     * @param caSubject subject
     * @return result
     */
    @Override
    public int updateCaSubject(CaSubject caSubject)
    {
        caSubject.setUpdateTime(DateUtils.getNowDate());
        return caSubjectMapper.updateCaSubject(caSubject);
    }

    /**
     * Batch delete subjects
     *
     * @param ids primary keys to delete
     * @return result
     */
    @Override
    public int deleteCaSubjectByIds(Long[] ids)
    {
        return caSubjectMapper.deleteCaSubjectByIds(ids);
    }

    /**
     * Delete subject
     *
     * @param id subject primary key
     * @return result
     */
    @Override
    public int deleteCaSubjectById(Long id)
    {
        return caSubjectMapper.deleteCaSubjectById(id);
    }
}
