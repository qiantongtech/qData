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

package tech.qiantong.qdata.module.system.ca.mapper;

import tech.qiantong.qdata.module.system.ca.domain.CaCert;

import java.util.List;

/**
 * Certificate management mapper interface
 *
 * @author qdata
 * @date 2024-08-18
 */
public interface CaCertMapper
{
    /**
     * Query certificate
     *
     * @param id certificate primary key
     * @return certificate
     */
    public CaCert selectCaCertById(Long id);

    /**
     * Query certificate list
     *
     * @param caCert certificate
     * @return certificate collection
     */
    public List<CaCert> selectCaCertList(CaCert caCert);

    /**
     * Insert certificate
     *
     * @param caCert certificate
     * @return result
     */
    public int insertCaCert(CaCert caCert);

    /**
     * Update certificate
     *
     * @param caCert certificate
     * @return result
     */
    public int updateCaCert(CaCert caCert);

    /**
     * Delete certificate
     *
     * @param id certificate primary key
     * @return result
     */
    public int deleteCaCertById(Long id);

    /**
     * Batch delete certificates
     *
     * @param ids primary keys to delete
     * @return result
     */
    public int deleteCaCertByIds(Long[] ids);
}
