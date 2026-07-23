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

package tech.qiantong.qdata.module.dpp.service.etl;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskLogPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskLogRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskLogSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskLogDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Data Integration Task Log Service Interface
 *
 * @author qdata
 * @date 2025-02-13
 */
public interface IDppEtlTaskLogService extends IService<DppEtlTaskLogDO> {

    /**
     * Get data integration task log pagination list
     *
     * @param pageReqVO Pagination request
     * @return Data integration task log pagination list
     */
    PageResult<DppEtlTaskLogDO> getDppEtlTaskLogPage(DppEtlTaskLogPageReqVO pageReqVO);

    DppEtlTaskLogRespVO getDppEtlTaskLogById(DppEtlTaskLogPageReqVO pageReqVO);

    /**
     * Create data integration task log
     *
     * @param createReqVO Data integration task log info
     * @return Data integration task log ID
     */
    Long createDppEtlTaskLog(DppEtlTaskLogSaveReqVO createReqVO);

    /**
     * Update data integration task log
     *
     * @param updateReqVO Data integration task log info
     */
    int updateDppEtlTaskLog(DppEtlTaskLogSaveReqVO updateReqVO);

    /**
     * Delete data integration task log
     *
     * @param idList Data integration task log ID list
     */
    int removeDppEtlTaskLog(Collection<Long> idList);

    /**
     * Get data integration task log detail
     *
     * @param id Data integration task log ID
     * @return Data integration task log
     */
    DppEtlTaskLogDO getDppEtlTaskLogById(Long id);

    /**
     * Get all data integration task log list
     *
     * @return Data integration task log list
     */
    List<DppEtlTaskLogDO> getDppEtlTaskLogList();

    /**
     * Get all data integration task log Map
     *
     * @return Data integration task log Map
     */
    Map<Long, DppEtlTaskLogDO> getDppEtlTaskLogMap();


    /**
     * Import data integration task log data
     *
     * @param importExcelList Data integration task log data list
     * @param isUpdateSupport Whether to support update. If already exists, update the data
     * @param operName        Operator
     * @return Result
     */
    String importDppEtlTaskLog(List<DppEtlTaskLogRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * Get max version number by task code
     *
     * @param taskCode
     * @return
     */
    Integer queryMaxVersionByCode(String taskCode);
}
