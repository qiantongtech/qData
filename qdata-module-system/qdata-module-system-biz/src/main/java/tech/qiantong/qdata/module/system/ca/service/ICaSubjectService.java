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

package tech.qiantong.qdata.module.system.ca.service;

import tech.qiantong.qdata.module.system.ca.domain.CaSubject;

import java.util.List;

/**
 * Subject management service interface
 *
 * @author qdata
 * @date 2024-08-18
 */
public interface ICaSubjectService
{
    /**
     * Query subject
     *
     * @param id subject primary key
     * @return subject
     */
    public CaSubject selectCaSubjectById(Long id);

    /**
     * Query subject list
     *
     * @param caSubject subject
     * @return subject collection
     */
    public List<CaSubject> selectCaSubjectList(CaSubject caSubject);

    /**
     * Insert subject
     *
     * @param caSubject subject
     * @return result
     */
    public int insertCaSubject(CaSubject caSubject);

    /**
     * Update subject
     *
     * @param caSubject subject
     * @return result
     */
    public int updateCaSubject(CaSubject caSubject);

    /**
     * Batch delete subjects
     *
     * @param ids primary keys to delete
     * @return result
     */
    public int deleteCaSubjectByIds(Long[] ids);

    /**
     * Delete subject
     *
     * @param id subject primary key
     * @return result
     */
    public int deleteCaSubjectById(Long id);
}
