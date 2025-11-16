/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
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
 * 证书管理Service业务层处理
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
     * 查询证书管理
     *
     * @param id 证书管理主键
     * @return 证书管理
     */
    @Override
    public CaCert selectCaCertById(Long id)
    {
        return caCertMapper.selectCaCertById(id);
    }

    /**
     * 查询证书管理列表
     *
     * @param caCert 证书管理
     * @return 证书管理
     */
    @Override
    public List<CaCert> selectCaCertList(CaCert caCert)
    {
        return caCertMapper.selectCaCertList(caCert);
    }

    /**
     * 新增证书管理
     *
     * @param caCert 证书管理
     * @return 结果
     */
    @Override
    public int insertCaCert(CaCert caCert)
    {
        caCert.setCreateTime(DateUtils.getNowDate());
        return caCertMapper.insertCaCert(caCert);
    }

    /**
     * 修改证书管理
     *
     * @param caCert 证书管理
     * @return 结果
     */
    @Override
    public int updateCaCert(CaCert caCert)
    {
        caCert.setUpdateTime(DateUtils.getNowDate());
        return caCertMapper.updateCaCert(caCert);
    }

    /**
     * 批量删除证书管理
     *
     * @param ids 需要删除的证书管理主键
     * @return 结果
     */
    @Override
    public int deleteCaCertByIds(Long[] ids)
    {
        return caCertMapper.deleteCaCertByIds(ids);
    }

    /**
     * 删除证书管理信息
     *
     * @param id 证书管理主键
     * @return 结果
     */
    @Override
    public int deleteCaCertById(Long id)
    {
        return caCertMapper.deleteCaCertById(id);
    }
}
