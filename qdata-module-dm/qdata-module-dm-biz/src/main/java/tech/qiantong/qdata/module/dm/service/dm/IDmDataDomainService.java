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
