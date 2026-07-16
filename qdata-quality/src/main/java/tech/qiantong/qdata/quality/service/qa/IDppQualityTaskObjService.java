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

package tech.qiantong.qdata.quality.service.qa;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityTaskObjPageReqVO;
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityTaskObjRespVO;
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityTaskObjSaveReqVO;
import tech.qiantong.qdata.quality.dal.dataobject.qa.DppQualityTaskObjDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * Data quality task-audit object Service interface
 *
 * @author Chaos
 * @date 2025-07-21
 */
public interface IDppQualityTaskObjService extends IService<DppQualityTaskObjDO> {

    /**
     * Obtain data quality task-audit object paging list
     *
     * @param pageReqVO paging request
     * @return Data quality task-paginated list of audit objects
     */
    PageResult<DppQualityTaskObjDO> getDppQualityTaskObjPage(DppQualityTaskObjPageReqVO pageReqVO);

    /**
     * Create data quality task-audit object
     *
     * @param createReqVO Data quality task-audit object information
     * @return Data quality task-audit object number
     */
    Long createDppQualityTaskObj(DppQualityTaskObjSaveReqVO createReqVO);

    /**
     * Update data quality task-audit object
     *
     * @param updateReqVO Data quality task-audit object information
     */
    int updateDppQualityTaskObj(DppQualityTaskObjSaveReqVO updateReqVO);

    /**
     * Delete data quality task-audit object
     *
     * @param idList Data quality task-audit object number
     */
    int removeDppQualityTaskObj(Collection<Long> idList);

    /**
     * Obtain data quality task-audit object details
     *
     * @param id data quality task-audit object number
     * @return Data quality task-audit object
     */
    DppQualityTaskObjDO getDppQualityTaskObjById(Long id);

    List<DppQualityTaskObjDO> getDppQualityTaskObjList(String taskId);

    /**
     * Obtain all data quality tasks-audit object list
     *
     * @return Data quality task-audit object list
     */
    List<DppQualityTaskObjDO> getDppQualityTaskObjList();

    /**
     * Obtain all data quality tasks-audit object map
     *
     * @return Data quality task-audit object Map
     */
    Map<Long, DppQualityTaskObjDO> getDppQualityTaskObjMap();


    /**
     * Import data quality tasks-audit object data
     *
     * @param importExcelList Data quality task-audit object data list
     * @param isUpdateSupport Whether to update support, if it already exists, update the data
     * @param operName operating user
     * @return result
     */
    String importDppQualityTaskObj(List<DppQualityTaskObjRespVO> importExcelList, boolean isUpdateSupport, String operName);
}
