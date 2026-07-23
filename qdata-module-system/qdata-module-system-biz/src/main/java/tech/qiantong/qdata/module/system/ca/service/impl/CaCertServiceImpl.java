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
import tech.qiantong.qdata.module.system.ca.domain.CaCert;
import tech.qiantong.qdata.module.system.ca.mapper.CaCertMapper;
import tech.qiantong.qdata.module.system.ca.service.ICaCertService;

import java.util.List;

/**
 * Certificate management service implementation
 *
 * @author qdata
 * @date 2024-08-18
 */
@Service
public class CaCertServiceImpl implements ICaCertService
{
    @Autowired
    private CaCertMapper caCertMapper;

    /**
     * Query certificate
     *
     * @param id certificate primary key
     * @return certificate
     */
    @Override
    public CaCert selectCaCertById(Long id)
    {
        return caCertMapper.selectCaCertById(id);
    }

    /**
     * Query certificate list
     *
     * @param caCert certificate
     * @return certificate collection
     */
    @Override
    public List<CaCert> selectCaCertList(CaCert caCert)
    {
        return caCertMapper.selectCaCertList(caCert);
    }

    /**
     * Insert certificate
     *
     * @param caCert certificate
     * @return result
     */
    @Override
    public int insertCaCert(CaCert caCert)
    {
        caCert.setCreateTime(DateUtils.getNowDate());
        return caCertMapper.insertCaCert(caCert);
    }

    /**
     * Update certificate
     *
     * @param caCert certificate
     * @return result
     */
    @Override
    public int updateCaCert(CaCert caCert)
    {
        caCert.setUpdateTime(DateUtils.getNowDate());
        return caCertMapper.updateCaCert(caCert);
    }

    /**
     * Batch delete certificates
     *
     * @param ids primary keys to delete
     * @return result
     */
    @Override
    public int deleteCaCertByIds(Long[] ids)
    {
        return caCertMapper.deleteCaCertByIds(ids);
    }

    /**
     * Delete certificate
     *
     * @param id certificate primary key
     * @return result
     */
    @Override
    public int deleteCaCertById(Long id)
    {
        return caCertMapper.deleteCaCertById(id);
    }
}
