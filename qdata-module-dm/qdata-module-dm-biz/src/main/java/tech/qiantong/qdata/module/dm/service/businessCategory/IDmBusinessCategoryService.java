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
import tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo.DmBusinessCategoryPageReqVO;
import tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo.DmBusinessCategoryRespVO;
import tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo.DmBusinessCategorySaveReqVO;
import tech.qiantong.qdata.module.dm.dal.dataobject.businessCategory.DmBusinessCategoryDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 业务分类Service接口
 *
 * @author qdata
 * @date 2026-04-08
 */
public interface IDmBusinessCategoryService extends IService<DmBusinessCategoryDO> {

    /**
     * 获得业务分类分页列表
     *
     * @param pageReqVO 分页请求
     * @return 业务分类分页列表
     */
    PageResult<DmBusinessCategoryDO> getDmBusinessCategoryPage(DmBusinessCategoryPageReqVO pageReqVO);

    /**
     * 创建业务分类
     *
     * @param createReqVO 业务分类信息
     * @return 业务分类编号
     */
    Long createDmBusinessCategory(DmBusinessCategorySaveReqVO createReqVO);

    /**
     * 更新业务分类
     *
     * @param updateReqVO 业务分类信息
     */
    int updateDmBusinessCategory(DmBusinessCategorySaveReqVO updateReqVO);

    /**
     * 删除业务分类
     *
     * @param idList 业务分类编号
     */
    int removeDmBusinessCategory(Collection<Long> idList);

    /**
     * 获得业务分类详情
     *
     * @param id 业务分类编号
     * @return 业务分类
     */
    DmBusinessCategoryDO getDmBusinessCategoryById(Long id);

    /**
     * 获得全部业务分类列表
     *
     * @return 业务分类列表
     */
    List<DmBusinessCategoryDO> getDmBusinessCategoryList(DmBusinessCategoryPageReqVO dmBusinessCategory);

    /**
     * 获得全部业务分类 Map
     *
     * @return 业务分类 Map
     */
    Map<Long, DmBusinessCategoryDO> getDmBusinessCategoryMap();


    /**
     * 导入业务分类数据
     *
     * @param importExcelList 业务分类数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDmBusinessCategory(List<DmBusinessCategoryRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 生成code
     *
     * @param parentId
     * @param parentCode
     * @return
     */
    String createCode(Long parentId, String parentCode);

    /**
     * 更改指定pid下的所有code
     *
     * @param pid
     */
    void changeCodeByPid(Long pid, String parentCode);
}
