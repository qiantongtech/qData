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

package tech.qiantong.qdata.module.dp.service.dataElem;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemPageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemSaveReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.dataElem.DpDataElemDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Data Element Service Interface
 *
 * @author qdata
 * @date 2025-01-21
 */
public interface IDpDataElemService extends IService<DpDataElemDO> {

    /**
     * Get data element paginated list
     *
     * @param pageReqVO Pagination request
     * @return Paginated list of data elements
     */
    PageResult<DpDataElemDO> getDpDataElemPage(DpDataElemPageReqVO pageReqVO);

    List<DpDataElemDO> getDpDataElemList(DpDataElemPageReqVO pageReqVO);

    /**
     * Create data element
     *
     * @param createReqVO Data element information
     * @return Data element ID
     */
    Long createDpDataElem(DpDataElemSaveReqVO createReqVO);

    /**
     * Update data element
     *
     * @param updateReqVO Data element information
     */
    int updateDpDataElem(DpDataElemSaveReqVO updateReqVO);

    /**
     * Delete data element
     *
     * @param idList Data element ID list
     */
    int removeDpDataElem(List<Long> idList);

    /**
     * Get data element details
     *
     * @param id Data element ID
     * @return Data element
     */
    DpDataElemDO getDpDataElemById(Long id);

    /**
     * Get all data element list
     *
     * @return Data element list
     */
    List<DpDataElemDO> getDpDataElemList();

    /**
     * Get all data element Map
     *
     * @return Data element Map
     */
    Map<Long, DpDataElemDO> getDpDataElemMap();


    /**
     * Import data element data
     *
     * @param importExcelList Data element data list
     * @param isUpdateSupport Whether to support update, if exists then update the data
     * @param operName        Operator
     * @return Result
     */
    String importDpDataElem(List<DpDataElemRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * Update data element status
     *
     * @param id
     * @param status
     * @return
     */
    Boolean updateStatus(Long id, Long status);
}
