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
 */

package tech.qiantong.qdata.module.dm.service.dm;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataLayerSpecificationRespVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataLayerSpecificationSaveReqVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataLayerSpecificationPageReqVO;
import tech.qiantong.qdata.module.dm.dal.dataobject.dm.DmDataLayerSpecificationDO;
/**
 * 数仓分层-规范管理Service接口
 *
 * @author FXB
 * @date 2026-03-24
 */
public interface IDmDataLayerSpecificationService extends IService<DmDataLayerSpecificationDO> {

    /**
     * 获得数仓分层-规范管理分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数仓分层-规范管理分页列表
     */
    PageResult<DmDataLayerSpecificationDO> getDmDataLayerSpecificationPage(DmDataLayerSpecificationPageReqVO pageReqVO);

    /**
     * 创建数仓分层-规范管理
     *
     * @param createReqVO 数仓分层-规范管理信息
     * @return 数仓分层-规范管理编号
     */
    Long createDmDataLayerSpecification(DmDataLayerSpecificationSaveReqVO createReqVO);

    /**
     * 更新数仓分层-规范管理
     *
     * @param updateReqVO 数仓分层-规范管理信息
     */
    int updateDmDataLayerSpecification(DmDataLayerSpecificationSaveReqVO updateReqVO);

    /**
     * 删除数仓分层-规范管理
     *
     * @param idList 数仓分层-规范管理编号
     */
    int removeDmDataLayerSpecification(Collection<Long> idList);

    /**
     * 获得数仓分层-规范管理详情
     *
     * @param id 数仓分层-规范管理编号
     * @return 数仓分层-规范管理
     */
    DmDataLayerSpecificationDO getDmDataLayerSpecificationById(Long id);

    /**
     * 获得全部数仓分层-规范管理列表
     *
     * @return 数仓分层-规范管理列表
     */
    List<DmDataLayerSpecificationDO> getDmDataLayerSpecificationPage();

    /**
     * 获得全部数仓分层-规范管理 Map
     *
     * @return 数仓分层-规范管理 Map
     */
    Map<Long, DmDataLayerSpecificationDO> getDmDataLayerSpecificationMap();


    /**
     * 导入数仓分层-规范管理数据
     *
     * @param importExcelList 数仓分层-规范管理数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDmDataLayerSpecification(List<DmDataLayerSpecificationRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
