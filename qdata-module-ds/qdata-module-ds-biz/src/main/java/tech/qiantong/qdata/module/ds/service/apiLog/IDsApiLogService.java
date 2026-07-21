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

package tech.qiantong.qdata.module.ds.service.apiLog;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.ds.controller.admin.apiLog.vo.DsApiLogPageReqVO;
import tech.qiantong.qdata.module.ds.controller.admin.apiLog.vo.DsApiLogRespVO;
import tech.qiantong.qdata.module.ds.controller.admin.apiLog.vo.DsApiLogSaveReqVO;
import tech.qiantong.qdata.module.ds.dal.dataobject.apiLog.DsApiLogDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * API service call log service interface
 *
 * @author lhs
 * @date 2025-02-12
 */
public interface IDsApiLogService extends IService<DsApiLogDO> {

    /**
     * Returns a paginated API service call log list.
     *
     * @param pageReqVO pagination request
     * @return the paginated API service call log list
     */
    PageResult<DsApiLogDO> getDsApiLogPage(DsApiLogPageReqVO pageReqVO);

    /**
     * Creates an API service call log.
     *
     * @param createReqVO API service call log information
     * @return the API service call log ID
     */
    Long createDsApiLog(DsApiLogSaveReqVO createReqVO);

    /**
     * Updates an API service call log.
     *
     * @param updateReqVO API service call log information
     */
    int updateDsApiLog(DsApiLogSaveReqVO updateReqVO);

    /**
     * Deletes an API service call log.
     *
     * @param idList API service call log IDs
     */
    int removeDsApiLog(Collection<Long> idList);

    /**
     * Returns API service call log details.
     *
     * @param id API service call log ID
     * @return the API service call log
     */
    DsApiLogDO getDsApiLogById(Long id);

    /**
     * Returns all API service call logs.
     *
     * @return the API service call log list
     */
    List<DsApiLogDO> getDsApiLogList();

    /**
     * Returns all API service call logs as a map.
     *
     * @return API service call logs as a map
     */
    Map<Long, DsApiLogDO> getDsApiLogMap();


    /**
     * Imports API service call log data.
     *
     * @param importExcelList API service call log data list
     * @param isUpdateSupport whether existing records should be updated
     * @param operName operator
     * @return the result
     */
    String importDsApiLog(List<DsApiLogRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
