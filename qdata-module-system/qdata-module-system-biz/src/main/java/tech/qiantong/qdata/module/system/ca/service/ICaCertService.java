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
