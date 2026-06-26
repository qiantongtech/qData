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

import tech.qiantong.qdata.module.system.ca.domain.CaCert;

import java.util.List;

/**
 * 证书管理Service接口
 *
 * @author qdata
 * @date 2024-08-18
 */
public interface ICaCertService
{
    /**
     * 查询证书管理
     *
     * @param id 证书管理主键
     * @return 证书管理
     */
    public CaCert selectCaCertById(Long id);

    /**
     * 查询证书管理列表
     *
     * @param caCert 证书管理
     * @return 证书管理集合
     */
    public List<CaCert> selectCaCertList(CaCert caCert);

    /**
     * 新增证书管理
     *
     * @param caCert 证书管理
     * @return 结果
     */
    public int insertCaCert(CaCert caCert);

    /**
     * 修改证书管理
     *
     * @param caCert 证书管理
     * @return 结果
     */
    public int updateCaCert(CaCert caCert);

    /**
     * 批量删除证书管理
     *
     * @param ids 需要删除的证书管理主键集合
     * @return 结果
     */
    public int deleteCaCertByIds(Long[] ids);

    /**
     * 删除证书管理信息
     *
     * @param id 证书管理主键
     * @return 结果
     */
    public int deleteCaCertById(Long id);
}
