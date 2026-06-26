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

package tech.qiantong.qdata.module.dm.service.businessCategory;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo.DmBusinessDomainRelPageReqVO;
import tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo.DmBusinessDomainRelRespVO;
import tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo.DmBusinessDomainRelSaveReqVO;
import tech.qiantong.qdata.module.dm.dal.dataobject.businessCategory.DmBusinessDomainRelDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 业务分类数据域关联关系Service接口
 *
 * @author qdata
 * @date 2026-04-12
 */
public interface IDmBusinessDomainRelService extends IService<DmBusinessDomainRelDO> {

    /**
     * 获得业务分类数据域关联关系分页列表
     *
     * @param pageReqVO 分页请求
     * @return 业务分类数据域关联关系分页列表
     */
    PageResult<DmBusinessDomainRelDO> getDmBusinessDomainRelPage(DmBusinessDomainRelPageReqVO pageReqVO);

    /**
     * 创建业务分类数据域关联关系
     *
     * @param createReqVO 业务分类数据域关联关系信息
     * @return 业务分类数据域关联关系编号
     */
    Long createDmBusinessDomainRel(DmBusinessDomainRelSaveReqVO createReqVO);

    /**
     * 更新业务分类数据域关联关系
     *
     * @param updateReqVO 业务分类数据域关联关系信息
     */
    int updateDmBusinessDomainRel(DmBusinessDomainRelSaveReqVO updateReqVO);

    /**
     * 删除业务分类数据域关联关系
     *
     * @param idList 业务分类数据域关联关系编号
     */
    int removeDmBusinessDomainRel(Collection<Long> idList);

    /**
     * 获得业务分类数据域关联关系详情
     *
     * @param id 业务分类数据域关联关系编号
     * @return 业务分类数据域关联关系
     */
    DmBusinessDomainRelDO getDmBusinessDomainRelById(Long id);

    /**
     * 获得全部业务分类数据域关联关系列表
     *
     * @return 业务分类数据域关联关系列表
     */
    List<DmBusinessDomainRelDO> getDmBusinessDomainRelList();

    /**
     * 获得全部业务分类数据域关联关系 Map
     *
     * @return 业务分类数据域关联关系 Map
     */
    Map<Long, DmBusinessDomainRelDO> getDmBusinessDomainRelMap();


    /**
     * 导入业务分类数据域关联关系数据
     *
     * @param importExcelList 业务分类数据域关联关系数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDmBusinessDomainRel(List<DmBusinessDomainRelRespVO> importExcelList, boolean isUpdateSupport, String operName);

    Integer removeDmBusinessDomainRelByDomainId(Long domainId, Long businessCategoryId);
}
