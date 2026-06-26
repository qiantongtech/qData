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

package tech.qiantong.qdata.module.dm.service.dm;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataDomainPageReqVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataDomainRespVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataDomainSaveReqVO;
import tech.qiantong.qdata.module.dm.dal.dataobject.dm.DmDataDomainDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 数据域管理Service接口
 *
 * @author FXB
 * @date 2026-03-24
 */
public interface IDmDataDomainService extends IService<DmDataDomainDO> {

    /**
     * 获得数据域管理分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据域管理分页列表
     */
    PageResult<DmDataDomainDO> getDmDataDomainPage(DmDataDomainPageReqVO pageReqVO);

    /**
     * 创建数据域管理
     *
     * @param createReqVO 数据域管理信息
     * @return 数据域管理编号
     */
    Long createDmDataDomain(DmDataDomainSaveReqVO createReqVO);

    /**
     * 更新数据域管理
     *
     * @param updateReqVO 数据域管理信息
     */
    int updateDmDataDomain(DmDataDomainSaveReqVO updateReqVO);

    /**
     * 删除数据域管理
     *
     * @param idList 数据域管理编号
     */
    int removeDmDataDomain(Collection<Long> idList);

    /**
     * 获得数据域管理详情
     *
     * @param id 数据域管理编号
     * @return 数据域管理
     */
    DmDataDomainDO getDmDataDomainById(Long id);

    /**
     * 获得全部数据域管理列表
     *
     * @return 数据域管理列表
     */
    List<DmDataDomainDO> getDmDataDomainList();

    /**
     * 获得全部数据域管理 Map
     *
     * @return 数据域管理 Map
     */
    Map<Long, DmDataDomainDO> getDmDataDomainMap();


    /**
     * 导入数据域管理数据
     *
     * @param importExcelList 数据域管理数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDmDataDomain(List<DmDataDomainRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 根据业务分类id查询数据域
     *
     * @param dmDataDomain
     * @return
     */
    PageResult<DmDataDomainDO> getDmDataDomainByCategoryId(DmDataDomainPageReqVO dmDataDomain);
}
