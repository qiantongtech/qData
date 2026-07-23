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
 * Logical Model Service Interface
 *
 * @author qdata
 * @date 2025-01-21
 */
public interface IDpModelService extends IService<DpModelDO> {

    /**
     * Get Logical Model Paginated List
     *
     * @param pageReqVO Pagination Request
     * @return Logical Model Paginated List
     */
    PageResult<DpModelDO> getDpModelPage(DpModelPageReqVO pageReqVO);

    /**
     * Create Logical Model
     *
     * @param createReqVO Logical Model Information
     * @return Logical Model ID
     */
    Long createDpModel(DpModelSaveReqVO createReqVO);

    /**
     * Update Logical Model
     *
     * @param updateReqVO Logical Model Information
     */
    int updateDpModel(DpModelSaveReqVO updateReqVO);

    /**
     * Delete Logical Model
     *
     * @param idList Logical Model ID
     */
    int removeDpModel(Collection<Long> idList);

    /**
     * Get Logical Model Details
     *
     * @param id Logical Model ID
     * @return Logical Model
     */
    DpModelDO getDpModelById(Long id);

    /**
     * Get All Logical Model List
     *
     * @return Logical Model List
     */
    List<DpModelDO> getDpModelList();

    /**
     * Get All Logical Model Map
     *
     * @return Logical Model Map
     */
    Map<Long, DpModelDO> getDpModelMap();


    /**
     * Import Logical Model Data
     *
     * @param importExcelList Logical Model Data List
     * @param isUpdateSupport Whether to support update, if exists then update the data
     * @param operName        Operator
     * @return Result
     */
    String importDpModel(List<DpModelRespVO> importExcelList, boolean isUpdateSupport, String operName);

    int removeDpModelAndColumnAll(List<Long> asList);

    Boolean updateStatus(Long id, Long status);

    /**
     * Get tree category data (combined from multiple data sources)
     *
     * @return
     */
    List<TreeData> getTreeData();

    /**
     * Query published model list
     *
     * @param pageReqVO
     * @return
     */
    PageResult<DpModelDO> getReleaseListPage(DpModelPageReqVO pageReqVO);

    /**
     * Batch delete model data check
     *
     * @param ids
     * @return
     */
    BatchDeleteCheck<Long> batchDeleteCheck(List<Long> ids);
}
