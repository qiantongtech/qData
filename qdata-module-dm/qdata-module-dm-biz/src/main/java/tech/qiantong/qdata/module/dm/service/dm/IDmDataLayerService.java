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
