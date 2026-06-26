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

import java.util.List;
import java.util.Map;
import java.util.Collection;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataLayerRespVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataLayerSaveReqVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataLayerPageReqVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataLayerTreeRespVO;
import tech.qiantong.qdata.module.dm.dal.dataobject.dm.DmDataLayerDO;

/**
 * 数仓分层管理Service接口
 *
 * @author FXB
 * @date 2026-03-24
 */
public interface IDmDataLayerService extends IService<DmDataLayerDO> {

    /**
     * 获得数仓分层管理分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数仓分层管理分页列表
     */
    PageResult<DmDataLayerDO> getDmDataLayerPage(DmDataLayerPageReqVO pageReqVO);

    /**
     * 创建数仓分层管理
     *
     * @param createReqVO 数仓分层管理信息
     * @return 数仓分层管理编号
     */
    Long createDmDataLayer(DmDataLayerSaveReqVO createReqVO);

    /**
     * 更新数仓分层管理
     *
     * @param updateReqVO 数仓分层管理信息
     */
    int updateDmDataLayer(DmDataLayerSaveReqVO updateReqVO);

    /**
     * 删除数仓分层管理
     *
     * @param idList 数仓分层管理编号
     */
    int removeDmDataLayer(Collection<Long> idList);

    /**
     * 获得数仓分层管理详情
     *
     * @param id 数仓分层管理编号
     * @return 数仓分层管理
     */
    DmDataLayerDO getDmDataLayerById(Long id);

    /**
     * 获得全部数仓分层管理列表
     *
     * @return 数仓分层管理列表
     */
    List<DmDataLayerDO> getDmDataLayerList();

    /**
     * 获得全部数仓分层管理 Map
     *
     * @return 数仓分层管理 Map
     */
    Map<Long, DmDataLayerDO> getDmDataLayerMap();


    /**
     * 导入数仓分层管理数据
     *
     * @param importExcelList 数仓分层管理数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    String importDmDataLayer(List<DmDataLayerRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 获取数仓分层管理树列表
     *
     * @return 数仓分层管理树列表
     */
    List<DmDataLayerTreeRespVO> tree();
}
