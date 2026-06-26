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

package tech.qiantong.qdata.module.dp.service.model;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.domain.BatchDeleteCheck;
import tech.qiantong.qdata.common.core.domain.TreeData;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelPageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelSaveReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.model.DpModelDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 逻辑模型Service接口
 *
 * @author qdata
 * @date 2025-01-21
 */
public interface IDpModelService extends IService<DpModelDO> {

    /**
     * 获得逻辑模型分页列表
     *
     * @param pageReqVO 分页请求
     * @return 逻辑模型分页列表
     */
    PageResult<DpModelDO> getDpModelPage(DpModelPageReqVO pageReqVO);

    /**
     * 创建逻辑模型
     *
     * @param createReqVO 逻辑模型信息
     * @return 逻辑模型编号
     */
    Long createDpModel(DpModelSaveReqVO createReqVO);

    /**
     * 更新逻辑模型
     *
     * @param updateReqVO 逻辑模型信息
     */
    int updateDpModel(DpModelSaveReqVO updateReqVO);

    /**
     * 删除逻辑模型
     *
     * @param idList 逻辑模型编号
     */
    int removeDpModel(Collection<Long> idList);

    /**
     * 获得逻辑模型详情
     *
     * @param id 逻辑模型编号
     * @return 逻辑模型
     */
    DpModelDO getDpModelById(Long id);

    /**
     * 获得全部逻辑模型列表
     *
     * @return 逻辑模型列表
     */
    List<DpModelDO> getDpModelList();

    /**
     * 获得全部逻辑模型 Map
     *
     * @return 逻辑模型 Map
     */
    Map<Long, DpModelDO> getDpModelMap();


    /**
     * 导入逻辑模型数据
     *
     * @param importExcelList 逻辑模型数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    String importDpModel(List<DpModelRespVO> importExcelList, boolean isUpdateSupport, String operName);

    int removeDpModelAndColumnAll(List<Long> asList);

    Boolean updateStatus(Long id, Long status);

    /**
     * 获取树类目数据（多个数据组合而来
     *
     * @return
     */
    List<TreeData> getTreeData();

    /**
     * 查询发布模型列表
     *
     * @param pageReqVO
     * @return
     */
    PageResult<DpModelDO> getReleaseListPage(DpModelPageReqVO pageReqVO);

    /**
     * 批量删除模型数据检查
     *
     * @param ids
     * @return
     */
    BatchDeleteCheck<Long> batchDeleteCheck(List<Long> ids);
}
