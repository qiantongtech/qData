/*
 * Copyright (c) 2025-present Jiangsu Qiantong Technology Co., Ltd.
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

package tech.qiantong.qdata.module.dpp.service.qa;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskObjRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskObjSaveReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskObjPageReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.qa.DppQualityTaskObjDO;
/**
 * Data Quality Task Audit Object Service Interface
 *
 * @author Chaos
 * @date 2025-07-21
 */
public interface IDppQualityTaskObjService extends IService<DppQualityTaskObjDO> {

    /**
     * Get data quality task audit object pagination list
     *
     * @param pageReqVO Pagination request
     * @return Data quality task audit object pagination list
     */
    PageResult<DppQualityTaskObjDO> getDppQualityTaskObjPage(DppQualityTaskObjPageReqVO pageReqVO);

    /**
     * Create data quality task audit object
     *
     * @param createReqVO Data quality task audit object info
     * @return Data quality task audit object ID
     */
    Long createDppQualityTaskObj(DppQualityTaskObjSaveReqVO createReqVO);

    /**
     * Update data quality task audit object
     *
     * @param updateReqVO Data quality task audit object info
     */
    int updateDppQualityTaskObj(DppQualityTaskObjSaveReqVO updateReqVO);

    /**
     * Delete data quality task audit object
     *
     * @param idList Data quality task audit object ID list
     */
    int removeDppQualityTaskObj(Collection<Long> idList);

    /**
     * Get data quality task audit object detail
     *
     * @param id Data quality task audit object ID
     * @return Data quality task audit object
     */
    DppQualityTaskObjDO getDppQualityTaskObjById(Long id);

    /**
     * Get all data quality task audit object list
     *
     * @return Data quality task audit object list
     */
    List<DppQualityTaskObjDO> getDppQualityTaskObjList();
    List<DppQualityTaskObjDO> getDppQualityTaskObjList(DppQualityTaskObjPageReqVO pageReqVO);

    /**
     * Get all data quality task audit object Map
     *
     * @return Data quality task audit object Map
     */
    Map<Long, DppQualityTaskObjDO> getDppQualityTaskObjMap();


    /**
     * Import data quality task audit object data
     *
     * @param importExcelList Data quality task audit object data list
     * @param isUpdateSupport Whether to support update. If already exists, update the data
     * @param operName Operator
     * @return Result
     */
    String importDppQualityTaskObj(List<DppQualityTaskObjRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
